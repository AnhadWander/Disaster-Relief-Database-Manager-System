package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {

    static class TestPerson extends Person {
        public TestPerson(int personID, String firstName, String lastName, String phone) {
            super(personID, firstName, lastName, phone);
        }
    }

    @Test
    public void testConstructorValid() {
        Person p = new TestPerson(1, "F", "L", "555-1234");
        assertEquals(1, p.getPersonID());
        assertEquals("F", p.getFirstName());
        assertEquals("L", p.getLastName());
        assertEquals("555-1234", p.getPhone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyFirstName() {
        new TestPerson(2, "", "L", "555");
    }

    @Test
    public void testSetFirstNameValid() {
        Person p = new TestPerson(3, "First", "Last", "Ph");
        p.setFirstName("NewFirst");
        assertEquals("NewFirst", p.getFirstName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameEmpty() {
        Person p = new TestPerson(4, "A", "B", "C");
        p.setFirstName("");
    }

    @Test
    public void testSetLastNameValid() {
        Person p = new TestPerson(5, "X", "OldLast", "Y");
        p.setLastName("NewLast");
        assertEquals("NewLast", p.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameEmpty() {
        Person p = new TestPerson(5, "A", "B", "C");
        p.setLastName("");
    }

    @Test
    public void testSetPhoneValid() {
        Person p = new TestPerson(6, "F", "L", "111");
        p.setPhone("222");
        assertEquals("222", p.getPhone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPhoneEmpty() {
        Person p = new TestPerson(7, "A", "B", "C");
        p.setPhone("");
    }

    @Test
    public void testUpdatePersonNoChanges() {
        try {
            Person.updatePerson(999, "", "", "", "", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Did not expect other exceptions here: " + e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePersonInvalidDOB() throws Exception {
        Person.updatePerson(999, "Alice", "", "not-a-date", "", "");
    }

    @Test
    public void testUpdatePersonSomeFields() {
        try {
            Person.updatePerson(999, "UpdatedName", "", "", "", "555-9999");
        } catch (IllegalArgumentException e) {
            fail("Should not fail for valid date or phone: " + e);
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
}
