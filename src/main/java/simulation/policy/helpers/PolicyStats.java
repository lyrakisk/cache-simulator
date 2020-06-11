package simulation.policy.helpers;

public class PolicyStats {
    private transient long operations = 0L;
    private transient long evictions = 0L;
    private transient long hits = 0L;

    public void recordOperation() {
        this.operations++;
    }

    public long getOperations() {
        return operations;
    }

    public void recordEviction() {
        evictions++;
    }

    public long getEvictions() {
        return evictions;
    }

    public long getHits() {
        return hits;
    }

    public void recordHit() {
        hits++;
    }
}
