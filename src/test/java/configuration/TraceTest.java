package configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import parser.arc.ArcTraceParser;
import parser.snia.CambridgeTraceParser;
import parser.upenn.UpennTraceParser;


public class TraceTest {

    @Test
    public void testCambridgeTracePath() {
        assertEquals(
                "src/main/resources/traces/cambridge/msr-cambridge1-sample.csv",
                Trace.Cambridge.getFilePath());
    }

    @Test
    void testCambridgeTraceParser() {
        assertTrue(Trace.Cambridge.getParser() instanceof  CambridgeTraceParser);
    }

    @Test
    void testArcTracePath() {
        assertEquals("src/main/resources/traces/arc/OLTP.lis", Trace.ARC.getFilePath());
    }

    @Test
    void testArcTraceParser() {
        assertTrue(Trace.ARC.getParser() instanceof ArcTraceParser);
    }

    @Test
    void testUpennTracePath() {
        assertEquals("src/main/resources/traces/upenn/aligned.trace", Trace.UPENN.getFilePath());
    }

    @Test
    void testUpennTraceParser() {
        assertTrue(Trace.UPENN.getParser() instanceof UpennTraceParser);
    }
}
