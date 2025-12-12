package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.io.File;

public class DiskMonitor implements Runnable {

    private final HistoryBuffer history;
    private final String path;
    private final int interval;
    private final CsvLogger logger;

    private volatile int currentValue;

    public DiskMonitor(HistoryBuffer history, String path, int interval, CsvLogger logger) {
        this.history = history;
        this.path = path;
        this.interval = interval;
        this.logger = logger;
    }

    @Override
    public void run() {
        File disk = new File(path);

        while(true) {
            long total = disk.getTotalSpace();
            long free = disk.getFreeSpace();
            currentValue = (int)((double)(total - free) / total * 100);

            history.add(currentValue);
            logger.log(-1, -1, currentValue); // -1 = ignorovat CPU a RAM

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
