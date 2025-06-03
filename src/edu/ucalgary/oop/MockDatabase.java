package edu.ucalgary.oop;

import java.util.HashMap;
import java.util.Map;

/**
 * MockDatabase simulates a basic in-memory key-value store
 * for {@link DataRecord} objects.
 *
 * <p>
 * This is useful for testing domain logic without depending on a real database.
 * </p>
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class MockDatabase {
    private Map<String, DataRecord> storage;

    /**
     * Constructs a MockDatabase with a default test entry.
     */
    public MockDatabase() {
        storage = new HashMap<>();
        storage.put("testKey", new DataRecord("testKey", 12345));
    }

    /**
     * Looks up a data record by key.
     *
     * @param param the key to search
     * @return the {@link DataRecord} if found, or null otherwise
     */
    public DataRecord queryForData(String param) {
        return storage.get(param);
    }

    /**
     * Saves a new {@link DataRecord} in the mock storage.
     *
     * @param newData the record to be saved
     * @return true if saved successfully, false if the record was null
     */
    public boolean saveData(DataRecord newData) {
        if (newData == null) {
            return false;
        }
        storage.put(newData.getKey(), newData);
        return true;
    }
}
