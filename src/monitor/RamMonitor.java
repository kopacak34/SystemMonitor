package monitor;

import history.HistoryBuffer;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class RamMonitor implements Runnable {

    private final HistoryBuffer history;
    private volatile int currentValue;

    public RamMonitor(HistoryBuffer history) {
        this.history = history;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public void run() {
        OperatingSystemMXBean os =
                ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        while (true) {
            long total = os.getTotalPhysicalMemorySize();
            long free = os.getFreePhysicalMemorySize();
            currentValue = (int) ((total - free) * 100 / total);
            history.add(currentValue);
            sleep();
        }
    }

    private void sleep() {
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) {}
    }
}
