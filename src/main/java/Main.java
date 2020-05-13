import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import configuration.Configuration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import configuration.Trace;
import parser.AbstractParserClass;
import parser.snia.CambridgeTraceParser;
import policy.Policy;
import simulator.Simulator;

public class Main {
    private static final String configurationFilePath = "src/main/resources/default.yml";

    /**
     * Run parser.
     * @param args args.
     */
    public static void main(String[] args) {

        // Read configuration file
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            Configuration configuration =
                    mapper.readValue(
                            new File(configurationFilePath),
                            Configuration.class);
            Class<?> policyClass = Class.forName("policy." + configuration.getPolicies()[0]);
            Constructor<?> policyConstructor = policyClass.getConstructor(int.class, boolean.class);
            Policy policy = (Policy) policyConstructor.newInstance(
                    configuration.getCacheSize(),
                    configuration.isSizeInBytes());

            Trace trace = Trace.valueOf(configuration.getTrace());
            AbstractParserClass parser = trace.getParser();
            String filePath = trace.getFilePath();

            Simulator simulator = new Simulator(
                    policy,
                    parser.parse(filePath));
            simulator.simulate();

            System.out.println("cache hits: " + simulator.getNumberOfCacheHits()
                    + " (" + simulator.getCacheHitsPercentage() + "%)");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: should we handle the rest of the exceptions separately?
            e.printStackTrace();
        }
    }

}
