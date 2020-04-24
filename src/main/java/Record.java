public class Record {
   private  int id ;
   private int size;

    /**
     * Construct a record.
     * @param id
     * @param size
     */
   Record (int id, int size){
       this.id = id;
       this.size = size;
   }

    /**
     * String representation of a record
     * @return String
     */
   public String toString(){
       String ret = this.id + " " + this.size;
       return ret;
   }
}
