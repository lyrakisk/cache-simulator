import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.CambridgeTraceParser;
import policy.LeastRecentlyUsed;
import policy.Policy;
import simulator.Simulator;


public class SimulateLruTest {
    private transient Policy policy;
    private transient CambridgeTraceParser parser = new CambridgeTraceParser();
    private transient Stream<Record> records = parser
            .parse("src/test/resources/msr-cambridge1-sample.csv");


    @Test
    public void testZeroCacheHits() {
        policy = new LeastRecentlyUsed(0);
        Simulator simulator = new Simulator(policy, records);
        simulator.simulate();
        assertEquals(0, simulator.getNumberOfCacheHits());
        assertEquals(0, simulator.getCacheHitsPercentage());
    }

    @Test
    public void testOneCacheHit() {
        policy = new LeastRecentlyUsed(10000);
        Simulator simulator = new Simulator(policy, records);
        simulator.simulate();
        assertEquals(1, simulator.getNumberOfCacheHits());
        assertEquals(10f, simulator.getCacheHitsPercentage());
    }

}
