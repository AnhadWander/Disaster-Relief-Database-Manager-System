package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class ItemTest {

    static class TestItem extends Item {
        public TestItem(int id) {
            super(id);
        }
    }

    @Test
    public void testConstructorValidID() {
        TestItem item = new TestItem(55);
        assertEquals(55, item.getItemID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeID() {
        new TestItem(-10);
    }

    @Test
    public void testSetAllocatedTo() {
        TestItem item = new TestItem(1);
        DisasterVictim vict = new DisasterVictim(2, "Bob", "2025-01-02");
        item.setAllocatedTo(vict);
        assertEquals(vict, item.getAllocatedTo());
    }

    @Test
    public void testSetLocation() {
        TestItem item = new TestItem(1);
        Location loc = new Location(10, "Shelter", "Address");
        item.setLocation(loc);
        assertEquals(loc, item.getLocation());
    }

    @Test
    public void testUpdateSupplyNoChanges() {
        try {
            Item.updateSupply(99, "", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateSupplyTypeOnly() {
        try {
            Item.updateSupply(100, "water", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateSupplyCommentsOnly() {
        try {
            Item.updateSupply(101, "", "Updated supply comments");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateSupplyBothFields() {
        try {
            Item.updateSupply(102, "cot", "Room=115,Grid=B6");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
}
