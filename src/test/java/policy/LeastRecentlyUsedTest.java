package policy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Record;

public class LeastRecentlyUsedTest {
    private transient LeastRecentlyUsed lru;

    @BeforeEach
    void init() {
        lru = new LeastRecentlyUsed(1048576, true);
    }

    @Test
    void testTooBig() {
        Record record = new Record(1, 10000000);
        Assertions.assertFalse(lru.isPresentInCache(record));
        Assertions.assertFalse(lru.isPresentInCache(record));
    }

    @Test
    void testNotInCacheFirstTimeButInCacheSecondTime() {
        Record record = new Record(1, 256);
        Assertions.assertFalse(lru.isPresentInCache(record));

        Record updatedRecord = new Record(1, 512);
        Assertions.assertTrue(lru.isPresentInCache(updatedRecord));

        Record notInCache = new Record(2, 256);
        Assertions.assertFalse(lru.isPresentInCache(notInCache));
    }

    @Test
    void testRemovedDueToOverflow() {
        Record first = new Record(1, 600);
        Record second = new Record(2, 1048000);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
        Assertions.assertFalse(lru.isPresentInCache(first));
    }
}
