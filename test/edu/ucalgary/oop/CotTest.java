package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CotTest {

    @Test
    public void testConstructorValid() {
        Cot cot = new Cot(10, "Room101", "G5");
        assertNotNull(cot);
        assertThat(cot.getItemID(), is(10));
        assertThat(cot.getRoom(), is("Room101"));
        assertThat(cot.getGridLocation(), is("G5"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRoomNull() {
        new Cot(1, null, "A1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorGridNull() {
        new Cot(1, "Room", null);
    }

    @Test
    public void testSetRoomValid() {
        Cot cot = new Cot(1, "R", "A1");
        cot.setRoom("UpdatedRoom");
        assertThat(cot.getRoom(), is("UpdatedRoom"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRoomEmpty() {
        Cot cot = new Cot(1, "R", "A1");
        cot.setRoom("");
    }

    @Test
    public void testSetGridLocationValid() {
        Cot cot = new Cot(2, "R", "A1");
        cot.setGridLocation("B2");
        assertThat(cot.getGridLocation(), is("B2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGridLocationEmpty() {
        Cot cot = new Cot(2, "R", "A1");
        cot.setGridLocation("");
    }
}
