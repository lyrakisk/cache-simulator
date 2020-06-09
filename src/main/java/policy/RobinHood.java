package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import parser.Record;
import parser.robinHood.Query;
import parser.robinHood.Request;

// A number of variables are used in the faster-to-write for-loop construction
// and thus giving a UR-anomaly, which is not a bug.
// Also, for now we are calling system.exit when the record is not a request,
// which is going to be discussed later.
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
     * Constructor for the RobinHood policy.
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

    private int randomLatency(int rangeMin, int rangeMax) {
        return ThreadLocalRandom.current().nextInt(rangeMin, rangeMax + 1);
    }

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
            String backend = query.getBackend();
            Policy evictionPolicyForQuery = evictionPolicyPerBackend.get(backend);
            Record recordToCheck = new Record(query.getUrl(), query.getSize());

            if (!query.isCachable() || !evictionPolicyForQuery.isPresentInCache(recordToCheck)) {
                allInCache = false;
                if (!latencyPerBackendForRequest.containsKey(backend)) {
                    latencyPerBackendForRequest.put(backend, 0);
                }
                int latencyForBackend = latencyPerBackend.get(backend);
                int previousLatency = latencyPerBackendForRequest.get(backend);
                latencyPerBackendForRequest.put(backend, previousLatency + latencyForBackend);
            }
        }
        latencyPerRequest.put(request.getId(), latencyPerBackendForRequest);

        if (delta == 0) {
            delta = beginningDelta;
            updateCacheSizes();
            latencyPerRequest.clear();
        }

        return allInCache;
    }

    // I made it return the total number of items in the cache
    // for every backend present.
    @Override
    public int numberOfItemsInCache() {
        int nbrItems = 0;
        for (Policy policy: evictionPolicyPerBackend.values()) {
            nbrItems += policy.numberOfItemsInCache();
        }

        return nbrItems;
    }

    @Override
    public void deleteUntilCacheNotOverloaded() {
        for (Policy policy: evictionPolicyPerBackend.values()) {
            policy.deleteUntilCacheNotOverloaded();
        }
    }

    public void setDelta(int delta) {
        this.beginningDelta = delta;
        this.delta = delta;
    }

    public void setLatencyForBackend(String backend, int latency) {
        latencyPerBackend.put(backend, latency);
    }
}
