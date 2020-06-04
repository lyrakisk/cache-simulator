package policy;

import parser.Record;

/**
 * Abstract class which all different cache policies will extend from.
 */
public abstract class Policy {

    private transient long cacheSize;
    private transient long usedCacheSpace;
    private transient boolean isBytes;

    /**
     * Constructor for the policy.
     * @param cacheSize the size of the cache.
     * @param isBytes if this is true, the cache size is in Bytes, otherwise
     *                it represents the maximum number of items the cache can store.
     */
    public Policy(long cacheSize, boolean isBytes) {
        this.cacheSize = cacheSize;
        this.usedCacheSpace = 0;
        this.isBytes = isBytes;
    }

    /**
     * Getter for the cache size.
     * @return
     */
    public long getCacheSize() {
        return cacheSize;
    }

    /**
     * Updates the cache size.
     * @param size the size with which the cache should be updated
     * @param isAdding if true it adds the size to the cache, otherwise it subtracts it
     */
    public void updateCacheSize(long size, boolean isAdding) {
        if (isAdding) {
            usedCacheSpace += size;
        } else {
            usedCacheSpace -= size;
        }
    }

    /**
     * Returns the remaining cache space.
     * @return the remaining cache space
     */
    public long getRemainingCache() {
        return cacheSize - usedCacheSpace;
    }

    /**
     * If the cache should take only number of records into account, set the size to 1.
     * @param record the record to be checked
     */
    public void checkIsBytes(Record record) {
        if (!isBytes) {
            record.setSize(1);
        }
    }

    /**
     * Check whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present, false otherwise
     */
    public abstract boolean isPresentInCache(Record record);

    public abstract int numberOfItemsInCache();
}
