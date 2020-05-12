package configuration;

import policy.Policy;

public class Configuration {

    private String[] policies;
    private int cacheSize;
    private boolean sizeInBytes;

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
