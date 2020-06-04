package parser.data.robinHood;

import parser.data.Record;

import java.util.ArrayList;

/**
 * This parser reads RobinHood traces as generated by the creators of the RobinHood policy.
 * traces: https://github.com/dasebe/robinhoodcache/blob/master/tracegen/trace.json
 * RobinHood policy paper:
 */
public class Request extends Record {
    ArrayList<Query> queries;

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



}
