package com.example.team_repo;

import java.util.ArrayList;

public class ItemList {
    // TODO:
    //  - Check add() for adding duplicates
    //  - finish delete(), edit(), sort(), filter()

    private ArrayList<Item> item_list;
    private float total_value;

    // Constructor (array list given)
    public ItemList(ArrayList<Item> item_list) {
        this.item_list = item_list;
        this.updateTotal();
    }

    // Constructor (no array list given)
    public ItemList() {
        this.item_list = new ArrayList<Item>();
        this.total_value = 0;
    }

    public ArrayList<Item> getList() {
        return item_list;
    }

    public void setList(ArrayList<Item> item_list) {
        this.item_list = item_list;
    }

    public float getTotal() {
        return total_value;
    }

    // Calculate a total estimated value for all items in the list
    // All methods that edit the item list should call updateTotal() at the end of the method
    private void updateTotal() {
        total_value = 0;
        for (Item item : item_list) {
            total_value = total_value + item.getValue();
        }
    }

    // Add item to list
    public void add(Item item) {
        item_list.add(item);
        this.updateTotal();
    }

    // Delete an item from list
    public void delete(Item item) {
        // TODO
    }

    // Edit an item in list
    public void edit(Item item) {
        // TODO
    }

    // Return a sorted item list, based on some item detail
    public ArrayList<Item> sort() {
        // TODO
        return this.item_list;
    }

    // Return a filtered item list, based on some item detail
    public ArrayList<Item> filter() {
        // TODO
        return this.item_list;
    }
}

