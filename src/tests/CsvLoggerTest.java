package tests;

import logging.CsvLogger;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CsvLoggerTest {

    @Test
    void testLogging() throws IOException {
        String path = "test_log.csv";
        CsvLogger logger = new CsvLogger(path);
        logger.log(10, 20, 30);

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String header = reader.readLine();
            assertEquals("Timestamp,CPU,RAM,DISK", header);

            String data = reader.readLine();
            assertNotNull(data);
            assertTrue(data.contains("10,20,30"));
        }
    }
}

