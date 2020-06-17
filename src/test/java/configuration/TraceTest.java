package configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.parser.arc.ArcTraceParser;
import data.parser.snia.CambridgeTraceParser;
import data.parser.upenn.UpennTraceParser;
import org.junit.jupiter.api.Test;


public class TraceTest {

    @Test
    public void testCambridgeTracePath() {
        assertEquals(
                "resources/traces/cambridge/msr-cambridge1-sample.csv",
                Trace.Cambridge.getFilePath());
    }

    @Test
    void testCambridgeTraceParser() {
        assertTrue(Trace.Cambridge.getParser() instanceof  CambridgeTraceParser);
    }

    @Test
    void testArcTracePath() {
        assertEquals("resources/traces/arc/OLTP.lis", Trace.ARC.getFilePath());
    }

    @Test
    void testArcTraceParser() {
        assertTrue(Trace.ARC.getParser() instanceof ArcTraceParser);
    }

    @Test
    void testUpennTracePath() {
        assertEquals("resources/traces/upenn/aligned.trace", Trace.UPENN.getFilePath());
    }

    @Test
    void testUpennTraceParser() {
        assertTrue(Trace.UPENN.getParser() instanceof UpennTraceParser);
    }
}
