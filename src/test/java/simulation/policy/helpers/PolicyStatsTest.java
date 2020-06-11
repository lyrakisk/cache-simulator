package simulation.policy.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class PolicyStatsTest {
    private transient PolicyStats stats;

    @Test
    void testRecordOperation() {
        stats = new PolicyStats();
        assertEquals(0, stats.getOperations());
        stats.recordOperation();
        assertEquals(1, stats.getOperations());
    }
}
