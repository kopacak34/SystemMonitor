package history;

import java.util.LinkedList;
import java.util.List;

/**
 * Kruhový buffer pro ukládání historie celočíselných hodnot.
 * <p>
 * Třída slouží k uchovávání posledních {@code maxSize} hodnot
 * (např. využití CPU, RAM, disku). Při překročení maximální velikosti
 * je nejstarší hodnota automaticky odstraněna.
 * </p>
 *
 * <p>
 * Třída je thread-safe – všechny veřejné metody jsou synchronizované,
 * takže může být bezpečně používána z více vláken (monitory + UI).
 * </p>
 */
public class HistoryBuffer {

    /** Maximální počet uložených hodnot */
    private final int maxSize;

    /** Interní úložiště dat v pořadí od nejstarších po nejnovější */
    private final LinkedList<Integer> data = new LinkedList<>();

    /**
     * Vytvoří nový {@code HistoryBuffer} s danou maximální velikostí.
     *
     * @param maxSize maximální počet uložených hodnot
     */
    public HistoryBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Přidá novou hodnotu do bufferu.
     * <p>
     * Pokud je buffer plný, nejstarší hodnota je automaticky odstraněna.
     * </p>
     *
     * @param value hodnota k přidání
     */
    public synchronized void add(int value) {
        if (data.size() >= maxSize) {
            data.removeFirst(); // odstraní nejstarší hodnotu
        }
        data.add(value);
    }

    /**
     * Vrátí nemodifikovatelnou kopii aktuálních uložených hodnot.
     * <p>
     * Používá se zejména pro vykreslování grafů nebo práci v UI,
     * aby nebyla narušena interní struktura bufferu.
     * </p>
     *
     * @return seznam hodnot v pořadí od nejstarší po nejnovější
     */
    public synchronized List<Integer> getSnapshot() {
        return List.copyOf(data);
    }

    /**
     * Vrátí uložené hodnoty ve formě pole.
     * <p>
     * Metoda je vhodná pro testování, výpočty průměrů
     * nebo jiné numerické zpracování dat.
     * </p>
     *
     * @return pole obsahující uložené hodnoty
     */
    public synchronized int[] getValues() {
        int[] result = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    /**
     * Vrátí poslední přidanou hodnotu.
     *
     * @return poslední hodnota nebo {@code -1}, pokud je buffer prázdný
     */
    public synchronized int getCurrent() {
        if (data.isEmpty()) {
            return -1;
        }
        return data.getLast();
    }
}
