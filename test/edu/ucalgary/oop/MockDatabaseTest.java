package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class MockDatabaseTest {

    private MockDatabase mockDB;

    @Before
    public void setUp() {
        mockDB = new MockDatabase();
    }

    @Test
    public void testObjectCreation() {
        assertNotNull("MockDatabase object should be created", mockDB);
    }

    @Test
    public void testQueryForDataFound() {
        DataRecord result = mockDB.queryForData("testKey");
        assertNotNull("Should find pre-populated record for 'testKey'", result);
        assertEquals("testKey", result.getKey());
        assertEquals(12345, result.getValue());
    }

    @Test
    public void testQueryForDataNotFound() {
        DataRecord result = mockDB.queryForData("no_such_key");
        assertNull(result);
    }

    @Test
    public void testSaveData() {
        DataRecord newRec = new DataRecord("newKey", 999);
        boolean saved = mockDB.saveData(newRec);
        assertTrue(saved);

        DataRecord fetched = mockDB.queryForData("newKey");
        assertNotNull(fetched);
        assertEquals(999, fetched.getValue());
    }

    @Test
    public void testSaveDataNull() {
        boolean saved = mockDB.saveData(null);
        assertFalse(saved);
    }
}
