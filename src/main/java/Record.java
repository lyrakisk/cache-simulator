public class Record {
   private  int id ;
   private int size;


   Record (int id, int size){
       this.id = id;
       this.size = size;
   }

   public String toString(){
       String ret = this.id + " " + this.size;
       return ret;
   }
}
