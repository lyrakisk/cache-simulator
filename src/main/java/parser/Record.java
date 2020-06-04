package parser;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "ID=" + identifier + ",size=" + size;
    }


    /**
     * Returns the object's hash code.
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return identifier.hashCode() + (int) size;
    }

    /**
     * Checks whether another object is the same as this one.
     * @param obj the object to be checked against
     * @return true if the objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Record)) {
            return false;
        }

        Record other = (Record) obj;
        return this.identifier.equals(other.identifier) && this.size == other.size;
    }
}
