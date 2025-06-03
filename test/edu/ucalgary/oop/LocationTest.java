package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LocationTest {

    private Location loc;

    @Before
    public void setUp() {
        loc = new Location(5, "LocName", "123 Address");
    }

    @Test
    public void testConstructorValid() {
        assertEquals(5, loc.getLocationID());
        assertEquals("LocName", loc.getName());
        assertEquals("123 Address", loc.getAddress());
        assertTrue(loc.getItems().isEmpty());
        assertTrue(loc.getOccupants().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        new Location(1, "", "Addr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyAddr() {
        new Location(1, "SomeName", "");
    }

    @Test
    public void testSetNameValid() {
        loc.setName("NewName");
        assertThat(loc.getName(), is("NewName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameEmpty() {
        loc.setName("");
    }

    @Test
    public void testSetAddressValid() {
        loc.setAddress("NewAddr");
        assertThat(loc.getAddress(), is("NewAddr"));
    }

    @Test
    public void testAddOccupant() {
        DisasterVictim dv = new DisasterVictim(1, "Bob", "2025-01-01");
        loc.addOccupant(dv);
        assertThat(loc.getOccupants().size(), is(1));
        assertSame(dv, loc.getOccupants().get(0));
    }

    @Test
    public void testRemoveOccupant() {
        DisasterVictim dv = new DisasterVictim(1, "Bob", "2025-01-01");
        loc.addOccupant(dv);
        loc.removeOccupant(dv);
        assertTrue(loc.getOccupants().isEmpty());
    }

    @Test
    public void testAddItem() {
        Item item = new PersonalBelonging(10, "Green Bag");
        loc.addItem(item);
        assertThat(loc.getItems().size(), is(1));
        assertThat(loc.getItems().get(0), sameInstance(item));
        assertThat(item.getLocation(), sameInstance(loc));
        assertNull(item.getAllocatedTo());
    }

    @Test
    public void testRemoveItem() {
        Item item = new PersonalBelonging(10, "Green Bag");
        loc.addItem(item);
        loc.removeItem(item);
        assertTrue(loc.getItems().isEmpty());
    }

    @Test
    public void testUpdateLocationNoChanges() {
        try {
            Location.updateLocation(999, "", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateLocationNewName() {
        try {
            Location.updateLocation(10, "NewName", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateLocationNewAddress() {
        try {
            Location.updateLocation(20, "", "SomeNewAddress");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testUpdateLocationBothFields() {
        try {
            Location.updateLocation(30, "BiggerName", "NewStreet 999");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
}
