package parser.data.robinHood;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        final String json = "{\"t\": 0, \"d\": [{\"7385c12d\": {\"S\": [20788], \"U\": [\"8d4d4ec1f020205f3\"], \"C\": [1]}, \"b4fbebd8\": {\"S\": [20788, 20881, 398, 25514, 26109], \"U\": [\"48efdeddbe76e5f60\", \"3430884714871a984\", \"641d4cc4e0d96de89\", \"dbe6fc5abbbc078f5\", \"991459718784f945f\"], \"C\": [1, 1, 1, 1, 1]}, \"39f00c48\": {\"S\": [26192, 2414], \"U\": [\"bf2ba48d4c4caa163\", \"362db55d825e027c2\"], \"C\": [1, 1]}, \"b293d37d\": {\"S\": [20884], \"U\": [\"91e4bf1d25652d04b\"], \"C\": [1]}, \"812126d3\": {\"S\": [37856, 20705, 424, 34915, 20788], \"U\": [\"f0bd9a2a45492adca\", \"03eb3847b6c9198d0\", \"e36470eff6abb2ff2\", \"c85a93b4541fecf55\", \"bf2f61f5dfaf86b16\"], \"C\": [1, 1, 1, 1, 1]}}]}";

        String id = "";
        ArrayList<Query> queries = new ArrayList<Query>();
        String queriesJSON = "";
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(json);

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

        for (Query query: queries) {
            System.out.println("backend: " + query.getBackend());
            System.out.println("  size: " + query.getSize());
            System.out.println("  url: " + query.getUrl());
            System.out.println("  is cachable : " + query.isCachable());
        }

        Request request = new Request(id, queries);
    }
}
