package simulation.policy;

import data.parser.robinhood.Query;
import data.parser.robinhood.Request;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


// It gives a DU-anomaly in the test starting on line 65,
// but there is no way to write the query more elegant.
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class RobinHoodTest {

    private transient RobinHood policy;
    private transient Request request1;
    private transient Request request2;
    private transient String backend;

    @BeforeEach
    void init() {
        policy = new RobinHood(100, true);
        policy.setDelta(2);
        policy.setLatencyForBackend("39f00c48", 10);
        policy.setLatencyForBackend("b4fbebd8", 5);
        backend = "39f00c48";
        request1 = new Request("request1", new ArrayList<>());
        request2 = new Request("request2", new ArrayList<>());
    }

    @Test
    void testTwoRequestsNoCacheHits() {
        for (int i = 1; i <= 3; ++ i) {
            Query query = new Query(backend, 50, "url" + i, true);
            request1.addQuery(query);
            request2.addQuery(query);
        }

        Assertions.assertFalse(policy.isPresentInCache(request1));
        Assertions.assertEquals(1, policy.numberOfItemsInCache());
        Assertions.assertFalse(policy.isPresentInCache(request2));
        Assertions.assertEquals(1, policy.numberOfItemsInCache());
    }

    @Test
    void testTOneRequestWithCacheHit() {
        Query query = new Query(backend, 50, "URL", true);
        request1.addQuery(query);
        request1.addQuery(query);

        Assertions.assertFalse(policy.isPresentInCache(request1));
    }

    @Test
    void testOneRequestWithNonCachableQuery() {
        Query query = new Query(backend, 50, "url", false);
        request1.addQuery(query);

        Assertions.assertFalse(policy.isPresentInCache(request1));
    }

    @Test
    void testFiveThousandRequestsWithTheSameSlowestBackend() {
        Query query = new Query(backend, 100, "url", true);
        policy.setDelta(5000);

        for (int i = 1; i <= 5000; ++ i) {
            Request newRequest = new Request("newRequest" + i, new ArrayList<>());
            newRequest.addQuery(query);
            Assertions.assertFalse(policy.isPresentInCache(newRequest));
        }
    }
}
