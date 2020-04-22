import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DemoClassTest {
    transient DemoClass demo;

    @BeforeEach
    void setUp() {
        demo = new DemoClass(5);
    }

    @Test
    public void testGetterAndSetter() {
        demo.setNumber(3);
        assertEquals(3, demo.getNumber());
    }

    @Test
    public void testIsPositive() {
        assertTrue(demo.isPositive());
    }

    @Test
    public void testIsNotPositive() {
        demo.setNumber(-45);
        assertFalse(demo.isPositive());
    }

}
