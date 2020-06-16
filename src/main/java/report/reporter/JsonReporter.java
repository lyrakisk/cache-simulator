package report.reporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import configuration.Configuration;
import report.Result;


public class JsonReporter extends Reporter {
    private transient Configuration configuration;

    public JsonReporter(Result[] results, Configuration configuration) {
        super(results);
        this.configuration = configuration;
    }

    @Override
    public void report() {
        // write results to the json file
        ObjectMapper resultsMapper = new ObjectMapper();

        try {
            resultsMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(configuration.getResultsFilePath()), this.getResults());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
