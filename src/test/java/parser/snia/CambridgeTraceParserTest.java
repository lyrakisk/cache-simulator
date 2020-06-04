package parser.snia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import parser.data.Record;


public class CambridgeTraceParserTest {

    @Test
    void parseTest() {
        CambridgeTraceParser parser = new CambridgeTraceParser();
        Record actual = parser
                .parse("src/test/resources/msr-cambridge1-sample.csv")
                .findFirst()
                .get();
        Record expected = new Record("9056014336", 2048);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void testExceptionCatch() {
        final PrintStream originalErr = System.err;
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        Stream<Record> records =
                (new CambridgeTraceParser()).parse("pita.gyros");
        String expectedMessage = "ERROR: The file named pita.gyros was not found!\n";
        List<Record> collectedRecords =
                records.sequential().collect(Collectors.toList());

        assertEquals(expectedMessage, err.toString());
        assertTrue(collectedRecords.isEmpty());

        System.setErr(originalErr);
    }
}
