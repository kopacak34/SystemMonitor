package monitor;

import history.HistoryBuffer;
import java.io.File;

public class DiskMonitor implements Runnable {

    private final HistoryBuffer history;
    private volatile int currentValue;
    private final File disk;

    public DiskMonitor(HistoryBuffer history, String path) {
        this.history = history;
        this.disk = new File(path);
    }

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public void run() {
        while (true) {
            long total = disk.getTotalSpace();
            long free = disk.getFreeSpace();
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
