import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Record;
import policy.LeastRecentlyUsed;
import policy.Policy;
import simulator.Simulator;

public class SimulatorTest {
    private transient Policy policy;
    private transient Simulator simulator;
    private transient List<Record> recordList = new ArrayList<Record>();

    @BeforeEach
    public void setUp() {
        policy = mock(LeastRecentlyUsed.class);
        simulator = new Simulator(policy, recordList.stream());
    }

    @Test
    public void testSetPolicy() {
        simulator.setPolicy(new LeastRecentlyUsed(20000));
        assertEquals("LeastRecentlyUsed", simulator.getPolicyName());
    }

    @Test
    public void testGetNumberOfCacheHits() {
        assertEquals(0, simulator.getNumberOfCacheHits());
    }

    @Test
    public void testGetCacheHitsPercentage() {
        assertEquals(0, simulator.getCacheHitsPercentage());
    }

}
