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
     * Adds a certain amount of bytes/records from the cache.
     * @param size the amount of bytes/records which should be added
     */
    public void addToCache(long size) {
        usedCacheSpace += size;
    }

    /**
     * Removes a certain amount of bytes/records from the cache.
     * @param size the amount of bytes/records which should be removed
     */
    public void removeFromCache(long size) {
        usedCacheSpace -= size;
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
}
