package edu.ucalgary.oop;

/**
 * Represents a simple data record with a key-value pair.
 *
 * <p>
 * This class is used by Mockdatabase.java to store entries.
 * </p>
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class DataRecord {
    private final String key;
    private final int value;

    /**
     * Constructs a DataRecord with the specified key and value.
     *
     * @param key   the record's key (e.g. "testKey")
     * @param value the record's integer value
     */
    public DataRecord(String key, int value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Retrieves the key of this data record.
     *
     * @return the key string
     */
    public String getKey() {
        return key;
    }

    /**
     * Retrieves the integer value of this data record.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
