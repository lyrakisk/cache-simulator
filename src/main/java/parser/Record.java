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
     * Getter for the size of the record.
     * @return the size of the record
     */
    public long getSize() {
        return size;
    }

    /**
     * Setter for the size of the record.
     * @param size the new size of the record
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Getter for the id of the record.
     * @return the id of the record
     */
    public String getId() {
        return identifier;
    }


}
