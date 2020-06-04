package parser.robinHood;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import parser.AbstractParserClass;
import parser.Record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class RobinHoodTraceParser extends AbstractParserClass {

    @Override
    public Record parseRecord(String line) {

        try {
            String id = "";
            ArrayList<Query> queries = new ArrayList<Query>();
            String queriesJSON = "";
            JsonFactory factory = new JsonFactory();

            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode = mapper.readTree(line);

            Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {

                Map.Entry<String,JsonNode> field = fieldsIterator.next();

                if (field.getKey().equals("t")) {
                    id = field.getValue().toString();
                } else {
                    queriesJSON = field.getValue().toString();
                }
            }

            queriesJSON = queriesJSON.substring(1, queriesJSON.length() - 1);
            rootNode = mapper.readTree(queriesJSON);


            // parse the queries of the request
            fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {

                Map.Entry<String,JsonNode> field = fieldsIterator.next();

                String backend = field.getKey().toString();
                JsonNode queryRootNode = mapper.readTree(field.getValue().toString());

                Iterator<Map.Entry<String,JsonNode>> queryIterator = queryRootNode.fields();
                long[] sizes = {};
                String[] urls = {};
                byte[] cachables = {};

                while (queryIterator.hasNext()) {
                    Map.Entry<String,JsonNode> queryField = queryIterator.next();

                    if (queryField.getKey().equals("S")) {
                        sizes =  mapper.readValue(queryField.getValue().toString(), long[].class);
                    } else if (queryField.getKey().equals("U")) {
                        urls = mapper.readValue(queryField.getValue().toString(), String[].class);
                    } else if (queryField.getKey().equals("C")) {
                        cachables = mapper.readValue(queryField.getValue().toString(), byte[].class);
                    }

                }

                assert sizes.length == urls.length;
                assert sizes.length == cachables.length;
                for (int i = 0; i < sizes.length; i++) {
                    queries.add(new Query(backend, sizes[i], urls[i], cachables[i]));
                }

            }



            return new Request(id, queries);

        } catch (Exception e) {
            System.err.print("ERROR: Couldn't parse the line: " + line);
            e.printStackTrace();
        }

        return new Request("", new ArrayList<>());
    }
}
