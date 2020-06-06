package parser.adaptSize;

import parser.AbstractParserClass;
import parser.Record;

/**
 * Trace parser for the files taken from:
 * https://github.com/dasebe/webcachesim#how-to-get-traces
 * time id  size
 * 1    1    120
 * 2    2     64
 * 3    1    120
 * 4    3     14
 *
 */
public class AdaptSizeParser extends AbstractParserClass {

    @Override
    public Record parseRecord(String line) {
        String[] lineS = line.split(" ");
        String id = lineS[1];
        String size = lineS[2];
        return new Record(id, Long.parseLong(size));
    }
}
