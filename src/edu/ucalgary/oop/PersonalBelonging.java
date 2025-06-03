package edu.ucalgary.oop;

/**
 * PersonalBelonging represents an item a victim brought with them,
 * stored as a description field.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class PersonalBelonging extends Item {
    private String description;

    /**
     * Constructs a PersonalBelonging item with a description.
     *
     * @param itemID the item ID
     * @param description the description text
     * @throws IllegalArgumentException if description is null/empty
     */
    public PersonalBelonging(int itemID, String description) {
        super(itemID);
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    /**
     * Gets the description for this belonging.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a new description.
     *
     * @param description the new description
     * @throws IllegalArgumentException if null/empty
     */
    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }
}
