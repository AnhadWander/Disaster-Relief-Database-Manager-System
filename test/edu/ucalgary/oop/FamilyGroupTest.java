package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FamilyGroupTest {

    private FamilyGroup group;
    private DisasterVictim dv;

    @Before
    public void setUp() {
        group = new FamilyGroup(50);
        dv = new DisasterVictim(1, "Test", "2025-01-01");
    }

    @Test
    public void testGetGroupID() {
        assertEquals(50, group.getGroupID());
    }

    @Test
    public void testAddMemberValid() {
        group.addMember(dv);
        assertEquals(1, group.getMembers().size());
        assertEquals(dv, group.getMembers().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMemberNull() {
        group.addMember(null);
    }

    @Test
    public void testRemoveMemberValid() {
        group.addMember(dv);
        group.removeMember(dv);
        assertTrue(group.getMembers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMemberNotInGroup() {
        group.removeMember(dv);
    }
}
