package report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ResultTest {
    static Result result;

    @BeforeAll
    static void setUp() {
        result = new Result("LeastRecentlyUsed", 50, 2134);
    }

    @Test
    void testGetPolicy() {
        assertEquals("LeastRecentlyUsed", result.getPolicy());
    }

    @Test
    void testGetHitRatio() {
        assertEquals(50, result.getHitRate());
    }

    @Test
    void testGetNumberOfHits() {
        assertEquals(2134, result.getNumberOfHits());
    }

    @Test
    void testSetGetEvictions() {
        result.setEvictions(10);
        assertEquals(10, result.getEvictions());
    }

    @Test
    void testGetAverageProcessTimePerRequest() {
        result.setAverageProcessTimePerRequest(0.02);
        assertEquals(0.02, result.getAverageProcessTimePerRequest());
    }

}
