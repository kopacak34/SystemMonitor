package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

/**
 * Monitor využití operační paměti (RAM).
 * <p>
 * Třída periodicky zjišťuje využití fyzické paměti systému
 * a ukládá hodnoty do {@link HistoryBuffer}. Je navržena
 * ke spuštění v samostatném vlákně.
 * </p>
 */
public class RamMonitor implements Runnable {

    /** Buffer pro ukládání historických hodnot využití RAM */
    private final HistoryBuffer history;

    /** Interval aktualizace v milisekundách */
    private final int interval;

    /** Logger pro zápis hodnot do CSV souboru */
    private final CsvLogger logger;

    /** Aktuální hodnota využití RAM v procentech */
    private volatile int currentValue = 0;

    /**
     * Vytvoří nový monitor RAM.
     *
     * @param history  buffer pro ukládání historických hodnot
     * @param interval interval měření v milisekundách
     * @param logger   logger pro ukládání hodnot do CSV souboru
     */
    public RamMonitor(HistoryBuffer history, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
    }

    /**
     * Hlavní smyčka monitoru RAM.
     * <p>
     * Pravidelně zjišťuje celkovou a volnou fyzickou paměť systému,
     * vypočítá procentuální využití a uloží hodnotu do historie.
     * </p>
     */
    @Override
    public void run() {
        OperatingSystemMXBean osBean =
                ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        while (true) {
            long totalMemory = osBean.getTotalPhysicalMemorySize();
            long freeMemory = osBean.getFreePhysicalMemorySize();

            currentValue = (int) ((double) (totalMemory - freeMemory) / totalMemory * 100);
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
     * Vrací aktuální hodnotu využití RAM.
     *
     * @return využití RAM v procentech (0–100)
     */
    public int getCurrentValue() {
        return currentValue;
    }
}
