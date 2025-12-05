import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;


public class SystemMonitor {

    private static JProgressBar cpuBar;
    private static JProgressBar ramBar;

    private static volatile int cpuPercent = 0;
    private static volatile int ramPercent = 0;


    public static void main(String[] args) {

        createGUI();
        Thread cpuThread = new Thread(SystemMonitor::monitorCPU);
        Thread ramThread = new Thread(SystemMonitor::monitorRAM);
        cpuThread.start();
        ramThread.start();
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


        frame.setLayout(new GridLayout(4, 1));
        frame.add(cpuBar);
        frame.add(ramBar);
        frame.setVisible(true);
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



    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

