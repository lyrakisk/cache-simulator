package policy;

import java.util.LinkedList;

import parser.Record;

/**
 * Class representing the LRU cache policy.
 */
public class LeastRecentlyUsed extends Policy {
    private transient LinkedList<Record> cache;

    /**
     * Constructing a new cache using the LRU policy.
     * @param size the size of the cache
     * @param isBytes the cache size parameter
     */
    public LeastRecentlyUsed(int size, boolean isBytes) {
        super(size, isBytes);
        cache = new LinkedList<>();
    }

    /**
     * Checks whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present in the cache, false otherwise
     */
    @Override
    // DD and DU anomalies for the boolean existing which I don't seem
    // to know how to fix (or if I fix the code looks really ugly and not
    // well structured).
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean isPresentInCache(Record record) {
        this.checkIsBytes(record);
        boolean existing = false;
        for (int i = 0; i < cache.size(); ++ i) {
            Record inCache = cache.get(i);
            if (inCache.getId().equals(record.getId())) {
                cache.remove(inCache);
                this.removeFromCache(inCache);
                existing = true;
                break;
            }
        }

        if (record.getSize() > this.getCacheSize()) {
            return false;
        }

        cache.addFirst(record);
        this.addToCache(record);
        while (this.getRemainingCache() < 0) {
            Record last = cache.removeLast();
            this.removeFromCache(last);
        }

        return existing;
    }
}
