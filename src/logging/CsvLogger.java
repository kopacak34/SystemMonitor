package logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvLogger {

    private final FileWriter writer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CsvLogger() throws IOException {
        File file = new File("system_log.csv");
        boolean isNewFile = !file.exists() || file.length() == 0;
        writer = new FileWriter(file, true); // true = append mode

        if (isNewFile) {
            writer.write("Timestamp,CPU,RAM,DISK\n");
            writer.flush();
        }
    }

    public synchronized void log(int cpu, int ram, int disk) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.write(String.format("%s,%d,%d,%d%n", timestamp, cpu, ram, disk));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
