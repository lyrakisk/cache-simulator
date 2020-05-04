import org.junit.jupiter.api.Test;
import parser.Record;
import parser.snia.CambridgeTraceParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CambridgeTraceParserTest {

    @Test
    public void parseTest() {
        CambridgeTraceParser parser = new CambridgeTraceParser();
        Record actual = parser
                .parse("src/test/resources/msr-cambridge1-sample.csv")
                .findFirst()
                .get();
        Record expected = new Record("9056014336", 2048);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }
}
