package edu.ucalgary.oop;

/**
 * Blanket class representing a simple item of type Blanket.
 * Extends Item without adding new fields.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Blanket extends Item {
    /**
     * Constructs a Blanket with the specified item ID.
     *
     * @param itemID the ID for this Blanket
     * @throws IllegalArgumentException if itemID is negative
     */
    public Blanket(int itemID) {
        super(itemID);
    }
}
