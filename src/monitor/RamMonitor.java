package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class RamMonitor implements Runnable {

    private final HistoryBuffer history;
    private final int interval;
    private final CsvLogger logger;

    private volatile int currentValue;

    public RamMonitor(HistoryBuffer history, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
    }

    @Override
    public void run() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        while(true) {
            long total = osBean.getTotalPhysicalMemorySize();
            long free = osBean.getFreePhysicalMemorySize();
            currentValue = (int)((double)(total - free) / total * 100);

            history.add(currentValue);
            logger.log(-1, currentValue, -1); // -1 = ignorovat CPU a Disk

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
