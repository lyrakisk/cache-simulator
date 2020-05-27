package report;


/**
 * This class will be used to store the results of the simulation.
 * It is supposed to be passed to an ObjectMapper, this is why the getters
 * are implemented.
 */
public class Result {
    private transient String policy;
    private transient double hitRate;
    private transient long numberOfHits;
    private transient long numberOfRequests;
    private transient long evictions;

    /**
     * Constructor.
     * @param policy the policy that was simulated.
     * @param hitRate (number of hits) / (total number of requests)
     * @param numberOfHits total number of items found in cache
     */
    public Result(String policy, double hitRate, int numberOfHits, int numberOfRequests) {
        this.policy = policy;
        this.hitRate = hitRate;
        this.numberOfHits = numberOfHits;
        this.numberOfRequests = numberOfRequests;
    }

    public String getPolicy() {
        return policy;
    }

    public double getHitRate() {
        return hitRate;
    }

    public long getNumberOfHits() {
        return numberOfHits;
    }

    public long getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(long numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public void setHitRate(double hitRate) {
        this.hitRate = hitRate;
    }

    public void setNumberOfHits(long numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public long getEvictions() {
        return evictions;
    }

    public void setEvictions(long evictions) {
        this.evictions = evictions;
    }
}
