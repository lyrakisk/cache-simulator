package reporter;

import java.io.FileWriter;

public class Report {
    transient private double percentage;

    /**
     * Constructor for the report.
     *
     * @param percentage The percentage of success of cache hits/miss.
     */
    public Report(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Creating the report.
     */
    public void report() {
        try {
            FileWriter fw = new FileWriter("src/main/resources/reports/report.txt");
            fw.write(String.valueOf(this.percentage));
            fw.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

}
