package parser.robinHood;

public class Query {
    private transient String backend;
    private transient long size;
    private transient String url;
    private transient byte cachable; // 1 for cachable, 0 for non-cachable

    /**
     * Constructor.
     * @param backend name of the backend that received the query
     * @param size size of the requested object
     * @param url url of the object (object identifier)
     * @param cachable  (1 if cachable, 0 if not cachable)
     */
    public Query(String backend, long size, String url, byte cachable) {
        this.backend = backend;
        this.size = size;
        this.url = url;
        this.cachable = cachable;
    }

    public String getBackend() {
        return backend;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCachable() {
        return cachable == 1;
    }
}
