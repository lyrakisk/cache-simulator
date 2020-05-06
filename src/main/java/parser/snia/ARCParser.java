package parser.snia;

import parser.AbstractParserClass;
import parser.Record;

/**
 * Read me for  traces:
 * https://www.dropbox.com/sh/9ii9sc7spcgzrth/j1CJ72HiWa/Papers/ARCTraces/README.txt
 * Traces taken from:
 * https://researcher.watson.ibm.com/researcher/view_person_subpage.php?id=4700&fbclid=IwAR3MxmGYHx-du67uRz1ZbfM_pGmAmWkS22Lbj5YQ4j4Tl6Ji8itCg8mSnyE
 *
 * Citation:
 * Nimrod Megiddo and Dharmendra S. Modha, "ARC: A Self-Tuning, Low Overhead Replacement Cache," USENIX Conference on File and Storage Technologies (FAST 03), San Francisco, CA, pp. 115-130, March 31-April 2, 2003.
 */
public class ARCParser  extends AbstractParserClass {

    private final int blockSize = 512;
    @Override
    public Record parseRecord(String line) {
        String[] fields = line.split( " ");
        String startingBlock = fields[0];
        int blocksNum = Integer.parseInt(fields[1]);
        long size = blocksNum * blockSize;
        Record record = new Record(startingBlock,size);
        return record;
    }


}
