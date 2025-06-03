package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LanguageManagerTest {

    private LanguageManager lm;

    @Before
    public void setUp() {
        lm = new LanguageManager();
    }

    @Test
    public void testDefaultLocaleIsEnCA() {
        assertEquals("en-CA", lm.getCurrentLocaleCode());
    }

    @Test
    public void testGetStringNotLoaded() {
        assertNull(lm.getString("anyKey"));
    }

    @Test
    public void testLoadLocaleInvalidCode() {
        lm.loadLocale("invalid-code");
        assertEquals("en-CA", lm.getCurrentLocaleCode());
    }

}
