package edu.ucalgary.oop;

import java.util.ArrayList;
import java.util.List;

/**
 * FamilyGroup represents a group ID that multiple DisasterVictims can join.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class FamilyGroup {
    private final int groupID;
    private final List<DisasterVictim> members;

    /**
     * Constructs a FamilyGroup with the specified group ID.
     *
     * @param groupID the unique group ID
     */
    public FamilyGroup(int groupID) {
        this.groupID = groupID;
        this.members = new ArrayList<>();
    }

    /**
     * Gets the family group's ID.
     *
     * @return the group ID
     */
    public int getGroupID() {
        return groupID;
    }

    /**
     * Gets the list of DisasterVictims in this family group.
     *
     * @return the list of members
     */
    public List<DisasterVictim> getMembers() {
        return members;
    }

    /**
     * Adds a DisasterVictim to the family group.
     *
     * @param victim the DisasterVictim
     * @throws IllegalArgumentException if victim is null
     */
    public void addMember(DisasterVictim victim) {
        if (victim == null) {
            throw new IllegalArgumentException("Cannot add a null victim to a family group");
        }
        this.members.add(victim);
    }

    /**
     * Removes a DisasterVictim from the group.
     *
     * @param victim the victim to remove
     * @throws IllegalArgumentException if victim not in group
     */
    public void removeMember(DisasterVictim victim) {
        if (!this.members.contains(victim)) {
            throw new IllegalArgumentException("Victim is not in this family group");
        }
        this.members.remove(victim);
    }
}
