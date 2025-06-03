package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class GenderTest {

    @Test
    public void testEnumValues() {
        Gender[] vals = Gender.values();
        assertEquals(3, vals.length);
        assertSame(Gender.MAN, vals[0]);
        assertSame(Gender.WOMAN, vals[1]);
        assertSame(Gender.NON_BINARY, vals[2]);
    }

    @Test
    public void testValueOfMan() {
        assertSame(Gender.MAN, Gender.valueOf("MAN"));
    }
}
