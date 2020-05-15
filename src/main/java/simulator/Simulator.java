package simulator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import parser.Record;
import policy.Policy;
import report.Result;


/**
 * This class is responsible to run the cache simulation.
 * It uses a policy and a stream of records and keeps track of
 * various metrics (cache hits etc.) about the performance of
 * the chosen policy.
 */
public class Simulator {
    private transient Policy policy;
    private transient int numberOfCacheHits;
    private transient double cacheHitsPercentage;
    private transient Stream<Record> records;

    public Simulator(Policy policy, Stream records) {
        this.policy = policy;
        this.records = records;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getPolicyName() {
        return this.policy.getClass().getSimpleName();
    }

    public int getNumberOfCacheHits() {
        return numberOfCacheHits;
    }

    public double getCacheHitsPercentage() {
        return cacheHitsPercentage;
    }

    /**
     * Performs the simulation and updates the fields
     * that store the performance metrics.
     */
    public Result simulate() {
        numberOfCacheHits = 0;

        List<Record> collectedRecords =  records.sequential().collect(Collectors.toList());
        float total = collectedRecords.size();

        for (int i = 0; i < total; i++) {
            if (policy.isPresentInCache(collectedRecords.get(i))) {
                numberOfCacheHits++;
            }
        }

        cacheHitsPercentage = ((int) (numberOfCacheHits / total * 10000)) / 100.0;

        return new Result(this.getPolicyName(), cacheHitsPercentage, numberOfCacheHits);
    }

}
