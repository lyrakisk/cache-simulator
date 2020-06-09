package simulator;

import java.util.ArrayList;
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
    private transient ArrayList<Policy> policies;
    private transient Stream<Record> records;

    public Simulator(ArrayList<Policy> policies, Stream records) {
        this.policies = policies;
        this.records = records;
    }


    /**
     * Performs the simulation and updates the results' fields
     * that store the performance metrics.
     * The DataflowAnomalyAnalysis is suppressed here, because it's raised
     * for the wrong reason. PMD thinks that the result variable inside the
     * for loop is not initialized.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Result[] simulate() {
        Result[] results = new Result[policies.size()];

        // initialize results
        for (int i = 0; i < results.length; i++) {
            results[i] = new Result(policies.get(i).getClass().getSimpleName(), 0, 0, 0);
        }

        records.forEachOrdered(record -> processRecord(record, results));

        // get the number of operations from each policy
        for (int i = 0; i < policies.size(); i++) {
            results[i].setNumberOfOperations(policies.get(i).getStats().getOperations());
            results[i].setEvictions(policies.get(i).getStats().getEvictions());
            results[i].setNumberOfHits(policies.get(i).getStats().getHits());
        }

        // Calculate the hit ratio for each policy
        // Calculate the average process time per request for each policy
        for (Result result: results) {
            result.setHitRate(
                    ((int) (result.getNumberOfHits()
                    / ((float) result.getNumberOfRequests())
                    * 10000))
                    / 100.0);
            result.setAverageProcessTimePerRequest(
                    ((long) (result.getTimeToProcessAllRequests()
                    / ((double) result.getNumberOfRequests())
                    * 10000))
                    / 10000.0);
        }

        return results;
    }

    /**
     * This function processes a record and updates the results of the simulation.
     * For each record it checks if the requested item is in the cache of each simulated policy
     * and it updates the results.
     * DataflowAnomalyAnalysis warning is supressed in this method. It's probably caused by a
     * bug in PMD. PMD thinks that the variable items, is undefined (which is not the case).
     * @param record record to be processed.
     * @param results current results of the simulation
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void processRecord(Record record, Result[] results) {
        for (int i = 0; i < results.length; i++) {
            Policy policy = policies.get(i);
            results[i].setNumberOfRequests(results[i].getNumberOfRequests() + 1);


            long startTime = System.nanoTime();

            // Send the record to the policy to process it.
            policy.isPresentInCache(record);


            long endTime = System.nanoTime();

            // convert time to milliseconds
            double timeToProcessCurrentRequest = (endTime - startTime) / 1000000.0;
            results[i]
                    .setTimeToProcessAllRequests(
                            results[i].getTimeToProcessAllRequests() + timeToProcessCurrentRequest);
        }
    }

}
