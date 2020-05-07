import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.ArcParser;


public class ArcTraceParserTest {

    @Test
    public void parseFirstTest() {
        ArcParser parser = new ArcParser();
        Record actual = parser
            .parse("src/test/resources/OLTP-sample.lis")
            .findFirst()
            .get();
        Record expected = new Record("1", 512);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void parseContentSizeTest() {
        ArcParser parser = new ArcParser();
        int size = parser
            .parse("src/test/resources/OLTP-sample.lis")
            .toArray().length;

        int actualSize = 3;
        assertEquals(actualSize, size);

    }
}


