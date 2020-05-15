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
    public void testCambridgeTraceParser() {
        assertTrue(Trace.Cambridge.getParser() instanceof  CambridgeTraceParser);
    }

    @Test
    public void testArcTracePath() {
        assertEquals("src/main/resources/traces/arc/OLTP.lis", Trace.ARC.getFilePath());
    }

    @Test
    public void testArcTraceParser() {
        assertTrue(Trace.ARC.getParser() instanceof ArcTraceParser);
    }

    @Test
    public void testUpennTracePath() {
        assertEquals("src/main/resources/traces/upenn/aligned.trace", Trace.UPENN.getFilePath());
    }

    @Test
    public void testUpennTraceParser() {
        assertTrue(Trace.UPENN.getParser() instanceof UpennTraceParser);
    }
}
