package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class CpuMonitor implements Runnable {

    private final HistoryBuffer history;
    private final int interval;
    private final CsvLogger logger;

    private volatile int currentValue;

    public CpuMonitor(HistoryBuffer history, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
    }

    @Override
    public void run() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        while(true) {
            double load = osBean.getSystemCpuLoad();
            currentValue = (int)(load * 100);
            history.add(currentValue);
            logger.log(currentValue, -1, -1); // -1 znamená, že hodnotu ignorovat

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
