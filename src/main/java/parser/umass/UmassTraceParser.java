package parser.umass;

import parser.AbstractParserClass;
import parser.Record;

/**
 * Traces taken  from and described here:
 * http://traces.cs.umass.edu/index.php/Storage/Storage?fbclid=IwAR2XFXBKz2AKUgV8UUPgo2fBqmJ9sF6gwc1K_l9wo9EhdkTWfxZZIrdmjK4
 *
 * example line:
 *0,20941264,8192,W,0.551706,Alpha/NT
 * Field 1: Application specific unit
 * Field 2: Logical block Address
 * Field 3: Size (in bytes)
 * Field 4: Operation Code
 * Field 5: Timestamp
 * ... optional fields.
 *
 */

public class UmassTraceParser extends AbstractParserClass {

    @Override
    public Record parseRecord(String line) {
        String[] sliced = line.split(",");
        String id = sliced[1];
        long size = Long.valueOf(sliced[2]);

        return new Record(id, size);
    }
}
