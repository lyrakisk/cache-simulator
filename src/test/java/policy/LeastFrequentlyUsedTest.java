package policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeastFrequentlyUsedTest {
    private transient LeastFrequentlyUsed lfuBytes;
    private transient LeastFrequentlyUsed lfuRecords;

    @BeforeEach
    void init() {
        lfuBytes = new LeastFrequentlyUsed(1000, true);
        lfuRecords = new LeastFrequentlyUsed(1000, false);
    }

//    @Test
//    void testMultipleSmallOneBig() {
//        for (int i = 1; i <= )
//    }
}
