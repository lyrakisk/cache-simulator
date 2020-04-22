import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DemoClassTest {
    @Test
    public void testGetterAndSetter() {
        DemoClass demo = new DemoClass(5);
        demo.setNumber(3);
        assertEquals(3, demo.getNumber());
    }
}
