package edu.ucalgary.oop;

/**
 * Cot represents an Item that has a room and grid location.
 * Extends the abstract Item class.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Cot extends Item {
    private String room;
    private String gridLocation;

    /**
     * Constructs a Cot with the specified ID, room, and grid location.
     *
     * @param itemID the item ID
     * @param room the room number or label
     * @param gridLocation the grid coordinate
     * @throws IllegalArgumentException if itemID is negative, room or gridLocation is null/empty
     */
    public Cot(int itemID, String room, String gridLocation) {
        super(itemID);
        if (room == null || room.isEmpty()) {
            throw new IllegalArgumentException("Room cannot be null/empty");
        }
        if (gridLocation == null || gridLocation.isEmpty()) {
            throw new IllegalArgumentException("Grid location cannot be null/empty");
        }
        this.room = room;
        this.gridLocation = gridLocation;
    }

    /**
     * Gets the room associated with this Cot.
     *
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * Sets the room for this Cot.
     *
     * @param room the new room
     * @throws IllegalArgumentException if room is null/empty
     */
    public void setRoom(String room) {
        if (room == null || room.isEmpty()) {
            throw new IllegalArgumentException("Room cannot be null/empty");
        }
        this.room = room;
    }

    /**
     * Gets the grid location of this Cot.
     *
     * @return the grid location
     */
    public String getGridLocation() {
        return gridLocation;
    }

    /**
     * Sets the grid location for this Cot.
     *
     * @param gridLocation the new grid location
     * @throws IllegalArgumentException if gridLocation is null/empty
     */
    public void setGridLocation(String gridLocation) {
        if (gridLocation == null || gridLocation.isEmpty()) {
            throw new IllegalArgumentException("Grid location cannot be null/empty");
        }
        this.gridLocation = gridLocation;
    }
}
