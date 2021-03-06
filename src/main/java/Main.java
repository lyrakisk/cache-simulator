import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import configuration.Configuration;
import configuration.Trace;
import data.parser.AbstractParserClass;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import report.Result;
import report.reporter.ConsoleReporter;
import report.reporter.JsonReporter;
import simulation.policy.Policy;
import simulation.simulator.Simulator;

/**
 * The DataflowAnomalyAnalysis is suppressed here, because it's raised
 * for the wrong reason. PMD thinks that the className variable inside the
 * for loop is not initialized.
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class Main {
    private static final String customConfigurationFilePath = "configuration/custom.yml";
    private static  final String defaultConfigurationFilePath = "configuration/default.yml";

    /**
     * Run data.parser.
     * @param args args.
     */
    public static void main(String[] args) {

        // Read configuration file
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        Configuration configuration =
                null;
        try {
            configuration = mapper.readValue(
                    new File(customConfigurationFilePath),
                    Configuration.class);
        } catch (IOException e) {
            try {
                configuration = mapper.readValue(new File(defaultConfigurationFilePath),
                        Configuration.class);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        try {

            ArrayList<Policy> policies = new ArrayList<Policy>();

            for (String className: configuration.getPolicies()) {
                Class<?> policyClass = Class.forName("simulation.policy." + className);
                Constructor<?> policyConstructor = policyClass.getConstructor(Configuration.class);
                policies.add((Policy) policyConstructor.newInstance(configuration));
            }


            Trace trace = Trace.valueOf(configuration.getTrace());
            AbstractParserClass parser = trace.getParser();
            String filePath = trace.getFilePath();

            Simulator simulator = new Simulator(
                    policies,
                    parser.parse(filePath));
            long startTime = System.nanoTime();
            Result[] results = simulator.simulate();
            long endTime = System.nanoTime();

            // convert time to milliseconds
            double totalTime = (endTime - startTime) / 1000000.0;

            JsonReporter jsonReporter = new JsonReporter(results, configuration);
            jsonReporter.report();

            if (configuration.isPrintResultsToConsole()) {
                ConsoleReporter consoleReporter = new ConsoleReporter(results);
                consoleReporter.report();
            }

            System.out.println("Simulation finished in " + totalTime + " milliseconds.");

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: should we handle the rest of the exceptions separately?
            e.printStackTrace();
        }
    }
}
