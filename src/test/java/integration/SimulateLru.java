package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import parser.ConcreteParser;
import parser.Record;
import policy.LeastRecentlyUsed;
import policy.Policy;
import simulator.Simulator;


public class SimulateLru {
    private transient Policy policy;
    private transient ConcreteParser parser = new ConcreteParser();
    private transient Stream<Record> records = parser.parse("src/test/resources/simulationTest1.txt");


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
        policy = new LeastRecentlyUsed(1000000);
        Simulator simulator = new Simulator(policy, records);
        simulator.simulate();
        assertEquals(1, simulator.getNumberOfCacheHits());
        assertEquals(1.25f, simulator.getCacheHitsPercentage());
    }

}
