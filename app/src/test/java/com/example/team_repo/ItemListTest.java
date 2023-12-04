package com.example.team_repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ItemListTest {
    @Test
    public void TestAdd() {

        Item item1 = new Item("MyName1", "2023-11-10", 12.34F, "Description of item", "First make", "First model", "ASDFGHJKL", "My comment");
        Item item2 = new Item("MyName2", "2023-11-11", 10.00F, "Description of item", "Second make", "Second model", "ASDFGHJKL", "My comment");

        // Create empty list, check size/contents/value
        ItemList my_list = new ItemList();
        assertEquals(my_list.getList().size(), 0);
        assertFalse(my_list.getList().contains(item1));
        assertFalse(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 0, 0.01);

        // Add one item to list, check size/contents/value
        my_list.add(item1);
        assertEquals(my_list.getList().size(), 1);
        assertTrue(my_list.getList().contains(item1));
        assertFalse(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 12.34, 0.01);

        // Add another item to list, check size/contents/value
        my_list.add(item2);
        assertEquals(my_list.getList().size(), 2);
        assertTrue(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 22.34, 0.01);

    }

    @Test
    public void TestAddAll() {
        Item item1 = new Item("MyName1", "2023-11-10", 12.34F, "Description of item", "First make", "First model", "ASDFGHJKL", "My comment");
        Item item2 = new Item("MyName2", "2023-11-11", 10.00F, "Description of item", "Second make", "Second model", "ASDFGHJKL", "My comment");
        Item item3 = new Item("MyName3", "2023-11-12", 20.00F, "Description of item", "Third make", "Third model", "ASDFGHJKL", "My comment");

        // Create two item lists to test, check size/contents/value
        ItemList my_list = new ItemList();
        my_list.add(item3);
        ItemList my_second_list = new ItemList();
        my_second_list.add(item1);
        my_second_list.add(item2);
        assertEquals(my_list.getList().size(), 1);
        assertFalse(my_list.getList().contains(item1));
        assertFalse(my_list.getList().contains(item2));
        assertTrue(my_list.getList().contains(item3));
        assertEquals(my_list.getTotalValue(), 20.00, 0.01);
        assertEquals(my_second_list.getList().size(), 2);
        assertTrue(my_second_list.getList().contains(item1));
        assertTrue(my_second_list.getList().contains(item2));
        assertFalse(my_second_list.getList().contains(item3));
        assertEquals(my_second_list.getTotalValue(), 22.34, 0.01);

        // Add second item list to first item list, check size/contents/value
        my_list.addAll(my_second_list);
        assertEquals(my_list.getList().size(), 3);
        assertTrue(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertTrue(my_list.getList().contains(item3));
        assertEquals(my_list.getTotalValue(), 42.34, 0.01);

    }

    @Test
    public void TestRemoveNullItem() {
        Item item1 = new Item("MyName1", "2023-11-10", 12.34F, "Description of item", "First make", "First model", "ASDFGHJKL", "My comment");
        Item item2 = new Item("MyName2", "2023-11-11", 10.00F, "Description of item", "Second make", "Second model", "ASDFGHJKL", "My comment");
        Item item3 = new Item();
        item3.setAllNull();

        // Create new test list
        ItemList my_list = new ItemList();
        my_list.add(item1);
        my_list.add(item2);
        my_list.add(item3);
        assertEquals(my_list.getList().size(), 3);
        assertTrue(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertTrue(my_list.getList().contains(item3));
        assertEquals(my_list.getTotalValue(), 22.34, 0.01);

        // Test if null items get removed from the list
        my_list.removeNullItem();
        assertEquals(my_list.getList().size(), 2);
        assertTrue(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertFalse(my_list.getList().contains(item3));
        assertEquals(my_list.getTotalValue(), 22.34, 0.01);

    }

    @Test
    public void TestRemove() {
        Item item1 = new Item("MyName1", "2023-11-10", 12.34F, "Description of item", "First make", "First model", "ASDFGHJKL", "My comment");
        Item item2 = new Item("MyName2", "2023-11-11", 10.00F, "Description of item", "Second make", "Second model", "ASDFGHJKL", "My comment");

        // Create new item list, check size/contents/value
        ItemList my_list = new ItemList();
        my_list.add(item1);
        my_list.add(item2);
        assertEquals(my_list.getList().size(), 2);
        assertTrue(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 22.34, 0.01);

        // Test removing one item
        my_list.remove(item1);
        assertEquals(my_list.getList().size(), 1);
        assertFalse(my_list.getList().contains(item1));
        assertTrue(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 10.00, 0.01);

        // Test removing the other item
        my_list.remove(item2);
        assertEquals(my_list.getList().size(), 0);
        assertFalse(my_list.getList().contains(item1));
        assertFalse(my_list.getList().contains(item2));
        assertEquals(my_list.getTotalValue(), 0.00, 0.01);
    }

}
