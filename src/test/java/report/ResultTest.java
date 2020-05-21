package report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ResultTest {
    static Result result;

    @BeforeAll
    static void setUp() {
        result = new Result("LeastRecentlyUsed", 50, 2134, 20000);
    }

    @Test
    public void testGetPolicy() {
        assertEquals("LeastRecentlyUsed", result.getPolicy());
    }

    @Test
    public void testGetHitRatio() {
        assertEquals(50, result.getHitRate());
    }

    @Test
    public void testGetNumberOfHits() {
        assertEquals(2134, result.getNumberOfHits());
    }

    @Test
    public void testSetGetEvictions() {
        result.setEvictions(10);
        assertEquals(10, result.getEvictions());
    }
}
