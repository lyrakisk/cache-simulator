package report.reporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import report.Result;


public class JsonReporter extends Reporter {

    public JsonReporter(Result[] results) {
        super(results);
    }

    @Override
    public void report() {
        // write results to the json file
        ObjectMapper resultsMapper = new ObjectMapper();

        try {
            resultsMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("results.json"), this.getResults());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
