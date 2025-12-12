package logging;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;

public class CsvLogger {

    private final FileWriter writer;

    public CsvLogger(String path) throws IOException {
        writer = new FileWriter(path, true); // append = true
        writer.write("Timestamp,CPU,RAM,DISK\n");
    }

    public synchronized void log(int cpu, int ram, int disk) {
        try {
            writer.write(LocalDateTime.now() + "," + cpu + "," + ram + "," + disk + "\n");
            writer.flush();
        } catch (IOException ignored) {}
    }

    public static Properties loadConfig() {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream(Paths.get("config", "config.properties").toFile())) {
            config.load(fis);
        } catch (IOException e) {
            System.out.println("Config soubor nenalezen, použity default hodnoty.");
            config.setProperty("update.interval", "1000");
            config.setProperty("history.size", "60");
            config.setProperty("disk.path", "C:/");
            config.setProperty("csv.path", "system_log.csv");
        }
        return config;
    }
}
