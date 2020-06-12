package simulation.policy.helpers;

public class Entry {
    private transient String id;
    private transient long size;

    public Entry(String id, long size) {
        this.id = id;
        this.size = size;
    }

    /**
     * Getter for the identifier of the cache entry.
     * @return the identifier of the entry
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the size of the cache entry.
     * @return the size of the entry
     */
    public long getSize() {
        return size;
    }
}
