package parser.snia;

import parser.AbstractParserClass;
import parser.data.Record;


/**
 * Parser for Cambridge traces, as specified in
 * https://www.usenix.org/legacy/event/fast08/tech/narayanan.html
 */
public class CambridgeTraceParser extends AbstractParserClass {

    /**
     * Construct a parser.record.Record object from a given string line.
     * @param line String line.
     * @return parser.record.Record
     */
    public Record parseRecord(String line) {
        String[] fields = line.split(",");
        Record record = new Record(fields[4], Long.parseLong(fields[5]));
        return record;
    }
}