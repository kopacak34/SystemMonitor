import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemMonitor {


    private static JProgressBar cpuBar;
    private static JProgressBar ramBar;
    private static JProgressBar diskBar;
    private static JTextArea logArea;


    private static FileWriter csvWriter;


    private static volatile int cpuPercent = 0;
    private static volatile int ramPercent = 0;
    private static volatile int diskPercent = 0;

    public static void main(String[] args) {
        setupCSV();
        createGUI();


        Thread cpuThread = new Thread(SystemMonitor::monitorCPU);
        Thread ramThread = new Thread(SystemMonitor::monitorRAM);
        Thread diskThread = new Thread(SystemMonitor::monitorDisk);
        Thread loggerThread = new Thread(SystemMonitor::writeLogLoop);

        cpuThread.start();
        ramThread.start();
        diskThread.start();
        loggerThread.start();
    }

    private static void createGUI() {
        JFrame frame = new JFrame("System Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        cpuBar.setBorder(BorderFactory.createTitledBorder("CPU Usage"));

        ramBar = new JProgressBar(0, 100);
        ramBar.setStringPainted(true);
        ramBar.setBorder(BorderFactory.createTitledBorder("RAM Usage"));

        diskBar = new JProgressBar(0, 100);
        diskBar.setStringPainted(true);
        diskBar.setBorder(BorderFactory.createTitledBorder("Disk Usage"));

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.setLayout(new GridLayout(4, 1));
        frame.add(cpuBar);
        frame.add(ramBar);
        frame.add(diskBar);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private static void setupCSV() {
        try {
            csvWriter = new FileWriter("system_log.csv");
            csvWriter.append("Timestamp,CPU,RAM,Disk\n");
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void monitorCPU() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        while(true) {
            double load = osBean.getSystemCpuLoad(); // mezi 0 a 1
            cpuPercent = (int)(load * 100);
            SwingUtilities.invokeLater(() -> cpuBar.setValue(cpuPercent));
            sleep(1000);
        }
    }

    private static void monitorRAM() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        while (true) {
            long totalPhysical = osBean.getTotalPhysicalMemorySize();
            long freePhysical = osBean.getFreePhysicalMemorySize();
            ramPercent = (int)((double)(totalPhysical - freePhysical) / totalPhysical * 100);

            SwingUtilities.invokeLater(() -> ramBar.setValue(ramPercent));
            sleep(1000);
        }
    }


    private static void monitorDisk() {
        File disk = new File("C:/"); // změň podle OS
        while(true) {
            long freeSpace = disk.getFreeSpace();
            long totalSpace = disk.getTotalSpace();
            diskPercent = (int)((double)(totalSpace - freeSpace) / totalSpace * 100);
            SwingUtilities.invokeLater(() -> diskBar.setValue(diskPercent));
            sleep(1000);
        }
    }


    private static void writeLogLoop() {
        while(true) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logLine = String.format("%s,%d,%d,%d\n", timestamp, cpuPercent, ramPercent, diskPercent);

            SwingUtilities.invokeLater(() -> logArea.append(logLine));

            try {
                csvWriter.append(logLine);
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sleep(1000);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

