package com.example.team_repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    private Tag mockTag(){
        Tag tag = new Tag("test");
        return tag;
    }

    @Test
    public void testgetTagString(){
        Tag tag = mockTag();
        assertEquals("test", tag.getTagString());
    }

    @Test
    public void testisSelected(){
        Tag tag = mockTag();
        assertFalse(tag.isSelected());
    }

    @Test
    public void testsetSelected(){
        Tag tag = mockTag();
        tag.setSelected(true);
        assertTrue(tag.isSelected());
    }
}
