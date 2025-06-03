package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseManagerTest {

    @Test
    public void testGetInstanceReturnsSame() {
        DatabaseManager d1 = DatabaseManager.getInstance();
        DatabaseManager d2 = DatabaseManager.getInstance();
        assertSame(d1, d2);
    }

    @Test
    public void testResetInstanceCreatesNew() {
        DatabaseManager d1 = DatabaseManager.getInstance();
        DatabaseManager.resetInstance();
        DatabaseManager d2 = DatabaseManager.getInstance();
        assertNotSame(d1, d2);
    }

}
