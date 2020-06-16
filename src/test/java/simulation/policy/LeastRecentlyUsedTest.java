package simulation.policy;

import configuration.Configuration;
import data.parser.Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class LeastRecentlyUsedTest {
    private transient LeastRecentlyUsed lru;

    @BeforeEach
    void init() {
        lru = new LeastRecentlyUsed(1048576, true);
    }

    @Test
    void testTooBig() {
        Record record = new Record("1", 10000000);
        Assertions.assertFalse(lru.isPresentInCache(record));
        Assertions.assertFalse(lru.isPresentInCache(record));
    }

    @Test
    void testNotInCacheFirstTimeButInCacheSecondTime() {
        Record record = new Record("1", 256);
        Assertions.assertFalse(lru.isPresentInCache(record));

        Record updatedRecord = new Record("1", 512);
        Assertions.assertFalse(lru.isPresentInCache(updatedRecord));

        Record notInCache = new Record("2", 256);
        Assertions.assertFalse(lru.isPresentInCache(notInCache));
    }

    @Test
    void testInCacheSameElement() {
        Record first = new Record("1", 256);
        Record againFirst = new Record("1", 256);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertTrue(lru.isPresentInCache(againFirst));
    }

    @Test
    void testRemoveElementsFromTheCache() {
        Record first = new Record("1", 1000000);
        Record second = new Record("2", 48000);
        Record evictingBoth = new Record("3", 1048000);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
        Assertions.assertFalse(lru.isPresentInCache(evictingBoth));
        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
    }

    @Test
    void removeDueToLowFrequency() {
        lru = new LeastRecentlyUsed(2, false);
        Record first = new Record("1", 215);
        Record againFirst = new Record("1", 125);
        Record thirdTimeFirst = new Record("1", 222);
        Record second = new Record("2", 216);
        Record againSecond = new Record("2", 15);
        Record third = new Record("3", 212);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
        Assertions.assertTrue(lru.isPresentInCache(againFirst));
        Assertions.assertFalse(lru.isPresentInCache(third));
        Assertions.assertTrue(lru.isPresentInCache(thirdTimeFirst));
        Assertions.assertFalse(lru.isPresentInCache(againSecond));
    }

    @Test
    void testRemovedDueToOverflow() {
        Record first = new Record("1", 600);
        Record second = new Record("2", 1048000);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
        Assertions.assertFalse(lru.isPresentInCache(first));
    }

    @Test
    void testCacheSizeIsNumberOfFiles() {
        Configuration configuration = mock(Configuration.class);
        Mockito.when(configuration.getCacheSize()).thenReturn(2L);
        Mockito.when(configuration.isSizeInBytes()).thenReturn(false);
//        configuration.setCacheSize(2);
//        configuration.setSizeInBytes(false);
        LeastRecentlyUsed lruRecords = new LeastRecentlyUsed(configuration);
        Record first = new Record("1", 2048);
        Record second = new Record("2", 1024);
        Record third = new Record("3", 512);

        Assertions.assertFalse(lruRecords.isPresentInCache(first));
        Assertions.assertFalse(lruRecords.isPresentInCache(second));
        Assertions.assertEquals(0, lruRecords.getRemainingCache());

        Assertions.assertFalse(lruRecords.isPresentInCache(third));
        Assertions.assertFalse(lruRecords.isPresentInCache(first));
    }

    @Test
    void testSameRecordsButDifferentSizes() {
        LeastRecentlyUsed leastRecentlyUsed = new LeastRecentlyUsed(2048, true);
        Record first = new Record("1", 512);
        Record againFirstShouldDeletePrevious = new Record("1", 256);
        Record check = new Record("1", 512);
        Assertions.assertFalse(leastRecentlyUsed.isPresentInCache(first));
        Assertions.assertFalse(leastRecentlyUsed.isPresentInCache(againFirstShouldDeletePrevious));
        Assertions.assertFalse(leastRecentlyUsed.isPresentInCache(check));
    }

    @Test
    void testSameIdButSecondWithHugeSize() {
        Record first = new Record("1", 256);
        Record firstBigSize = new Record("1", 2000000);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(firstBigSize));
        Assertions.assertFalse(lru.isPresentInCache(first));
    }

    @Test
    void testDeleteUntilCacheNotOverloaded() {
        Record first = new Record("1", 1000);
        Record second = new Record("2", 2000);

        Assertions.assertFalse(lru.isPresentInCache(first));
        Assertions.assertFalse(lru.isPresentInCache(second));
        Assertions.assertTrue(lru.isPresentInCache(first));
        Assertions.assertTrue(lru.isPresentInCache(second));

        lru.setCacheSize(2500);
        lru.deleteUntilCacheNotOverloaded();

        Assertions.assertTrue(lru.isPresentInCache(second));
    }
}
