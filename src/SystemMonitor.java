import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemMonitor {

    private static JProgressBar cpuBar;

    private static volatile int cpuPercent = 0;


    public static void main(String[] args) {

        createGUI();
        Thread cpuThread = new Thread(SystemMonitor::monitorCPU);
        cpuThread.start();
    }

    private static void createGUI() {
        JFrame frame = new JFrame("System Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        cpuBar.setBorder(BorderFactory.createTitledBorder("CPU Usage"));



        frame.setLayout(new GridLayout(4, 1));
        frame.add(cpuBar);
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


    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

