package ui;

import history.HistoryBuffer;
import history.HistoryGraphPanel;
import logging.CsvLogger;
import monitor.CpuMonitor;
import monitor.RamMonitor;
import monitor.DiskMonitor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class MainWindow {

    private HistoryBuffer cpuHistory;
    private HistoryBuffer ramHistory;
    private HistoryBuffer diskHistory;

    private int interval;

    public void start() {
        // Načtení konfigurace
        Properties config = CsvLogger.loadConfig();
        interval = Integer.parseInt(config.getProperty("update.interval", "1000"));
        int historySize = Integer.parseInt(config.getProperty("history.size", "60"));
        String diskPath = config.getProperty("disk.path", "C:/");

        cpuHistory = new HistoryBuffer(historySize);
        ramHistory = new HistoryBuffer(historySize);
        diskHistory = new HistoryBuffer(historySize);

        CsvLogger logger;
        try {
            logger = new CsvLogger(config.getProperty("csv.path", "system_log.csv"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        CpuMonitor cpu = new CpuMonitor(cpuHistory, interval, logger);
        RamMonitor ram = new RamMonitor(ramHistory, interval, logger);
        DiskMonitor disk = new DiskMonitor(diskHistory, diskPath, interval, logger);

        new Thread(cpu).start();
        new Thread(ram).start();
        new Thread(disk).start();

        SwingUtilities.invokeLater(() -> createUI(cpu, ram, disk));
    }

    private void createUI(CpuMonitor cpu, RamMonitor ram, DiskMonitor disk) {
        JFrame frame = new JFrame("System Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JProgressBar cpuBar = createBar("CPU");
        JProgressBar ramBar = createBar("RAM");
        JProgressBar diskBar = createBar("Disk");

        HistoryGraphPanel graph = new HistoryGraphPanel(cpuHistory, ramHistory, diskHistory);

        Timer timer = new Timer(interval, e -> {
            cpuBar.setValue(cpu.getCurrentValue());
            ramBar.setValue(ram.getCurrentValue());
            diskBar.setValue(disk.getCurrentValue());
            graph.repaint();
        });
        timer.start();

        JPanel bars = new JPanel(new GridLayout(3, 1));
        bars.add(cpuBar);
        bars.add(ramBar);
        bars.add(diskBar);

        frame.setLayout(new BorderLayout());
        frame.add(bars, BorderLayout.NORTH);
        frame.add(graph, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JProgressBar createBar(String title) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setBorder(BorderFactory.createTitledBorder(title));
        return bar;
    }
}
