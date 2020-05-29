package policy.helpers;

public class PolicyStats {
    private transient long operations = 0L;

    public void recordOperation() {
        this.operations++;
    }

    public long getOperations() {
        return operations;
    }
}
