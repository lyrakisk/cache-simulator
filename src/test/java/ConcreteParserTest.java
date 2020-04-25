import org.junit.jupiter.api.Test;
import parser.AbstractParserClass;
import parser.ConcreteParser;
import parser.Record;

public class ConcreteParserTest {

    @Test
    public void parseTest() {
        AbstractParserClass abstractParserClass = new ConcreteParser();
        Object[] stream = abstractParserClass.parse("src/test/resources/test.txt").toArray();
        Record record1 = new Record(0, 2);
        Record record2 = (Record) stream[0];
        assert (record1.toString().equals(record2.toString()));
    }
}
