package parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// The literal 'abc' is used 4 times in the file, but this is because of testing the equals method.
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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

    @Test
    void testEqualsNotRecord() {
        assertNotEquals(record, 2);
    }

    @Test
    void testEqualsSameObjects() {
        Record same = new Record("abc", 2048);
        assertEquals(record, same);
    }

    @Test
    void testSameIdDifferentSize() {
        Record different = new Record("abc", 1024);
        assertNotEquals(record, different);
    }

    @Test
    void testDifferentId() {
        Record different = new Record("a", 128);
        assertNotEquals(record, different);
    }
}
