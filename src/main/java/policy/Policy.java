package policy;

import parser.Record;

/**
 * Abstract class which all different cache policies will extend from.
 */
public abstract class Policy {

    private transient int cacheSize;
    private transient int usedCacheSpace;
    private transient boolean isBytes;

    /**
     * Constructor for the policy.
     * @param cacheSize the size of the cache in bytes
     * @param isBytes the cache size parameter
     */
    public Policy(int cacheSize, boolean isBytes) {
        this.cacheSize = cacheSize;
        this.usedCacheSpace = 0;
        this.isBytes = isBytes;
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
