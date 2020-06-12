import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import configuration.Configuration;
import configuration.Trace;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.u8.U8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import parser.AbstractParserClass;
import policy.Policy;
import report.Result;
import simulator.Simulator;

/**
 * The DataflowAnomalyAnalysis is suppressed here, because it's raised
 * for the wrong reason. PMD thinks that the className variable inside the
 * for loop is not initialized.
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class Main {
    private static final String configurationFilePath = "src/main/resources/custom.yml";

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

            ArrayList<Policy> policies = new ArrayList<Policy>();

            for (String className: configuration.getPolicies()) {
                Class<?> policyClass = Class.forName("policy." + className);
                Constructor<?> policyConstructor =
                        policyClass.getConstructor(int.class, boolean.class);
                policies.add((Policy) policyConstructor.newInstance(
                        configuration.getCacheSize(), configuration.isSizeInBytes()));
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

            // write results to the json file
            ObjectMapper resultsMapper = new ObjectMapper();

            resultsMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("results.json"), results);

            // print results to console
            AsciiTable table = new AsciiTable();
            table.addRow("Policy", "Requests", "Hit Rate",
                    "Hits", "Evictions", "Avg. Time per Request (millis)", "Operations");
            table.addRule();
            for (Result result: results) {
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
            System.out.println("Simulation finished in " + totalTime + " milliseconds.");

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
