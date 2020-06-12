package data.parser;

public class Record {
    private transient long size;
    private transient String identifier;


    /**
    * Construct a data.record.
    * @param id identifier.
    * @param size size.
    */
    public Record(String id, long size) {
        this.identifier = id;
        this.size = size;
    }


    /**
     * Getter for the size of the data.record.
     * @return the size of the data.record
     */
    public long getSize() {
        return size;
    }

    /**
     * Setter for the size of the data.record.
     * @param size the new size of the data.record
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Getter for the id of the data.record.
     * @return the id of the data.record
     */
    public String getId() {
        return identifier;
    }
}
