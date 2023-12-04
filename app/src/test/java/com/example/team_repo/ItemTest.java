package com.example.team_repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ItemTest {
    @Test
    public void TestSetAllNull_CheckAllNull() {
        // Create non-null item, check that it is non-null
        Item item = new Item("MyName", "2023-11-10", 12.34F, "Description of item", "First make", "First model", "ASDFGHJKL", "My comment");
        assertFalse(item.checkAllNull());
        assertNotNull(item.getName());
        assertNotNull(item.getPurchase_date());
        assertNotEquals(item.getValue(), 0);
        assertNotNull(item.getDescription());
        assertNotNull(item.getMake());
        assertNotNull(item.getModel());
        assertNotNull(item.getSerial_number());
        assertNotNull(item.getComment());

        // Set all values to null and check if all values are null
        item.setAllNull();
        assertTrue(item.checkAllNull());
        assertNull(item.getName());
        assertNull(item.getPurchase_date());
        assertEquals(item.getValue(), 0, 0.01);
        assertNull(item.getDescription());
        assertNull(item.getMake());
        assertNull(item.getModel());
        assertNull(item.getSerial_number());
        assertNull(item.getComment());
    }
}
