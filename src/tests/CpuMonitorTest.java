package tests;

import monitor.CpuMonitor;
import history.HistoryBuffer;
import logging.CsvLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CpuMonitorTest {

    @Test
    void testCpuValueRange() throws Exception {
        HistoryBuffer buffer = new HistoryBuffer(5);
        CsvLogger logger = new CsvLogger("cpu_test.csv");
        CpuMonitor cpu = new CpuMonitor(buffer, 100, logger);

        new Thread(cpu).start();
        Thread.sleep(150); // necháme změřit jednu hodnotu
        int value = cpu.getCurrentValue();
        assertTrue(value >= 0 && value <= 100, "CPU usage mimo rozsah");
    }
}
