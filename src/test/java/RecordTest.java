import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import parser.Record;

public class RecordTest {
    private transient Record record = new Record("abc", 2048);

    @Test
    public void testGetId() {
        assertEquals("abc", record.getId());
    }

    @Test
    public void testGetSize() {
        assertEquals(2048, record.getSize());
    }
}
