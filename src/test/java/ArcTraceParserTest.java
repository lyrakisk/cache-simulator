import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import parser.Record;
import parser.arc.ArcTraceParser;



public class ArcTraceParserTest {

    transient String path = "src/test/resources/OLTP-sample-2.lis";

    @Test
    public void simpleFileCheckFirstLineTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Record actual = parser
            .parse("src/test/resources/OLTP-sample.lis")
            .findFirst()
            .get();
        Record expected = new Record("1", 512);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void checkSizeOfSimpleFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        int size = parser
            .parse("src/test/resources/OLTP-sample.lis")
            .toArray().length;

        int actualSize = 3;
        assertEquals(size, actualSize);
    }

    @Test
    public void checkSizeOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        int size = parser
            .parse(path)
            .toArray().length;

        int actualSize = 6;
        assertEquals(actualSize, size);
    }

    @Test
    public void checkSecondLineOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Object[] records = parser
            .parse(path)
            .toArray();
        Record record = (Record) records[1];

        String actualId = "2";
        assertEquals(actualId, record.getId());
    }

    @Test
    public void checkThirdLineOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Object[] records = parser
            .parse(path)
            .toArray();
        Record record = (Record) records[2];

        String actualId = "3";
        assertEquals(actualId, record.getId());
    }


    @Test
    public void checkFourthLineOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Object[] records = parser
            .parse(path)
            .toArray();
        Record record = (Record) records[3];

        String actualId = "2";
        assertEquals(actualId, record.getId());
    }

    @Test
    public void checkFifthLineOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Object[] records = parser
            .parse(path)
            .toArray();
        Record record = (Record) records[4];

        String actualId = "3";
        assertEquals(actualId, record.getId());
    }

    @Test
    public void checkLastLineOfBiggerFileTest() {
        ArcTraceParser parser = new ArcTraceParser();
        Object[] records = parser
            .parse(path)
            .toArray();
        Record record = (Record) records[5];

        String actualId = "1";
        assertEquals(actualId, record.getId());
    }

}


