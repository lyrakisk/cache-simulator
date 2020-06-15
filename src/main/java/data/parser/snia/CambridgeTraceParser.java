package data.parser.snia;

import data.parser.AbstractParserClass;
import data.parser.Record;


/**
 * Parser for Cambridge traces, as specified in
 * https://www.usenix.org/legacy/event/fast08/tech/narayanan.html
 */
public class CambridgeTraceParser extends AbstractParserClass {

    /**
     * Construct a data.parser.data.record.Record object from a given string line.
     * @param line String line.
     * @return data.parser.data.record.Record
     */
    public Record parseRecord(String line) {
        String[] fields = line.split(",");
        Record record = new Record(fields[4], Long.parseLong(fields[5]));
        return record;
    }
}