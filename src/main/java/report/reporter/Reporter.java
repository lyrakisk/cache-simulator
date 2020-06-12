package report.reporter;

import report.Result;

public abstract class Reporter {
    private transient Result[] results;

    public Reporter(Result[] results) {
        this.results = results;
    }

    public abstract void report();

    public Result[] getResults() {
        return results;
    }
}
