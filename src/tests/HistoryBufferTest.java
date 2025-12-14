package tests;

import history.HistoryBuffer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryBufferTest {

    @Test
    void testAddAndRetrieve() {
        HistoryBuffer buffer = new HistoryBuffer(3);
        buffer.add(10);
        buffer.add(20);
        buffer.add(30);

        int[] values = buffer.getValues();
        assertArrayEquals(new int[]{10,20,30}, values);

        buffer.add(40);
        values = buffer.getValues();
        assertArrayEquals(new int[]{20,30,40}, values);
    }
}
