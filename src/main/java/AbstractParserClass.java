import java.io.FileNotFoundException;
import java.util.stream.Stream;

public abstract class AbstractParserClass {
    /**
     * Parse a record.
     * @param filename name of the file.
     * @return Stream of records.
     */
    public abstract Stream<Record> parse(String filename);

}
