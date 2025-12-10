package history;

import java.util.LinkedList;
import java.util.List;

public class HistoryBuffer {

    private final int maxSize;
    private final List<Integer> data = new LinkedList<>();

    public HistoryBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void add(int value) {
        if (data.size() >= maxSize) {
            data.remove(0);
        }
        data.add(value);
    }

    public synchronized List<Integer> getSnapshot() {
        return List.copyOf(data);
    }
}
