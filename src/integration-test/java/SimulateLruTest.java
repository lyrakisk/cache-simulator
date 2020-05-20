import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.PreferenceChangeListener;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.CambridgeTraceParser;
import policy.LeastRecentlyUsed;
import policy.Policy;
import report.Result;
import simulator.Simulator;


public class SimulateLruTest {
    private transient Policy policy;
    private transient CambridgeTraceParser parser = new CambridgeTraceParser();
    private transient Stream<Record> records = parser
            .parse("src/test/resources/msr-cambridge1-sample.csv");
    private transient ArrayList<Policy> policies = new ArrayList<Policy>();

    @Test
    public void testZeroCacheHits() {
        policies.add(new LeastRecentlyUsed(0, true));

        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(0, results[0].getNumberOfHits());
        assertEquals(0, results[0].getHitRatio());
    }

    @Test
    public void testOneCacheHit() {
        policies.add(new LeastRecentlyUsed(10000, true));
        Simulator simulator = new Simulator(policies, records);
        Result[] results = simulator.simulate();
        assertEquals(1, results[0].getNumberOfHits());
        assertEquals(10f, 0, results[0].getHitRatio());
    }


    @AfterEach
    public void clear() {
        policies.clear();
    }

}
