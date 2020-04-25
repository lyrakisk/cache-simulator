public class Record {
    private transient int size;
    private transient int id;


    /**
    * Construct a record.
    * @param id identifier.
    * @param size size.
    */
    Record(int id, int size) {
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
}
