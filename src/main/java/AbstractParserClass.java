import java.io.FileNotFoundException;
import java.util.stream.Stream;

public abstract class AbstractParserClass {

    /**
     * Parse a record
     * @param filename
     * @return Stream of records.
     * @throws FileNotFoundException
     */
    public abstract Stream<Record> parse(String filename) throws FileNotFoundException;

}
