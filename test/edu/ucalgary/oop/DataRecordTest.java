package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class DataRecordTest {

    @Test
    public void testObjectCreation() {
        DataRecord record = new DataRecord("abc", 100);
        assertNotNull(record);
        assertEquals("abc", record.getKey());
        assertEquals(100, record.getValue());
    }

    @Test
    public void testGetKey() {
        DataRecord record = new DataRecord("xyz", 999);
        assertEquals("xyz", record.getKey());
    }

    @Test
    public void testGetValue() {
        DataRecord record = new DataRecord("someKey", 42);
        assertEquals(42, record.getValue());
    }
}
