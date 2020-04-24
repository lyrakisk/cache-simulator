import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

/**
 * e.g.
 * Contents of type:
 * 1 0 68
 * 2 1 7454
 * 3 2 40773
 * 4 3 1822
 */

public class ConcreteParser extends AbstractParserClass {
    /**
     * Parse file contents.
     * @param filename name of the file.
     * @return Stream of records.
     * @throws FileNotFoundException when file is not found
     */
    @Override
    @SuppressWarnings("PMD")
    public Stream<Record> parse(String filename) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader input = new BufferedReader(reader);
        Stream<Record> ret = input.lines().map(this::processLine);
        return ret;
    }

    /**
     * Construct a Record object from a given string line.
     * @param line String line.
     * @return Record
     */
    public Record processLine(String line) {
        String[] split = line.split(" ");
        Record ret = new Record(Integer.valueOf(split[1]), Integer.valueOf(split[2]));
        return ret;
    }
}