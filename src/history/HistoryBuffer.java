package history;

import java.util.LinkedList;
import java.util.List;

public class HistoryBuffer {

    private final int maxSize;
    private final LinkedList<Integer> data = new LinkedList<>();

    public HistoryBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    // Přidání nové hodnoty do bufferu
    public synchronized void add(int value) {
        if (data.size() >= maxSize) {
            data.removeFirst(); // odstraň nejstarší hodnotu
        }
        data.add(value);
    }

    // Vrací bezpečnou kopii aktuálních hodnot (pro UI, grafy)
    public synchronized List<Integer> getSnapshot() {
        return List.copyOf(data);
    }

    // Nová metoda pro testování nebo výpočty
    public synchronized int[] getValues() {
        int[] result = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    // Vrací poslední přidanou hodnotu
    public synchronized int getCurrent() {
        if (data.isEmpty()) return -1;
        return data.getLast();
    }
}
