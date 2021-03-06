package data.parser.robinhood;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;


public class QueryTest {

    @Test
    void testIsNotCachable() {
        Query query = new Query("backend", 100, "www.google.com", false);
        assertFalse(query.isCachable());
    }
}
