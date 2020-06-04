package parser.robinHood;

public class Query {
    String backend;
    long size;
    String url;
    byte cachable; // 1 for cachable, 0 for non-cachable

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
