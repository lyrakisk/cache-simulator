package data.parser.upenn;

import data.parser.AbstractParserClass;
import data.parser.Record;

/**
 * Parser for trace file provided by the university of Pennsylvania
 * https://www.cis.upenn.edu/~milom/cis371-Spring08/homework/hw3/
 */
public class UpennTraceParser extends AbstractParserClass {

    //block size is given  to be 64B => 64* 1024 bytes = 65536 bytes.
    transient long size = 65536;

    @Override
    public Record parseRecord(String line) {
        String[] strings = line.split(" ");
        Record record = new Record(strings[0], size);
        return record;
    }
}
