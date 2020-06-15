package data.parser.robinhood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import configuration.Trace;
import data.parser.Record;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;


/**
 * PMD raises an error for the query variable in lines 62-64
 * because it is assigned multiple times (as it should be since this is a for loop).
 * This is why I suppressed PMD.DataflowAnomalyAnalysis.
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class RobinHoodTraceParserTest {
    private transient String json =
            "{\"t\": 0, \"d\": [{\"7385c12d\": "
                    + "{\"S\": [20788], \"U\": [\"8d4d4ec1f020205f3\"], "
                    + "\"C\": [1]}, \"b4fbebd8\": {\"S\": [20788, 20881, 398, 25514, 26109], "
                    + "\"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\","
                    + " \"641d4cc4e0d96de89\", "
                    + "\"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": [1, 1, 1, 1, 1]}, "
                    + "\"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\", "
                    + "\"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884], "
                    + "\"U\": [\"91e4bf1d25652d04b\"], \"C\": [1]}, \"812126d3\": {\"S\": "
                    + "[37856, 20705, 424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\", "
                    + "\"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\","
                    + " \"bf2f61f5dfaf86b16\"], \"C\": [1, 1, 1, 1, 1]}}]}";

    private transient RobinHoodTraceParser parser = new RobinHoodTraceParser();
    private transient Request request = (Request) parser.parseRecord(json);

    @Test
    void testCorrectId() {
        assertEquals("0", request.getId());
    }

    @Test
    void testCorrectNumberOfqueries() {
        assertEquals(14, request.getQueries().size());
    }

    @Test
    void testFirstQueryCorrectSize() {
        assertEquals(20788, request.getQueries().get(0).getSize());
    }

    @Test
    void testFirstQueryCorrectUrl() {
        assertEquals("8d4d4ec1f020205f3", request.getQueries().get(0).getUrl());
    }

    @Test
    void testFirstQueryShouldBeCachable() {
        assertEquals(true, request.getQueries().get(0).isCachable());
    }

    @Test
    void testAllQueriesShouldBeCachable() {
        for (Query query: request.getQueries()) {
            assertTrue(query.isCachable());
        }
    }

    @Test
    void queryHasMoreSizesThanUrls() {
        String record = "{\"t\": 0, \"d\": "
                + "[{\"7385c12d\": {\"S\": [20788], "
                + "\"U\": [\"8d4d4ec1f020205f3\"], "
                + "\"C\": [1]}, \"b4fbebd8\": {\"S\": [20788, 20881, 398, 25514, 26109], "
                + "\"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\",  \"dbe6fc5abbbc078f5\","
                + " \"991459718784f945f\"], \"C\": "
                + "[1, 1, 1, 1, 1]}, \"39f00c48\": {\"S\": [26192, 2414],"
                + " \"U\": [\"bf2ba48d4c4caa163\", \"362db55d825e027c2\"], \"C\": [1, 1]},"
                + " \"b293d37d\": {\"S\": [20884], \"U\": "
                + "[\"91e4bf1d25652d04b\"], \"C\": [1]},"
                + " \"812126d3\": {\"S\": [37856, 20705, "
                + "424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\","
                + " \"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\","
                + " \"bf2f61f5dfaf86b16\"], \"C\": [1, 1, 1, 1, 1]}}]}";
        assertThrows(AssertionError.class, () -> {
            parser.parseRecord(record); });
    }

    @Test
    void queryHasLessSizesThanUrls() {
        String record = "{\"t\": 0, \"d\": [{\"7385c12d\": {\"S\": [20788], \"U\": "
                + "[\"8d4d4ec1f020205f3\"], \"C\": [1]}, \"b4fbebd8\": {\"S\": [20881, "
                + "398, 25514, 26109], \"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\","
                + "  \"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": [1, 1, 1, 1, 1]}, "
                + "\"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\", "
                + "\"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884],"
                + " \"U\": [\"91e4bf1d25652d04b\"], \"C\": [1]}, \"812126d3\": "
                + "{\"S\": [37856, 20705, 424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\", "
                + "\"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\","
                + " \"bf2f61f5dfaf86b16\"], \"C\": [1, 1, 1, 1, 1]}}]}";
        assertThrows(AssertionError.class, () -> {
            parser.parseRecord(record); });
    }

    @Test
    void queryHasMoreSizesThanCachables() {
        String record = "{\"t\": 0, \"d\": [{\"7385c12d\": {\"S\": "
                + "[20788], \"U\": [\"8d4d4ec1f020205f3\"], \"C\": [1]}, "
                + "\"b4fbebd8\": {\"S\": [20788, 20881, 398, 25514, 26109], "
                + "\"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\",  "
                + "\"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": [1, 1, 1, 1, 1]},"
                + " \"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\", "
                + "\"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884], "
                + "\"U\": [\"91e4bf1d25652d04b\"], \"C\": [1]}, \"812126d3\": {\"S\": [37856,"
                + " 20705, 424, 34915, 20788], \"U\": "
                + "[\"f0bd9a2a45492adca\", \"03eb3847b6c9198d0\","
                + " \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\", \"bf2f61f5dfaf86b16\"], "
                + "\"C\": [1, 1, 1, 1]}}]}";
        assertThrows(AssertionError.class, () -> {
            parser.parseRecord(record); });
    }

    @Test
    void queryHasLessSizesThanCachables() {
        String record = "{\"t\": 0, \"d\": [{\"7385c12d\": {\"S\": [20788], "
                + "\"U\": [\"8d4d4ec1f020205f3\"], \"C\": [1]}, \"b4fbebd8\": "
                + "{\"S\": [20788, 20881, 398, 25514], \"U\": [\"48efdeddbe76e5f60\","
                + " \"3430884714871a984\",  \"dbe6fc5abbbc078f5\"], \"C\": [1, 1, 1, 1, 1]},"
                + " \"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\","
                + " \"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884],"
                + " \"U\": [\"91e4bf1d25652d04b\"], \"C\": [1]}, \"812126d3\": {\"S\": "
                + "[37856, 20705, 424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\", "
                + "\"03eb3847b6c9198d0\","
                + " \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\", \"bf2f61f5dfaf86b16\"], "
                + "\"C\": [1, 1, 1, 1]}}]}";
        assertThrows(AssertionError.class, () -> {
            parser.parseRecord(record); });
    }

    @Test
    void testExceptionCatch() {
        final PrintStream originalErr = System.err;
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        String record = "test";
        parser.parseRecord(record);
        String expectedMessage = "ERROR: Couldn't parse the line: test\n";
        assertEquals(expectedMessage, err.toString());

        System.setErr(originalErr);
    }

    @Test
    void testParseQueryWithWrongField() {
        final PrintStream originalErr = System.err;
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        String record = "{\"t\": 0, \"d\": [{\"7385c12d\": {\"S\":"
                + " [20788], \"U\": [\"8d4d4ec1f020205f3\"], \"C\": [1],"
                + " \"Soutzoukakia\": []}, \"b4fbebd8\": {\"S\": [20788, 20881, "
                + "398, 25514, 26109], \"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\","
                + " \"641d4cc4e0d96de89\", \"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": "
                + "[1, 1, 1, 1, 1]}, \"39f00c48\": "
                + "{\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\","
                + " \"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884], \"U\": "
                + "[\"91e4bf1d25652d04b\"], \"C\": [1]}, "
                + "\"812126d3\": {\"S\": [37856, 20705, 424, 34915,"
                + " 20788], \"U\": [\"f0bd9a2a45492adca\", "
                + "\"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", "
                + "\"c85a93b4541fecf55\", \"bf2f61f5dfaf86b16\"], \"C\": [1, 1, 1, 1, 1]}}]}";
        parser.parseRecord(record);
        String expectedMessage =
                "ERROR: The field Soutzoukakia is not recognized as part of a Query\n";
        assertEquals(expectedMessage, err.toString());

        System.setErr(originalErr);

    }

    @Test
    void testParseMethod() {
        Stream<Record> records = parser.parse("src/test/resources/robinHood-sample.json");

        List<Record> recordList = records.collect(Collectors.toList());

        assertEquals(21, recordList.size());
    }

    @Test
    void testPathFromTraceEnum() {
        Stream<Record> records = parser.parse(Trace.RobinHood.getFilePath());
        assertEquals("0", records.findFirst().get().getId());
    }

    @Test
    void testWrongCachableFieldInTraceFile() {
        final PrintStream originalErr = System.err;
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        String record =
                "{\"t\": 0, \"d\": [{\"7385c12d\": "
                        + "{\"S\": [20788], \"U\": [\"8d4d4ec1f020205f3\"], "
                        + "\"C\": [1]}, \"b4fbebd8\": {\"S\": [20788, 20881, 398, 25514, 26109], "
                        + "\"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\","
                        + " \"641d4cc4e0d96de89\", "
                        + "\"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": [1, 1, 1, 1, 1]}, "
                        + "\"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\", "
                        + "\"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884], "
                        + "\"U\": [\"91e4bf1d25652d04b\"], \"C\": [5]}, \"812126d3\": {\"S\": "
                        + "[37856, 20705, 424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\", "
                        + "\"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\","
                        + " \"bf2f61f5dfaf26b16\"], \"C\": [1, 1, 1, 0, 1]}}]}";
        parser.parseRecord(record);
        String expectedMessage =
                "ERROR: The field C can be either 0 or 1, but instead it was 5\n";
        assertEquals(expectedMessage, err.toString());

        System.setErr(originalErr);
    }
}
