import org.junit.jupiter.api.Test;

public class RecordTest {

    @Test
    public void toStringTest(){
        Record record  = new Record(0, 1);
        String temp = "0 1";
        assert (temp.equals(record.toString()));
    }

}
