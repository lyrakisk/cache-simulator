package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RecordTest {
    private transient Record record = new Record("abc", 2048);

    @Test
    void testGetId() {
        assertEquals("abc", record.getId());
    }

    @Test
    void testGetSize() {
        assertEquals(2048, record.getSize());
    }
}
