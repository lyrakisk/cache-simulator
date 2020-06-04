package parser.adaptSize;

import org.junit.jupiter.api.Test;
import parser.Record;
import parser.upenn.UpennTraceParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdaptSizeParserTest {

    @Test
    void sampleFileCheckFirstLineTest() {
        AdaptSizeParser parser = new AdaptSizeParser();
        Record actual = parser
                .parse("src/test/resources/request.trace")
                .findFirst()
                .get();
        Record expected = new Record("0", 68);
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getId(), actual.getId());
    }



}
