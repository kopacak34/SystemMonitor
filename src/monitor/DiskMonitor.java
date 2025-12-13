package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.io.File;

public class DiskMonitor implements Runnable {

    private final HistoryBuffer history;
    private final int interval;
    private final CsvLogger logger;
    private final File disk;
    private volatile int currentValue = 0;

    public DiskMonitor(HistoryBuffer history, String path, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
        this.disk = new File(path);
    }

    @Override
    public void run() {
        while (true) {
            long total = disk.getTotalSpace();
            long free = disk.getFreeSpace();
            currentValue = (int)((double)(total - free) / total * 100);
            history.add(currentValue);

            try { Thread.sleep(interval); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public int getCurrentValue() { return currentValue; }
}
