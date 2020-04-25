package policy;

import java.util.LinkedList;

import parser.Record;

/**
 * Class representing the LRU cache policy.
 */
public class LeastRecentlyUsed implements Policy {
    private transient LinkedList<Record> cache;
    private transient int sizeOfCache;
    private static int MAX_SIZE = 1048576;

    /**
     * Constructing a new cache using the LRU policy.
     */
    public LeastRecentlyUsed() {
        cache = new LinkedList<>();
        sizeOfCache = 0;
    }

    /**
     * Checks whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present in the cache, false otherwise
     */
    @Override
    public boolean isPresentInCache(Record record) {
        int occurrence = 0;
        for (int i = 0; i < cache.size(); ++ i) {
            Record inCache = cache.get(i);
            if (inCache.getId() == record.getId()) {
                cache.remove(inCache);
                sizeOfCache -= inCache.getSize();
                ++ occurrence;
                break;
            }
        }

        if (record.getSize() > LeastRecentlyUsed.MAX_SIZE) {
            -- occurrence;
            return occurrence > 0;
        }

        cache.addFirst(record);
        sizeOfCache += record.getSize();
        if (sizeOfCache > LeastRecentlyUsed.MAX_SIZE) {
            Record last = cache.removeLast();
            sizeOfCache -= last.getSize();
        }

        return occurrence > 0;
    }
}
