package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InquiryTest {

    private Inquiry inquiry;
    private Person inquirer, missing;
    private Location loc;

    @Before
    public void setUp() {
        inquirer = new DisasterVictim(1, "Inq", "2025-01-01");
        missing  = new DisasterVictim(2, "Miss", "2025-01-02");
        loc      = new Location(10, "Shelter", "Addr");
        inquiry  = new Inquiry(100, inquirer, missing, "2025-05-05", "Looking for family", loc);
    }

    @Test
    public void testConstructorFields() {
        assertEquals(100, inquiry.getInquiryID());
        assertEquals("2025-05-05", inquiry.getDateOfInquiry());
        assertEquals("Looking for family", inquiry.getInfoProvided());
        assertSame(inquirer, inquiry.getInquirer());
        assertSame(missing, inquiry.getMissingPerson());
        assertSame(loc, inquiry.getLastKnownLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidDate() {
        new Inquiry(101, inquirer, missing, "bad-date", "info", loc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullInquirer() {
        new Inquiry(102, null, missing, "2025-01-01", "hi", loc);
    }

    @Test
    public void testSetInquirer() {
        Person newInq = new DisasterVictim(3, "NewOne", "2025-02-02");
        inquiry.setInquirer(newInq);
        assertSame(newInq, inquiry.getInquirer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInquirerNull() {
        inquiry.setInquirer(null);
    }

    @Test
    public void testSetMissingPerson() {
        Person newMissing = new DisasterVictim(4, "NewMissing", "2025-03-03");
        inquiry.setMissingPerson(newMissing);
        assertSame(newMissing, inquiry.getMissingPerson());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMissingPersonNull() {
        inquiry.setMissingPerson(null);
    }

    @Test
    public void testSetDateOfInquiryValid() {
        inquiry.setDateOfInquiry("2025-12-31");
        assertEquals("2025-12-31", inquiry.getDateOfInquiry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfInquiryInvalid() {
        inquiry.setDateOfInquiry("not-a-date");
    }

    @Test
    public void testSetInfoProvided() {
        inquiry.setInfoProvided("updated info");
        assertEquals("updated info", inquiry.getInfoProvided());
    }

    @Test
    public void testSetLastKnownLocation() {
        Location newLoc = new Location(11, "Another", "Somewhere");
        inquiry.setLastKnownLocation(newLoc);
        assertSame(newLoc, inquiry.getLastKnownLocation());
    }
}
