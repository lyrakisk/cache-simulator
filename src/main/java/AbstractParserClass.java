import java.io.FileNotFoundException;
import java.util.stream.Stream;

public abstract class AbstractParserClass {

    public abstract Stream<Record> parse(String filename) throws FileNotFoundException;

}
