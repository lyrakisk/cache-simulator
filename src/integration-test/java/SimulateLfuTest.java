import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.CambridgeTraceParser;
import policy.LeastFrequentlyUsed;
import policy.Policy;
import simulator.Simulator;


public class SimulateLfuTest {
    private transient Policy policy;
    private transient CambridgeTraceParser parser = new CambridgeTraceParser();
    private transient Stream<Record> records = parser
            .parse("src/main/resources/msr-cambridge1-sample.csv");


    @Test
    public void testZeroCacheHits() {
        policy = new LeastFrequentlyUsed(0, true);
        Simulator simulator = new Simulator(policy, records);
        simulator.simulate();
        assertEquals(0, simulator.getNumberOfCacheHits());
        assertEquals(0, simulator.getCacheHitsPercentage());
    }

    @Test
    public void testOneCacheHit() {
        policy = new LeastFrequentlyUsed(10000, true);
        Simulator simulator = new Simulator(policy, records);
        simulator.simulate();
        assertEquals(1, simulator.getNumberOfCacheHits());
        assertEquals(10f, simulator.getCacheHitsPercentage());
    }
}
