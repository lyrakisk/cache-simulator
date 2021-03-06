import static org.junit.jupiter.api.Assertions.assertEquals;

import data.parser.Record;
import data.parser.snia.CambridgeTraceParser;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import report.Result;
import simulation.policy.LeastFrequentlyUsed;
import simulation.policy.Policy;
import simulation.simulator.Simulator;


public class SimulateLfuTest {
    private transient Policy policy;
    private transient CambridgeTraceParser parser = new CambridgeTraceParser();
    private transient Stream<Record> records = parser
            .parse("src/test/resources/msr-cambridge1-sample.csv");
    private transient ArrayList<Policy> policies = new ArrayList<Policy>();

    @Test
    void testZeroCacheHits() {
        policies.add(new LeastFrequentlyUsed(0, true));

        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(0, results[0].getNumberOfHits());
        assertEquals(0, results[0].getHitRate());
    }

    @Test
    void testOneCacheHit() {
        policies.add(new LeastFrequentlyUsed(10000, true));
        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(1, results[0].getNumberOfHits());
        assertEquals(10f, 0, results[0].getHitRate());
    }

    @Test
    void testZeroEvictions() {
        policies.add(new LeastFrequentlyUsed(10, false));
        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(0, results[0].getEvictions());
    }

    @Test
    void testOneEviction() {
        policies.add(new LeastFrequentlyUsed(8, false));
        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(1, results[0].getEvictions());
    }

    @Test
    void testMultipleEvictions() {
        policies.add(new LeastFrequentlyUsed(1, false));
        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(9, results[0].getEvictions());
    }


    @AfterEach
    void clear() {
        policies.clear();
    }
}
