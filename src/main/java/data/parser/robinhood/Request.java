package data.parser.robinhood;

import data.parser.Record;
import java.util.ArrayList;

/**
 * Requests for the RobinHood simulation.policy. Each request can consist of multiple queries.
 */
public class Request extends Record {
    private transient ArrayList<Query> queries;

    /**
     * Construct a data.record.
     * @param id in this case, the timestamp of each request will serve as the data.record's id.
     */
    public Request(String id, ArrayList<Query> queries) {
        /*
        For this data.parser, each data.record represents a request,
        therefore there is no point of specifying the data.record's size.
         */
        super(id, 0);
        this.queries = queries;

    }

    public ArrayList<Query> getQueries() {
        return queries;
    }

    public void addQuery(Query query) {
        queries.add(query);
    }

}
