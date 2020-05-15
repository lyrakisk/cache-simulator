package parser.upenn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import parser.Record;


public class UpennTraceParserTest {

    @Test
    public void sampleFileCheckFirstLineTest() {
        UpennTraceParser parser = new UpennTraceParser();
        Record actual = parser
            .parse("src/test/resources/aligned-sample")
            .findFirst()
            .get();
        Record expected = new Record("bfede22c", 65536);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }


    @Test
    public void sampleFileCheckContentSizeTest() {
        UpennTraceParser parser = new UpennTraceParser();
        long size = parser
            .parse("src/test/resources/aligned-sample")
            .count();
        long expected = 9;
        assertEquals(expected, size);
    }

    @Test
    public void testExceptionCatch() {
        final PrintStream originalErr = System.err;
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        Stream<Record> records =
            (new UpennTraceParser()).parse("cookie");
        String expectedMessage = "ERROR: The file named cookie was not found!\n";
        List<Record> collectedRecords =
            records.sequential().collect(Collectors.toList());

        assertTrue(err.toString().contains(expectedMessage));
        assertTrue(collectedRecords.isEmpty());

        System.setErr(originalErr);
    }
}
