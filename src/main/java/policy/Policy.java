package policy;

import parser.Record;

/**
 * Interface which all different cache policies will implement.
 */
public interface Policy {

    /**
     * Check whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present, false otherwise
     */
    boolean isPresentInCache(Record record);
}
