import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.ARCParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ARCTraceParserTest {

    @Test
    public void parseTest() {
        ARCParser parser = new ARCParser();
        Record actual = parser.
            parse("src/test/resources/OLTP-sample.lis")
            .findFirst()
            .get();
        Record expected = new Record("1", 512);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }
}
