package ui;

import history.*;
import logging.CsvLogger;
import monitor.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow {

    private final HistoryBuffer cpuHistory = new HistoryBuffer(60);
    private final HistoryBuffer ramHistory = new HistoryBuffer(60);
    private final HistoryBuffer diskHistory = new HistoryBuffer(60);

    private CsvLogger logger; // přidáno

    public void start() {
        // vytvoření loggeru
        try {
            logger = new CsvLogger();
        } catch (IOException e) {
            e.printStackTrace();
            return; // pokud se logger nepodaří, ukončíme start
        }

        CpuMonitor cpu = new CpuMonitor(cpuHistory);
        RamMonitor ram = new RamMonitor(ramHistory);
        DiskMonitor disk = new DiskMonitor(diskHistory, "C:/");

        new Thread(cpu).start();
        new Thread(ram).start();
        new Thread(disk).start();

        // vlákno pro zapisování logu
        new Thread(() -> {
            while (true) {
                logger.log(cpu.getCurrentValue(), ram.getCurrentValue(), disk.getCurrentValue());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        SwingUtilities.invokeLater(() -> createUI(cpu, ram, disk));
    }

    private void createUI(CpuMonitor cpu, RamMonitor ram, DiskMonitor disk) {
        JFrame frame = new JFrame("System Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JProgressBar cpuBar = createBar("CPU");
        JProgressBar ramBar = createBar("RAM");
        JProgressBar diskBar = createBar("Disk");

        HistoryGraphPanel graph =
                new HistoryGraphPanel(cpuHistory, ramHistory, diskHistory);

        Timer timer = new Timer(1000, e -> {
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
