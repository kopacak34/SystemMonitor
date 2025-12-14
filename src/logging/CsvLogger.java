package logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Třída {@code CsvLogger} zajišťuje ukládání naměřených hodnot
 * využití systému do CSV souboru.
 * <p>
 * Logger zapisuje časovou značku, vytížení CPU, RAM a disku
 * v pravidelných intervalech. Data jsou ukládána v append módu,
 * aby nedocházelo k přepisování existujících záznamů.
 * </p>
 *
 * <p>
 * Součástí třídy je také statická metoda pro načtení konfiguračního
 * souboru {@code config.properties}.
 * </p>
 */
public class CsvLogger {

    /** Zápisový proud pro CSV soubor */
    private final FileWriter writer;

    /**
     * Vytvoří nový CSV logger.
     * <p>
     * Pokud soubor neexistuje nebo je prázdný, zapíše se hlavička CSV.
     * Jinak se nové záznamy pouze přidávají na konec souboru.
     * </p>
     *
     * @param path cesta k CSV souboru
     * @throws IOException pokud dojde k chybě při otevření souboru
     */
    public CsvLogger(String path) throws IOException {
        File file = new File(path);
        boolean writeHeader = !file.exists() || file.length() == 0;

        writer = new FileWriter(file, true); // append = true
        if (writeHeader) {
            writer.write("Timestamp,CPU,RAM,DISK\n");
        }
    }

    /**
     * Zapíše jeden řádek s aktuálními hodnotami do CSV souboru.
     * <p>
     * Metoda je synchronizovaná, aby bylo zajištěno bezpečné
     * volání z více vláken (CPU, RAM a Disk monitor).
     * </p>
     *
     * @param cpu  aktuální vytížení CPU v procentech
     * @param ram  aktuální využití RAM v procentech
     * @param disk aktuální využití disku v procentech
     */
    public synchronized void log(int cpu, int ram, int disk) {
        try {
            writer.write(LocalDateTime.now() + "," + cpu + "," + ram + "," + disk + "\n");
            writer.flush();
        } catch (IOException ignored) {
            // Záměrně ignorováno – logování nesmí shodit aplikaci
        }
    }

    /**
     * Načte konfigurační soubor aplikace.
     * <p>
     * Konfigurační soubor se očekává v cestě {@code config/config.properties}.
     * Pokud soubor neexistuje nebo jej nelze načíst, použijí se
     * výchozí hodnoty.
     * </p>
     *
     * @return objekt {@link Properties} s konfigurací aplikace
     */
    public static Properties loadConfig() {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream(
                Paths.get("config", "config.properties").toFile())) {

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
