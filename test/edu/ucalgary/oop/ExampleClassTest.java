package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;


public class ExampleClassTest {

    private ExampleClass example;
    private MockDatabase mockDB;

    @Before
    public void setUp() {
        example = new ExampleClass();
        mockDB = new MockDatabase();
    }

    @Test
    public void testLookupSomeDataFound() {
        String result = example.lookupSomeData("testKey", mockDB);
        assertTrue(result.contains("Found record testKey with value 12345"));
    }

    @Test
    public void testLookupSomeDataNotFound() {
        String result = example.lookupSomeData("nonExistent", mockDB);
        assertEquals("No record found for key: nonExistent", result);
    }

    @Test
    public void testStoreData() {
        boolean saved = example.storeData("newKey", 999, mockDB);
        assertTrue(saved);

        DataRecord dr = mockDB.queryForData("newKey");
        assertNotNull(dr);
        assertEquals(999, dr.getValue());
    }
}
