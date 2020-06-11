package data.parser.adaptsize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import data.parser.Record;
import org.junit.jupiter.api.Test;


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
