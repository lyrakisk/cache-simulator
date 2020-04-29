package policy;

import parser.Record;

/**
 * Abstract class which all different cache policies will extend from.
 */
public abstract class Policy {

    private transient int cacheSize;
    private transient int usedCacheSpace;

    /**
     * Constructor for the policy.
     * @param cacheSize the size of the cache in bytes
     */
    public Policy(int cacheSize) {
        this.cacheSize = cacheSize;
        this.usedCacheSpace = 0;
    }

    /**
     * Getter for the cache size.
     * @return
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Adds the record's size to the current size of the cache.
     * @param record the record whose size should be added
     */
    public void addToCache(Record record) {
        usedCacheSpace += record.getSize();
    }

    public void removeFromCache(Record record) {
        usedCacheSpace -= record.getSize();
    }

    /**
     * Returns the remaining cache space.
     * @return the remaining cache space
     */
    public int getRemainingCache() {
        return cacheSize - usedCacheSpace;
    }

    /**
     * Check whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present, false otherwise
     */
    public abstract boolean isPresentInCache(Record record);
}
