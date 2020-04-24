import java.io.FileNotFoundException;
import java.util.stream.Stream;

public abstract class AbstractParserClass {
    /**
     * Parse a record.
     * @param filename name of the file.
     * @return Stream of records.
     * @throws FileNotFoundException when file not found.
     */
    public abstract Stream<Record> parse(String filename) throws FileNotFoundException;

}
