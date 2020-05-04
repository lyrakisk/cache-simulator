package parser;

public class Record {
    private transient int size;
    private transient int id;


    /**
    * Construct a record.
    * @param id identifier.
    * @param size size.
    */
    public Record(int id, int size) {
        this.id = id;
        this.size = size;
    }

    /**
    * String representation of a record.
    * @return String string.
    */
    public String toString() {
        String ret = this.id + " " + this.size;
        return ret;
    }

    /**
     * Getter for the size of the record.
     * @return the size of the record
     */
    public int getSize() {
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
    public int getId() {
        return id;
    }
}
