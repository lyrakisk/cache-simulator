package parser.arc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import parser.AbstractParserClass;
import parser.Record;



/**
 * Read me for  traces:
 * https://www.dropbox.com/sh/9ii9sc7spcgzrth/j1CJ72HiWa/Papers/ARCTraces/README.txt
 * Traces taken from:
 * https://researcher.watson.ibm.com/researcher/ +
 * view_person_subpage.php?id=4700&fbclid +
 * =IwAR3MxmGYHx-du67uRz1ZbfM_pGmAmWkS22Lbj5YQ4j4Tl6Ji8itCg8mSnyE
 * Citation:
 * Nimrod Megiddo and Dharmendra S. Modha,
 * "Arc: A Self-Tuning, Low Overhead Replacement Cache,
 * " USENIX Conference on File and Storage Technologies (FAST 03),
 * San Francisco, CA, pp. 115-130, March 31-April 2, 2003.
 */
public class ArcTraceParser extends AbstractParserClass {

    transient long blockSize = 512;

    /**
     * Parser for the traces described in he description of the class.
     * It was necessary to overwrite it as from a single line,
     * we can produce multiple records.
     * @param filename name of the file.
     * @return stream Stream of records.
     */

    @Override
    public  Stream<Record> parse(String filename) {

        InputStream inputStream;

        try {
            inputStream = new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader input = new BufferedReader(reader);
            Stream<Stream<Record>> nestedStream = input.lines().map(this::parseMultipleRecords);
            Stream<Record> stream = nestedStream.flatMap(Function.identity());
            return stream;
        } catch (FileNotFoundException e) {
            System.err.print("ERROR: The file named " + filename + " was not found!\n");
            e.printStackTrace();
            return Stream.empty();
        }
    }

    /**
    * For efficiency reasons it is not used => returns null.
    * @param line line of a trace file.
    * @return  null
    */
    @Override
    public Record parseRecord(String line) {
        return null;
    }

    /**
    * Parse multiple records in case that a line in a trace file,
    * corresponds to more than one records.
    * @param line String line that corresponds to a line in the trace file.
    * @return Stream of Records.
    */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Stream<Record> parseMultipleRecords(String line) {

        String[] fields = line.split(" ");
        long startingBlock = Long.parseLong(fields[0]);
        int numberOfBlocks = Integer.parseInt(fields[1]);

        List<Record> recordList = new ArrayList<Record>();

        for (int i = 0; i < numberOfBlocks; i++) {
            Record record = new Record(Long.toString(startingBlock + i), blockSize);
            recordList.add(record);
        }

        return recordList.stream();
    }


}
