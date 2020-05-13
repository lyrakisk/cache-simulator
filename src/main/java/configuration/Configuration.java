package configuration;

import policy.Policy;

public class Configuration {

    private transient String[] policies;
    private transient int cacheSize;
    private transient boolean sizeInBytes;

    public String[] getPolicies() {
        return policies;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public boolean isSizeInBytes() {
        return sizeInBytes;
    }
}
