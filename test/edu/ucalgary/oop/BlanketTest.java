package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BlanketTest {

    @Test
    public void testConstructorValidID() {
        Blanket blanket = new Blanket(100);
        assertNotNull("Blanket object should be created", blanket);
        assertThat(blanket.getItemID(), is(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeIDThrows() {
        new Blanket(-1);
    }
}
