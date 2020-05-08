package reporter;

import java.text.DecimalFormat;

public class Report {
    private transient String policyType;
    private transient double hitRatio;

    /**
     * Constructor for the report.
     *
     * @param policyType The cache policy type.
     * @param hitRatio The percentage of hit success.
     */
    public Report(String policyType, double hitRatio) {
        this.policyType = policyType;
        this.hitRatio = hitRatio;
    }

    /**
     * Create the report..
     */
    public String report() {
        DecimalFormat df = new DecimalFormat("#.##");
        String hitRatio = String.valueOf(df.format(this.hitRatio * 100)) + '%';
        String missRatio = String.valueOf(df.format((1 - this.hitRatio) * 100)) + '%';
        String toPrint = this.policyType + "," + hitRatio + "," + missRatio;
        // todo
        // Decide how we will present results to the users.
        return toPrint;
    }
}
