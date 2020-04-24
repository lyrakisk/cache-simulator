package Reporter;

import java.io.FileWriter;

public class Report {
    private double rate;

    /**
     * Constructor for the report.
     * @param rate
     */
    public Report(double rate) {
        this.rate = rate;
    }

    /**
     * Creating the report.
     * */
    public void report() {
        try{
            FileWriter fw = new FileWriter("src/main/resources/reports/report.txt");
            fw.write(String.valueOf(this.rate));
            fw.close();
        } catch(Exception e){System.out.println(e);}
    }

}
