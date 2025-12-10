package monitor;

import history.HistoryBuffer;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class CpuMonitor implements Runnable {

    private final HistoryBuffer history;
    private volatile int currentValue;

    public CpuMonitor(HistoryBuffer history) {
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
            currentValue = (int) (os.getSystemCpuLoad() * 100);
            history.add(currentValue);
            sleep();
        }
    }

    private void sleep() {
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) {}
    }
}
