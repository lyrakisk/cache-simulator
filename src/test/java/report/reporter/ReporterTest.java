package report.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import report.Result;


public class ReporterTest {

    @Test
    void testGetResults() {
        Configuration c = mock(Configuration.class);
        JsonReporter r = new JsonReporter(new Result[0], c);
        assertEquals(0, r.getResults().length);
    }
}
