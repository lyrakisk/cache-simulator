package simulation.policy;

import data.parser.Record;
import data.parser.robinhood.Query;
import data.parser.robinhood.Request;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


// A number of variables are used in the faster-to-write for-loop construction
// and thus giving a UR-anomaly, which is not a bug.

/**
 * Class to implement the RobinHood cache policy.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.DoNotCallSystemExit"})
public class RobinHood extends Policy {

    /**
     * Class that represents the backend with the biggest latency in a request.
     */
    private class BiggestDelay implements Comparable<BiggestDelay> {
        private transient String requestId;
        private transient String backend;
        private transient int delay;

        /**
         * Constructor for the biggest latency.
         * @param requestId the request this latency belongs to
         * @param backend the backend with the highest latency
         * @param delay the latency
         */
        private BiggestDelay(String requestId, String backend, int delay) {
            this.requestId = requestId;
            this.backend = backend;
            this.delay = delay;
        }

        /**
         * Method used to sort the requests on biggest delay.
         * @param other another requst to compare with
         * @return the order those requests should appear in
         */
        @Override
        public int compareTo(BiggestDelay other) {
            return this.delay - other.delay;
        }
    }


    private transient Map<String, Map<String, Integer>> latencyPerRequest;
    private transient Map<String, Long> cachePerBackend;
    private transient Map<String, Integer> latencyPerBackend;
    private transient Map<String, Policy> evictionPolicyPerBackend;
    private transient int beginningDelta;
    private transient int delta;


    /**
     * Constructor for the RobinHood simulation.policy.
     * @param cacheSize the size of the cache
     * @param isBytes true if the cache is in number of bytes, false otherwise
     */
    public RobinHood(long cacheSize, boolean isBytes) {
        super(cacheSize, isBytes);
        latencyPerRequest = new HashMap<>();
        cachePerBackend = new HashMap<>();
        latencyPerBackend = new HashMap<>();
        evictionPolicyPerBackend = new HashMap<>();
        beginningDelta = 5000;
        delta = 5000;

        // There are 2 backends in the traces available - 39f00c48 and b4fbebd8.
        // todo: let the user pass the backend names in the constructor as an array or list.
        cachePerBackend.put("39f00c48", (long) (0.5 * (double) this.getCacheSize()));
        cachePerBackend.put("b4fbebd8", (long) (0.5 * (double) this.getCacheSize()));
        latencyPerBackend.put("39f00c48", randomLatency(100, 10000));
        latencyPerBackend.put("b4fbebd8", randomLatency(100, 10000));

        long sizeForEachBackend = cacheSize / 2;
        evictionPolicyPerBackend.put("39f00c48",
                new LeastRecentlyUsed(sizeForEachBackend, isBytes));
        evictionPolicyPerBackend.put("b4fbebd8",
                new LeastRecentlyUsed(sizeForEachBackend, isBytes));
    }

    /**
     * Used to create a randomized latency for each backend.
     * @param rangeMin minimum latency
     * @param rangeMax maximum latency
     * @return
     */
    private int randomLatency(int rangeMin, int rangeMax) {
        return ThreadLocalRandom.current().nextInt(rangeMin, rangeMax + 1);
    }

    /**
     * Updates the caches for every backend.
     */
    private void updateCacheSizes() {
        List<BiggestDelay> delaysOfRequests = new ArrayList<>();
        for (String request: latencyPerRequest.keySet()) {
            Map<String, Integer> latenciesPerBackend = latencyPerRequest.get(request);
            List<Map.Entry<String, Integer>> listLatencies =
                    new ArrayList<>(latenciesPerBackend.entrySet());
            listLatencies.sort(Map.Entry.comparingByValue());

            int size = listLatencies.size();
            Map.Entry<String, Integer> biggestLatency = listLatencies.get(size - 1);
            BiggestDelay forThisRequest = new
                    BiggestDelay(request, biggestLatency.getKey(), biggestLatency.getValue());

            delaysOfRequests.add(forThisRequest);
        }

        Collections.sort(delaysOfRequests);

        long cacheToSplit = 0;
        for (Map.Entry<String, Long> cacheSize: cachePerBackend.entrySet()) {
            long cacheToDelete = cacheSize.getValue() / 100;
            cacheToSplit += cacheSize.getValue() / 100;
            cachePerBackend.put(cacheSize.getKey(), cacheSize.getValue() - cacheToDelete);
        }

        int percentileLower = (int) ((double) delta / 100.0 * 98.5);
        int percentileUpper = (int) ((double) delta / 100.0 * 99.5);
        Map<String, Integer> requestBlockingCount = new HashMap<>();
        for (int i = percentileLower; i <= percentileUpper; ++ i) {
            BiggestDelay delay = delaysOfRequests.get(i);
            if (!requestBlockingCount.containsKey(delay.backend)) {
                requestBlockingCount.put(delay.backend, 0);
            }

            int lastBlockingCount = requestBlockingCount.get(delay.backend);
            requestBlockingCount.put(delay.backend, lastBlockingCount + 1);
        }

        for (Map.Entry<String, Integer> backend: requestBlockingCount.entrySet()) {
            long cacheSizeToAdd = cacheToSplit * backend.getValue()
                    / (percentileUpper - percentileLower + 1);
            long newCacheSize = cachePerBackend.get(backend.getKey()) + cacheSizeToAdd;
            cachePerBackend.put(backend.getKey(), newCacheSize);
            evictionPolicyPerBackend.get(backend.getKey()).setCacheSize(newCacheSize);
        }

        this.deleteUntilCacheNotOverloaded();
    }

    // I am not entirely sure what I am supposed to return here,
    // so I decided to return true only if every query of the request
    // is present in the cache.

    /**
     * Checks whether all of the queries in a request end up in a cache hit.
     * @param record the record to be checked
     * @return true if all of the queries are present in the cache, false otherwise
     */
    @Override
    public boolean isPresentInCache(Record record) {

        if (!(record instanceof Request)) {
            System.err.println("ERROR: Record is of wrong type!");
            System.exit(-1);
        }

        -- delta;
        Request request = (Request) record;
        this.checkIsBytes(request);
        Map<String, Integer> latencyPerBackendForRequest = new HashMap<>();
        ArrayList<Query> queries = request.getQueries();
        boolean allInCache = true;

        for (Query query: queries) {
            this.getStats().recordRequest();
            this.getStats().recordOperation();
            String backend = query.getBackend();
            Policy evictionPolicyForQuery = evictionPolicyPerBackend.get(backend);
            Record recordToCheck = new Record(query.getUrl(), query.getSize());

            if (!query.isCachable() || !evictionPolicyForQuery.isPresentInCache(recordToCheck)) {
                this.getStats().recordOperation();
                allInCache = false;
                if (!latencyPerBackendForRequest.containsKey(backend)) {
                    latencyPerBackendForRequest.put(backend, 0);
                }
                int latencyForBackend = latencyPerBackend.get(backend);
                int previousLatency = latencyPerBackendForRequest.get(backend);
                latencyPerBackendForRequest.put(backend, previousLatency + latencyForBackend);
            }

            // update RobinHood stats
            long hits = 0L;
            long evictions = 0L;
            for (Policy policy: evictionPolicyPerBackend.values()) {
                this.getStats().recordOperation();
                hits += policy.getStats().getHits();
                evictions += policy.getStats().getEvictions();
            }
            this.getStats().setHits(hits);
            this.getStats().setEvictions(evictions);
        }
        latencyPerRequest.put(request.getId(), latencyPerBackendForRequest);

        if (delta == 0) {
            this.getStats().recordOperation();
            delta = beginningDelta;
            updateCacheSizes();
            latencyPerRequest.clear();
        }

        return allInCache;
    }

    // I made it return the total number of items in the cache
    // for every backend present.

    /**
     * Returns the number of items in the cache.
     * @return the number of items in the cache
     */
    @Override
    public int numberOfItemsInCache() {
        int nbrItems = 0;
        for (Policy policy: evictionPolicyPerBackend.values()) {
            nbrItems += policy.numberOfItemsInCache();
        }

        return nbrItems;
    }

    /**
     * Deletes elements from the cache until all the caches for a backend are not overflowed.
     */
    @Override
    public void deleteUntilCacheNotOverloaded() {
        for (Policy policy: evictionPolicyPerBackend.values()) {
            this.getStats().recordOperation();
            policy.deleteUntilCacheNotOverloaded();
        }
    }

    /**
     * Sets the parameter delta which says after how many steps we update the cache.
     * @param delta the parameter to say after how many steps we update
     */
    public void setDelta(int delta) {
        this.getStats().recordOperation();
        this.beginningDelta = delta;
        this.delta = delta;
    }

    /**
     * Sets the latency of a backend to a specific one.
     * @param backend the backend whose latency is going to be updated
     * @param latency the latency to be added
     */
    public void setLatencyForBackend(String backend, int latency) {
        this.getStats().recordOperation();
        latencyPerBackend.put(backend, latency);
    }
}
