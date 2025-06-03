package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DisasterVictimTest {
    private DisasterVictim victim;

    @Before
    public void setUp() {
        victim = new DisasterVictim(10, "Alice", "2025-01-01");
    }

    @Test
    public void testConstructorBasicFields() {
        assertThat(victim.getPersonID(), is(10));
        assertThat(victim.getFirstName(), is("Alice"));
        assertThat(victim.getLastName(), is("UnknownLast"));
        assertThat(victim.getPhone(), is("UnknownPhone"));
        assertThat(victim.getEntryDate(), is("2025-01-01"));
    }

    @Test
    public void testConstructorWithDobValid() {
        DisasterVictim v2 = new DisasterVictim(11, "Bob", "2025-02-02", "1990-05-10");
        assertThat(v2.getDateOfBirth(), is("1990-05-10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDobInvalid() {
        new DisasterVictim(12, "Charlie", "2025-03-03", "not-a-date");
    }

    @Test
    public void testAssignedSocialIDIncrements() {
        int firstID = victim.getAssignedSocialID();
        DisasterVictim v2 = new DisasterVictim(13, "Zoe", "2025-01-01");
        int secondID = v2.getAssignedSocialID();
        assertTrue(secondID > firstID);
    }

    @Test
    public void testSetDateOfBirthValid() {
        victim.setDateOfBirth("1980-11-30");
        assertThat(victim.getDateOfBirth(), is("1980-11-30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfBirthInvalid() {
        victim.setDateOfBirth("11/30/1980");
    }

    @Test
    public void testSetGenderEnumValid() {
        victim.setGenderEnum(Gender.WOMAN);
        assertThat(victim.getGenderEnum(), is(Gender.WOMAN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGenderEnumNull() {
        victim.setGenderEnum(null);
    }

    @Test
    public void testMedicalRecordsList() {
        assertTrue(victim.getMedicalRecords().isEmpty());
        MedicalRecord rec = new MedicalRecord(10, null, "Bandaged leg", "2025-05-01");
        victim.addMedicalRecord(rec);
        assertThat(victim.getMedicalRecords().size(), is(1));
        assertThat(victim.getMedicalRecords().get(0), sameInstance(rec));
    }

    @Test
    public void testGetFamilyGroupInitiallyNull() {
        assertNull(victim.getFamilyGroupObj());
    }

    @Test
    public void testSetFamilyGroupObjValid() {
        FamilyGroup fg = new FamilyGroup(100);
        victim.setFamilyGroupObj(fg);
        assertThat(victim.getFamilyGroupObj(), sameInstance(fg));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFamilyGroupObjNull() {
        victim.setFamilyGroupObj(null);
    }

    @Test
    public void testItemsAllocatedInitiallyEmpty() {
        assertTrue(victim.getItemsAllocated().isEmpty());
    }

    @Test
    public void testAllocateItemValid() {
        Item item = new PersonalBelonging(1, "Suitcase");
        victim.allocateItem(item);
        assertThat(victim.getItemsAllocated().size(), is(1));
        assertThat(victim.getItemsAllocated().get(0), sameInstance(item));
        assertThat(item.getAllocatedTo(), sameInstance(victim));
        assertNull(item.getLocation());
    }

    @Test
    public void testAllocateItemRemovesFromLocation() {
        Item c = new Cot(2, "RoomA", "G6");
        victim.allocateItem(c);
        assertThat(c.getAllocatedTo(), sameInstance(victim));
        assertNull(c.getLocation());
        assertThat(victim.getItemsAllocated().contains(c), is(true));
    }

    @Test
    public void testGetEntryDate() {
        assertThat(victim.getEntryDate(), is("2025-01-01"));
    }

    @Test
    public void testCommentsInitiallyNull() {
        assertNull(victim.getComments());
    }

    @Test
    public void testSetComments() {
        victim.setComments("Needs attention");
        assertThat(victim.getComments(), is("Needs attention"));
    }
}
