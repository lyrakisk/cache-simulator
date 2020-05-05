package parser;

import java.util.stream.Stream;

public abstract class AbstractParserClass {
    /**
     * Parse a trace file. These files come in different formats
     * (e.g. .txt, .csv, .trace etc...)
     * @param filename name of the file.
     * @return Stream of records.
     */
    public abstract Stream<Record> parse(String filename);

    /**
     * Parses one line of a trace file and converts it to a record.
     * @param line line of a trace file.
     * @return record.
     */
    public abstract Record parseRecord(String line);
}
