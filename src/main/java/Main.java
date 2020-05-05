import parser.snia.CambridgeTraceParser;
import policy.LeastRecentlyUsed;
import simulator.Simulator;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {
        CambridgeTraceParser parser = new CambridgeTraceParser();
        Simulator simulator = new Simulator(
                new LeastRecentlyUsed(20000, true),
                parser.parse("src/main/resources/msr-cambridge1-sample.csv"));
        simulator.simulate();

        System.out.println("cache hits: " + simulator.getNumberOfCacheHits()
                + " (" + simulator.getCacheHitsPercentage() + "%)");
    }

}
