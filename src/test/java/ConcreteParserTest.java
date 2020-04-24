import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

public class ConcreteParserTest {

    @Test
    public void parseTest() throws FileNotFoundException {
        AbstractParserClass abstractParserClass = new ConcreteParser();

        Object[] stream = abstractParserClass.parse("src/test/resources/test.txt").toArray();
        Object[] temp = new Object[1];
        temp[0] = new Record(0, 2);

        assert(temp.length == stream.length);
        assert(temp.length == 1);
        assert(temp[0].getClass() == Record.class);
        assert(stream[0].getClass() == Record.class);
        Record record1 = (Record) temp[0];
        Record record2 = (Record) stream[0];
        assert(record1.toString().equals(record2.toString()));

    }

}
