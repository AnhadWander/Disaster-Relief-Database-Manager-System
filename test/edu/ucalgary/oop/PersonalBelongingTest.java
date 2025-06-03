package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PersonalBelongingTest {

    @Test
    public void testConstructorValid() {
        PersonalBelonging pb = new PersonalBelonging(10, "Green Suitcase");
        assertThat(pb.getItemID(), is(10));
        assertThat(pb.getDescription(), is("Green Suitcase"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyDescription() {
        new PersonalBelonging(1, "");
    }

    @Test
    public void testSetDescriptionValid() {
        PersonalBelonging pb = new PersonalBelonging(2, "Suitcase");
        pb.setDescription("Black Suitcase");
        assertThat(pb.getDescription(), is("Black Suitcase"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionEmpty() {
        PersonalBelonging pb = new PersonalBelonging(3, "Desc");
        pb.setDescription("");
    }
}
