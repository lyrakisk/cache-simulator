package reporter;

import org.junit.jupiter.api.Test;

public class ReportTest {

    @Test
    public void reportTest() {
        Report test = new Report("Test", 0.44);
        Report test2 = new Report("Test2", 0.4466);

        String content = test.report();
        String content2 = test2.report();

        assert (content.equals("Test,44%,56%"));
        assert (content2.equals("Test2,44.66%,55.34%"));
    }

}
