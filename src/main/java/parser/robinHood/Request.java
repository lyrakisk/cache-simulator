package parser.robinHood;

import parser.Record;

import java.util.ArrayList;

/**
 * Requests for the RobinHood policy. Each request can consist of multiple queries.
 */
public class Request extends Record {
    private transient ArrayList<Query> queries;

    /**
     * Construct a record.
     * @param id in this case, the timestamp of each request will serve as the record's id.
     */
    public Request(String id, ArrayList<Query> queries) {
        /*
        For this parser, each record represents a request,
        therefore there is no point of specifying the record's size.
         */
        super(id, 0);
        this.queries = queries;

    }

    public ArrayList<Query> getQueries() {
        return queries;
    }

}
