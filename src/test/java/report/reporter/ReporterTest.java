package report.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import report.Result;


public class ReporterTest {

    @Test
    void testGetResults() {
        JsonReporter r = new JsonReporter(new Result[0]);
        assertEquals(0, r.getResults().length);
    }
}
