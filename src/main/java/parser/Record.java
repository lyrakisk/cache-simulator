package parser;

public class Record {
    private transient long size;
    private transient String identifier;


    /**
    * Construct a record.
    * @param id identifier.
    * @param size size.
    */
    public Record(String id, long size) {
        this.identifier = id;
        this.size = size;
    }

    /**
    * String representation of a record.
    * @return String string.
    */
    public String toString() {
        String ret = this.identifier + " " + this.size;
        return ret;
    }

    /**
     * Getter for the size of the record.
     * @return the size of the record
     */
    public long getSize() {
        return size;
    }

    /**
     * Getter for the id of the record.
     * @return the id of the record
     */
    public String getId() {
        return identifier;
    }
}
