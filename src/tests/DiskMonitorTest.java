package tests;

import monitor.DiskMonitor;
import history.HistoryBuffer;
import logging.CsvLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiskMonitorTest {

    @Test
    void testDiskValueRange() throws Exception {
        HistoryBuffer buffer = new HistoryBuffer(5);
        CsvLogger logger = new CsvLogger("disk_test.csv");
        DiskMonitor disk = new DiskMonitor(buffer, "C:/", 100, logger);

        new Thread(disk).start();
        Thread.sleep(150);
        int value = disk.getCurrentValue();
        assertTrue(value >= 0 && value <= 100, "Disk usage mimo rozsah");
    }
}
