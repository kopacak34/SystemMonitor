package tests;

import monitor.RamMonitor;
import history.HistoryBuffer;
import logging.CsvLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RamMonitorTest {

    @Test
    void testRamValueRange() throws Exception {
        HistoryBuffer buffer = new HistoryBuffer(5);
        CsvLogger logger = new CsvLogger("ram_test.csv");
        RamMonitor ram = new RamMonitor(buffer, 100, logger);

        new Thread(ram).start();
        Thread.sleep(150);
        int value = ram.getCurrentValue();
        assertTrue(value >= 0 && value <= 100, "RAM usage mimo rozsah");
    }
}
