import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * e.g.
 *
 * Contents of type:
 * 1 0 68
 * 2 1 7454
 * 3 2 40773
 * 4 3 1822
 */

public class ConcreteParser extends  AbstractParserClass {

    @Override
    public Stream<Record> parse(String filename) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader input = new BufferedReader(reader);
        Stream<Record> ret = input.lines().map(this::processLine);
        /**
         * To print it.
         */
//        Object[] arrayList = ret.toArray();
//        for(Object rec: arrayList){
//            if(rec instanceof Record){
//                Record re =  (Record)rec;
//                System.out.println(rec.toString());
//            }
//        }
        return ret;
    }

    public Record processLine(String line) {
        String[] split = line.split(" ");
        Record ret = new Record(Integer.valueOf(split[1]), Integer.valueOf(split[2]));
        return ret;
    }
}