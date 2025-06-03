package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MedicalRecordTest {

    @Test
    public void testConstructorValid() {
        Location loc = new Location(5, "Clinic", "123");
        MedicalRecord mr = new MedicalRecord(10, loc, "Treatment", "2025-01-02");
        assertThat(mr.getRecordID(), is(10));
        assertThat(mr.getLocation(), sameInstance(loc));
        assertThat(mr.getTreatmentDetails(), is("Treatment"));
        assertThat(mr.getDateOfTreatment(), is("2025-01-02"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyTreatmentDetails() {
        new MedicalRecord(1, null, "", "2025-01-01");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidDate() {
        new MedicalRecord(2, null, "Something", "invalid");
    }

    @Test
    public void testSetLocation() {
        MedicalRecord mr = new MedicalRecord(3, null, "Med", "2025-01-01");
        Location loc = new Location(10, "Place", "Addr");
        mr.setLocation(loc);
        assertThat(mr.getLocation(), sameInstance(loc));
    }

    @Test
    public void testSetTreatmentDetailsValid() {
        MedicalRecord mr = new MedicalRecord(4, null, "abc", "2025-01-02");
        mr.setTreatmentDetails("NewDetails");
        assertThat(mr.getTreatmentDetails(), is("NewDetails"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTreatmentDetailsEmpty() {
        MedicalRecord mr = new MedicalRecord(5, null, "abc", "2025-01-02");
        mr.setTreatmentDetails("");
    }

    @Test
    public void testSetDateOfTreatmentValid() {
        MedicalRecord mr = new MedicalRecord(6, null, "abc", "2025-01-02");
        mr.setDateOfTreatment("2030-12-31");
        assertThat(mr.getDateOfTreatment(), is("2030-12-31"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfTreatmentInvalid() {
        MedicalRecord mr = new MedicalRecord(7, null, "xyz", "2025-01-01");
        mr.setDateOfTreatment("31/12/2030");
    }

    @Test
    public void testUpdateMedicalRecordNoChanges() {
        try {
            MedicalRecord.updateMedicalRecord(123, "", "", "");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateMedicalRecordInvalidDate() throws Exception {
        MedicalRecord.updateMedicalRecord(123, "20", "invalid-date", "Some details");
    }

    @Test
    public void testUpdateMedicalRecordSomeFields() {
        try {
            MedicalRecord.updateMedicalRecord(99, "10", "2025-12-01", "Updated record");
        } catch (IllegalArgumentException e) {
            fail("Should not fail for a valid date: " + e);
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
}
