import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

public class ConcreteParserTest {

    @Test
    public void parseTest() throws FileNotFoundException {
        AbstractParserClass abstractParserClass = Mockito.mock(ConcreteParser.class);
        try {
            abstractParserClass.parse(Mockito.anyString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Mockito.verify(abstractParserClass, Mockito.times(1));
    }

}
