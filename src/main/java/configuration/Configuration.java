package configuration;

public class Configuration {

    private transient String[] policies;
    private transient int cacheSize;
    private transient boolean sizeInBytes;
    private transient String trace;

    public String[] getPolicies() {
        return policies;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public boolean isSizeInBytes() {
        return sizeInBytes;
    }

    public String getTrace() {
        return trace;
    }
}
