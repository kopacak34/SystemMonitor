package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.io.File;

/**
 * Monitor využití diskového prostoru.
 * <p>
 * Třída periodicky zjišťuje obsazenost disku (v procentech)
 * pro zadanou cestu a ukládá hodnoty do {@link HistoryBuffer}.
 * Je určena ke spuštění ve vlastním vlákně.
 * </p>
 */
public class DiskMonitor implements Runnable {

    /** Buffer pro ukládání historických hodnot využití disku */
    private final HistoryBuffer history;

    /** Interval aktualizace v milisekundách */
    private final int interval;

    /** Logger pro ukládání dat do CSV (volitelný) */
    private final CsvLogger logger;

    /** Disk nebo oddíl, který je monitorován */
    private final File disk;

    /** Aktuální hodnota využití disku */
    private volatile int currentValue = 0;

    /**
     * Vytvoří nový monitor disku.
     *
     * @param history  buffer pro ukládání historických hodnot
     * @param path     cesta k disku nebo oddílu (např. {@code "C:/"})
     * @param interval interval měření v milisekundách
     * @param logger   logger pro zápis do CSV souboru
     */
    public DiskMonitor(HistoryBuffer history, String path, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
        this.disk = new File(path);
    }

    /**
     * Hlavní smyčka monitoru.
     * <p>
     * Pravidelně zjišťuje celkovou a volnou kapacitu disku,
     * vypočítá procentuální využití a uloží hodnotu do historie.
     * </p>
     */
    @Override
    public void run() {
        while (true) {
            long total = disk.getTotalSpace();
            long free = disk.getFreeSpace();

            currentValue = (int) ((double) (total - free) / total * 100);
            history.add(currentValue);

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // Přerušení vlákna – vypíše chybu a pokračuje
                e.printStackTrace();
            }
        }
    }

    /**
     * Vrací aktuální hodnotu využití disku.
     *
     * @return využití disku v procentech (0–100)
     */
    public int getCurrentValue() {
        return currentValue;
    }
}
