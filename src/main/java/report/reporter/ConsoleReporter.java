package report.reporter;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.u8.U8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import report.Result;

public class ConsoleReporter extends Reporter {

    public ConsoleReporter(Result[] results) {
        super(results);
    }

    /**
     * The DataflowAnomalyAnalysis is suppressed here, because it's raised
     * for the wrong reason. PMD thinks that the result variable inside the
     * for loop is not initialized, which is the case indeed but it doesn't need to be.
     */
    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void report() {
        AsciiTable table = new AsciiTable();
        table.addRow("Policy", "Requests", "Hit Rate",
                "Hits", "Evictions", "Avg. Time per Request (millis)", "Operations");
        table.addRule();
        for (Result result: this.getResults()) {
            table.addRow(
                    result.getPolicy(),
                    result.getNumberOfRequests(),
                    result.getHitRate(),
                    result.getNumberOfHits(),
                    result.getEvictions(),
                    result.getAverageProcessTimePerRequest(),
                    result.getNumberOfOperations());
            table.addRule();
        }
        table.getContext().setGrid(U8_Grids.borderDouble());
        table.setTextAlignment(TextAlignment.CENTER);

        String renderedTable = table.render();
        System.out.println(renderedTable);

    }
}
