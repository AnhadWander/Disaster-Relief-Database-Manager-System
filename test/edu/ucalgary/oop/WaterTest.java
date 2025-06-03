package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class WaterTest {

    @Test
    public void testConstructorValid() {
        Water w = new Water(99);
        assertEquals(99, w.getItemID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeID() {
        new Water(-5);
    }

}
