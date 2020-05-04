import org.junit.jupiter.api.Test;
import parser.AbstractParserClass;
import parser.Record;
import parser.snia.CambridgeTraceParser;

public class CambridgeTraceParserTest {

    @Test
    public void parseTest() {
        AbstractParserClass abstractParserClass = new CambridgeTraceParser();
        Object[] stream = abstractParserClass
                .parse("src/test/resources/msr-cambridge1-sample.csv")
                .toArray();
        Record record1 = new Record("9056014336", 2048);
        Record record2 = (Record) stream[0];
        assert (record1.toString().equals(record2.toString()));
    }
}
