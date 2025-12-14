package monitor;

import history.HistoryBuffer;
import logging.CsvLogger;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

/**
 * Monitor vytížení procesoru (CPU).
 * <p>
 * Třída pravidelně zjišťuje aktuální zatížení CPU pomocí
 * {@link OperatingSystemMXBean} a ukládá naměřené hodnoty
 * do {@link HistoryBuffer}. Je určena ke spuštění ve
 * vlastním vlákně.
 * </p>
 */
public class CpuMonitor implements Runnable {

    /** Buffer pro ukládání historických hodnot CPU */
    private final HistoryBuffer history;

    /** Interval aktualizace v milisekundách */
    private final int interval;

    /** Logger pro ukládání dat do CSV (volitelný) */
    private final CsvLogger logger;

    /** Aktuální hodnota vytížení CPU */
    private volatile int currentValue = 0;

    /**
     * Vytvoří nový monitor CPU.
     *
     * @param history  buffer pro ukládání historických hodnot
     * @param interval interval měření v milisekundách
     * @param logger   logger pro zápis do CSV souboru
     */
    public CpuMonitor(HistoryBuffer history, int interval, CsvLogger logger) {
        this.history = history;
        this.interval = interval;
        this.logger = logger;
    }

    /**
     * Hlavní smyčka monitoru.
     * <p>
     * V pravidelných intervalech zjišťuje vytížení CPU,
     * převádí hodnotu na procenta a ukládá ji do historie.
     * </p>
     */
    @Override
    public void run() {
        OperatingSystemMXBean osBean =
                ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        while (true) {
            double load = osBean.getSystemCpuLoad();
            currentValue = (int) (load * 100);
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
     * Vrací aktuální hodnotu vytížení CPU.
     *
     * @return vytížení CPU v procentech (0–100)
     */
    public int getCurrentValue() {
        return currentValue;
    }
}
