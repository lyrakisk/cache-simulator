package parser.snia;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

import parser.AbstractParserClass;
import parser.Record;


public class CambridgeTraceParser extends AbstractParserClass {
    /**
     * Parser for Cambridge traces, as specified in
     * https://www.usenix.org/legacy/event/fast08/tech/narayanan.html
     * @param filename name of the file.
     * @return Stream of records.
     */
    @Override
    public Stream<Record> parse(String filename) {

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader input = new BufferedReader(reader);
            Stream<Record> ret = input.lines().map(this::parseRecord);
            return ret;
        } catch (FileNotFoundException e) {
            System.err.print("ERROR: The file named \" + filename + \" was not found!\n");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Construct a parser.Record object from a given string line.
     * @param line String line.
     * @return parser.Record
     */
    public Record parseRecord(String line) {
        String[] fields = line.split(",");
        Record record = new Record(fields[4], Integer.parseInt(fields[5]));
        return record;
    }
}