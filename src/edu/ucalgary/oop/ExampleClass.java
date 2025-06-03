package edu.ucalgary.oop;

/**
 * ExampleClass demonstrates how one might interact with a {@link MockDatabase}
 * by looking up or storing data records.
 *
 * <p>
 * This class is primarily for testing or demonstration purposes.
 * </p>
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class ExampleClass {

    /**
     * Looks up data from the provided {@link MockDatabase} using a key.
     *
     * @param param the key to search for
     * @param db    the mock database instance
     * @return a string describing the lookup result; if no record found,
     *         returns a "not found" message
     */
    public String lookupSomeData(String param, MockDatabase db) {
        DataRecord record = db.queryForData(param);
        if (record == null) {
            return "No record found for key: " + param;
        }
        int value = record.getValue();
        return "Found record " + record.getKey() + " with value " + value;
    }

    /**
     * Stores a new key-value pair in the provided {@link MockDatabase}.
     *
     * @param key   the key to store
     * @param value the integer value
     * @param db    the mock database instance
     * @return true if the data was saved successfully, false otherwise
     */
    public boolean storeData(String key, int value, MockDatabase db) {
        DataRecord newRec = new DataRecord(key, value);
        return db.saveData(newRec);
    }
}
