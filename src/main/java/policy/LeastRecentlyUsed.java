package policy;

import java.util.LinkedHashMap;

import parser.Record;

/**
 * Class representing the LRU cache policy.
 */
public class LeastRecentlyUsed extends Policy {

    private transient LinkedHashMap<Record, Boolean> cache;

    /**
     * Constructing a new cache using the LRU policy.
     * @param size the size of the cache
     * @param isBytes the cache size parameter
     */
    public LeastRecentlyUsed(int size, boolean isBytes) {
        super(size, isBytes);
        cache = new LinkedHashMap<>(16, .75f, true);
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
        boolean existing = true;

        if (record.getSize() > this.getCacheSize()) {
            return false;
        }

        if (cache.get(record) == null) {
            cache.put(record, true);
            this.addToCache(record.getSize());
            existing = false;
        }

        while (this.getRemainingCache() < 0) {
            Record toRemove = cache.keySet().iterator().next();
            cache.remove(toRemove);
            this.removeFromCache(toRemove.getSize());
        }

        return existing;
    }
}
