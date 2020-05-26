package policy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Record;

public class LeastFrequentlyUsedTest {
    private transient LeastFrequentlyUsed lfuBytes;
    private transient LeastFrequentlyUsed lfuRecords;

    @BeforeEach
    void init() {
        lfuBytes = new LeastFrequentlyUsed(1000, true);
        lfuRecords = new LeastFrequentlyUsed(2, false);
    }

    @Test
    void testMultipleSmallOneBig() {
        for (int i = 1; i <= 1000; ++ i) {
            Record toAdd = new Record(String.valueOf(i), 1);
            Assertions.assertFalse(lfuBytes.isPresentInCache(toAdd));
        }
        Assertions.assertFalse(lfuBytes.isPresentInCache(new Record(String.valueOf(1), 1001)));
        Assertions.assertTrue(lfuBytes.isPresentInCache(new Record(String.valueOf(2), 10)));
    }

    @Test
    void testOneBig() {
        Record toAdd = new Record("id", 1001);
        Assertions.assertFalse(lfuBytes.isPresentInCache(toAdd));
        Assertions.assertFalse(lfuBytes.isPresentInCache(toAdd));
    }

    @Test
    void testTwoRecordsAccessedTwoTimes() {
        Record first = new Record("1", 2);
        Record second = new Record("2", 2);
        Record againFirst = new Record("1", 1);
        Record againSecond = new Record("2", 1);

        Assertions.assertFalse(lfuBytes.isPresentInCache(first));
        Assertions.assertFalse(lfuBytes.isPresentInCache(second));
        Assertions.assertEquals(996, lfuBytes.getRemainingCache());
        Assertions.assertTrue(lfuBytes.isPresentInCache(againFirst));
        Assertions.assertTrue(lfuBytes.isPresentInCache(againSecond));
        Assertions.assertEquals(998, lfuBytes.getRemainingCache());
    }

    @Test
    void testOneRecordAccessedThreeTimes() {
        Record first = new Record("1", 2);
        Record second = new Record("2", 2);
        Assertions.assertFalse(lfuBytes.isPresentInCache(first));

        Record againFirst = new Record("1", 1);
        Assertions.assertTrue(lfuBytes.isPresentInCache(againFirst));

        Assertions.assertFalse(lfuBytes.isPresentInCache(second));
        Record firstThird = new Record("1", 5);
        Assertions.assertTrue(lfuBytes.isPresentInCache(firstThird));
        Assertions.assertEquals(993, lfuBytes.getRemainingCache());
    }

    @Test
    void testRemoveOnly1Record() {
        Record first = new Record("1", 500);
        Record second = new Record("2", 499);
        Record third = new Record("3", 20);

        Assertions.assertFalse(lfuBytes.isPresentInCache(first));
        Assertions.assertFalse(lfuBytes.isPresentInCache(second));
        Assertions.assertEquals(1, lfuBytes.getRemainingCache());

        Assertions.assertFalse(lfuBytes.isPresentInCache(third));
        Assertions.assertEquals(481, lfuBytes.getRemainingCache());
    }

    @Test
    void testRemove2RecordsAccessedDifferentAmountOfTimes() {
        Record first = new Record("1", 500);
        Record second = new Record("2", 5);
        Record secondAgain = new Record("2", 490);
        Record third = new Record("3", 700);

        Assertions.assertFalse(lfuBytes.isPresentInCache(first));
        Assertions.assertFalse(lfuBytes.isPresentInCache(second));
        Assertions.assertTrue(lfuBytes.isPresentInCache(secondAgain));
        Assertions.assertEquals(10, lfuBytes.getRemainingCache());
        Assertions.assertFalse(lfuBytes.isPresentInCache(third));
        Assertions.assertEquals(300, lfuBytes.getRemainingCache());
    }

    @Test
    void testCacheSizeIsNumberOfFiles() {
        Record first = new Record("1", 2);
        Record second = new Record("2", 1000);
        Record againSecond = new Record("2", 2000);
        Record third = new Record("3", 1000);
        Record secondThirdTime = new Record("2", 200);

        Assertions.assertFalse(lfuRecords.isPresentInCache(first));
        Assertions.assertFalse(lfuRecords.isPresentInCache(second));
        Assertions.assertTrue(lfuRecords.isPresentInCache(againSecond));
        Assertions.assertEquals(0, lfuRecords.getRemainingCache());

        Assertions.assertFalse(lfuRecords.isPresentInCache(third));
        Assertions.assertTrue(lfuRecords.isPresentInCache(secondThirdTime));
        Assertions.assertEquals(0, lfuRecords.getRemainingCache());
    }
}
