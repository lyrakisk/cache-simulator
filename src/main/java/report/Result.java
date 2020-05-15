package report;


/**
 * This class will be used to store the results of the simulation.
 * It is supposed to be passed to an ObjectMapper, this is why the getters
 * are implemented.
 */
public class Result {
    private transient String policy;
    private transient double hitRatio;
    private transient int numberOfHits;


    public Result(String policy, double hitRatio, int numberOfHits) {
        this.policy = policy;
        this.hitRatio = hitRatio;
        this.numberOfHits = numberOfHits;
    }

    public String getPolicy() {
        return policy;
    }

    public double getHitRatio() {
        return hitRatio;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }
}
