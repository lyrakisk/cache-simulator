import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import configuration.Configuration;
import parser.snia.CambridgeTraceParser;
import policy.LeastRecentlyUsed;
import policy.Policy;
import simulator.Simulator;
import java.io.File;
import java.lang.reflect.Constructor;

public class Main {

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {

        String[] policies = {"LeastRecentlyUsed"};

        // Read configuration file
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            Configuration configuration = mapper.readValue(new File("src/main/resources/config.yml"), Configuration.class);
            Class<?> clazz = Class.forName("policy." + configuration.getPolicies()[0]);
            Constructor<?> constructor = clazz.getConstructor(int.class, boolean.class);
            Policy policy = (Policy) constructor.newInstance(configuration.getCacheSize(), configuration.isSizeInBytes());
            CambridgeTraceParser parser = new CambridgeTraceParser();
            Simulator simulator = new Simulator(
                    policy,
                    parser.parse("src/main/resources/msr-cambridge1-sample.csv"));
            simulator.simulate();

            System.out.println("cache hits: " + simulator.getNumberOfCacheHits()
                    + " (" + simulator.getCacheHitsPercentage() + "%)");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
