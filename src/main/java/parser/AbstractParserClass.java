package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

public abstract class AbstractParserClass {
    /**
     * Parse a trace file. These files come in different formats
     * (e.g. .txt, .csv, .trace etc...)
     * @param filename name of the file.
     * @return Stream of records.
     */
    public  Stream<Record> parse(String filename) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader input = new BufferedReader(reader);
            Stream<Record> ret = input.lines().map(this::parseRecord);
            return ret;
        } catch (FileNotFoundException e) {
            System.err.print("ERROR: The file named " + filename + " was not found!\n");
            return Stream.empty();
        }
    }

    /**
     * Parses one line of a trace file and converts it to a record.
     * @param line line of a trace file.
     * @return record.
     */
    public abstract Record parseRecord(String line);
}
