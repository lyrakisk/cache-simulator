package data.parser.umass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import data.parser.Record;
import org.junit.jupiter.api.Test;


public class UmassTraceParserTest {

    private transient String path = "src/test/resources/WebSearch-sample.spc";

    @Test
    public void fileCheckFirstLineTest() {
        UmassTraceParser parser = new UmassTraceParser();
        Record actual = parser
            .parse(path)
            .findFirst()
            .get();
        Record expected = new Record("657728", 8192);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void sizeOfFileTest() {
        UmassTraceParser parser = new UmassTraceParser();
        int size = parser
            .parse(path)
            .toArray().length;

        int actualSize = 10;
        assertEquals(size, actualSize);
    }

}
