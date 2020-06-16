package configuration;

public class Configuration {

    private transient String[] policies;
    private transient long cacheSize;
    private transient boolean sizeInBytes;
    private transient String trace;
    private transient boolean printResultsToConsole;
    private transient String resultsFilePath;

    public String[] getPolicies() {
        return policies;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public boolean isSizeInBytes() {
        return sizeInBytes;
    }

    public String getTrace() {
        return trace;
    }

    public boolean isPrintResultsToConsole() {
        return printResultsToConsole;
    }

    public String getResultsFilePath() {
        return resultsFilePath;
    }

}
