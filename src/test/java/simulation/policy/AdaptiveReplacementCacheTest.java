package simulation.policy;

import data.parser.Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.policy.helpers.Entry;
import simulation.policy.helpers.QueueNode;

public class AdaptiveReplacementCacheTest {

    private transient AdaptiveReplacementCache arc;


    @BeforeEach
    void init() {
        arc = new AdaptiveReplacementCache(1048576, true);
    }

    @Test
    void testTooBig() {
        Record record = new Record("1", 10000000);
        Assertions.assertFalse(arc.isPresentInCache(record));
        Assertions.assertFalse(arc.isPresentInCache(record));
    }

    @Test
    void testNotInCacheFirstTimeButInCacheSecondTime() {
        Record record = new Record("1", 256);
        Assertions.assertFalse(arc.isPresentInCache(record));

        Assertions.assertTrue(arc.isPresentInCache(record));

        Record notInCache = new Record("2", 256);
        Assertions.assertFalse(arc.isPresentInCache(notInCache));

    }

    @Test
    void testCacheSizeIsNumberOfFiles() {
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        Record first = new Record("1", 2048);
        Record second = new Record("2", 1024);
        Record third = new Record("3", 512);

        Assertions.assertFalse(arcRecords.isPresentInCache(first));
        Assertions.assertFalse(arcRecords.isPresentInCache(second));
        Assertions.assertEquals(0, arcRecords.getRemainingCache());

        Assertions.assertFalse(arcRecords.isPresentInCache(third));
        Assertions.assertFalse(arcRecords.isPresentInCache(first));
    }

    @Test
    void testOnHitB2() {
        Record first = new Record("1", 2048);
        Record second = new Record("2", 1024);
        Record third = new Record("3", 512);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        Assertions.assertTrue(arcRecords.isPresentInCache(second));
    }

    @Test
    void testT1CacheBiggerThanMaxSize() {
        Record first = new Record("1", 2048);
        Record second = new Record("2", 1024);
        Record third = new Record("3", 512);
        Record fourth = new Record("4", 256);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(third);
        Assertions.assertTrue(arcRecords.isPresentInCache(third));
        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
    }

    @Test
    void testL1andL2BiggerThanCache() {
        Record first = new Record("1", 2048);
        Record second = new Record("2", 1024);
        Record third = new Record("3", 512);
        Record fourth = new Record("4", 256);

        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);

        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
        Assertions.assertTrue(arcRecords.isPresentInCache(third));
    }

    @Test
    void testHitOnB1True() {
        Record first = new Record("1", 3);
        Record second = new Record("2", 5);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 3);
        Record fifth = new Record("1", 4);


        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(15, true);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fifth);


        Assertions.assertTrue(arcRecords.isPresentInCache(fifth));
        Assertions.assertTrue(arcRecords.isPresentInCache(third));
        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));

    }

    @Test
    void testHitOnB1False() {
        Record first = new Record("1", 3);
        Record second = new Record("2", 5);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 3);

        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(second);

        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(third);
        Assertions.assertTrue(arcRecords.isPresentInCache(third));
        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
    }

    @Test
    void testHitOnB2False2() {
        Record first = new Record("1", 3);
        Record second = new Record("2", 5);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 3);

        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);

        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fourth);

        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);

        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);

        Assertions.assertTrue(arcRecords.isPresentInCache(third));
        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
    }


    @Test
    void testHitOnB2() {
        Record first = new Record("1", 10);
        Record second = new Record("2", 5);
        Record first2 = new Record("1", 14);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 7);
        Record fifth = new Record("5", 7);
        Record sixth = new Record("6", 7);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(15, true);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(first2);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fifth);
        arcRecords.isPresentInCache(sixth);


        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
        Assertions.assertTrue(arcRecords.isPresentInCache(sixth));

    }

    @Test
    void testHitOnB2False() {
        Record first = new Record("1", 10);
        Record second = new Record("2", 5);
        Record first2 = new Record("1", 14);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 7);
        Record fifth = new Record("5", 7);
        Record sixth = new Record("6", 7);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(2, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(first2);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fifth);
        arcRecords.isPresentInCache(sixth);

        Assertions.assertTrue(arcRecords.isPresentInCache(sixth));
        Assertions.assertTrue(arcRecords.isPresentInCache(fifth));

    }

    @Test
    void testB2() {
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(3, false);

        Record first = new Record("1", 10);
        Record second = new Record("2", 5);
        Record third = new Record("3", 7);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);

        Record fourth = new Record("4", 7);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fourth);

        Record fifth = new Record("5", 7);
        arcRecords.isPresentInCache(fifth);
        arcRecords.isPresentInCache(fifth);

        Record six = new Record("6", 7);
        arcRecords.isPresentInCache(six);
        arcRecords.isPresentInCache(six);

        Record seven = new Record("7", 7);
        arcRecords.isPresentInCache(seven);

        Record eight = new Record("8", 7);
        arcRecords.isPresentInCache(eight);

        Record nine = new Record("9", 7);
        arcRecords.isPresentInCache(nine);

        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fifth);
        arcRecords.isPresentInCache(six);


        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
        Assertions.assertTrue(arcRecords.isPresentInCache(fifth));
        Assertions.assertTrue(arcRecords.isPresentInCache(six));
    }


    @Test
    void testL1EqualsMaxSize() {
        Record first = new Record("1", 10);
        Record second = new Record("2", 5);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 7);
        Record fifth = new Record("5", 7);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(3, false);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(third);

        arcRecords.isPresentInCache(second);

        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(fifth);

        Assertions.assertTrue(arcRecords.isPresentInCache(second));
        Assertions.assertTrue(arcRecords.isPresentInCache(fourth));
        Assertions.assertTrue(arcRecords.isPresentInCache(fifth));

    }

    @Test
    void testRemoveNodeCompletely() {
        Record first = new Record("1", 10);
        Record second = new Record("1", 15);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(15, true);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        Assertions.assertEquals(arcRecords.getRemainingCache(), 0);
        Record third = new Record("2", 15);
        Record fourth = new Record("1", 14);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        Assertions.assertEquals(arcRecords.getRemainingCache(), 1);
    }

    @Test
    void testHitB2isBytesTrue() {
        Record first = new Record("1", 9);
        Record second = new Record("2", 8);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(10, true);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(first);
        Assertions.assertTrue(arcRecords.isPresentInCache(first));

    }

    @Test
    void testNumberOfItems() {
        Record first = new Record("1", 9);
        Record second = new Record("2", 8);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(20, true);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(second);
        Assertions.assertEquals(2, arcRecords.numberOfItemsInCache());
    }


    @Test
    void testQueueNode() {
        Record testa = new Record("1", 2);
        Record testa2 = new Record("2", 2);
        QueueNode test = new QueueNode();
        test.setEntry(new Entry(testa.getId(), testa.getSize()));
        QueueNode test2 = new QueueNode();
        test2.setEntry(new Entry(testa2.getId(), testa2.getSize()));
        test.addToLast(test2);
        test.remove();
        test.remove();
        test.remove();
        Assertions.assertTrue(test.getKey().equals(""));

    }

    @Test
    void testMakeSpace() {
        Record zero = new Record("10", 14);
        Record first = new Record("1", 7);
        Record second = new Record("2", 7);
        Record third = new Record("3", 7);
        Record fourth = new Record("4", 7);
        AdaptiveReplacementCache arcRecords = new AdaptiveReplacementCache(15, true);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(zero);
        arcRecords.isPresentInCache(zero);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(second);
        arcRecords.isPresentInCache(zero);
        arcRecords.isPresentInCache(first);
        arcRecords.isPresentInCache(third);
        arcRecords.isPresentInCache(fourth);
        arcRecords.isPresentInCache(zero);
        arcRecords.isPresentInCache(second);
        Assertions.assertTrue(arcRecords.isPresentInCache(second));
    }


}
