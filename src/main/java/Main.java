import parser.ConcreteParser;
import policy.LeastRecentlyUsed;
import simulator.Simulator;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {
        ConcreteParser parser = new ConcreteParser();
        Simulator simulator = new Simulator(
                new LeastRecentlyUsed(20000),
                parser.parse("src/main/resources/request.txt"));
        simulator.simulate();

        System.out.println("cache hits: " + simulator.getNumberOfCacheHits()
                + " (" + simulator.getCacheHitsPercentage() + "%)");
    }

}
