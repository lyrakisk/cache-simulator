package policy.helpers;

public class PolicyStats {
    private transient long operations = 0L;
    private transient long evictions = 0L;

    public void recordOperation() {
        this.operations++;
    }

    public long getOperations() {
        return operations;
    }

    public void recordEviction() {
        evictions++;
    }

    public void setEvictions(long evictions) {
        this.evictions = evictions;
    }

    public long getEvictions() {
        return evictions;
    }
}
