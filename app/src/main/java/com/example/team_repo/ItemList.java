package com.example.team_repo;

import java.util.ArrayList;

/**
 * Maintains an item list and the total estimated value for all items in the list.
 */
public class ItemList {

    private ArrayList<Item> item_list;
    private float total_value;

    /**
     * Constructor for a new empty item list.
     */
    public ItemList() {
        this.item_list = new ArrayList<Item>();
        this.total_value = 0;
    }

    /**
     * Adds an item to the item list and updates the total estimated value of the list.
     * @param item the item to be added to the list
     */
    public void add(Item item) {
        item_list.add(item);
        this.total_value = this.total_value + item.getValue();
    }

    /**
     * Removes an item from the item list and updates the total estimated value of the list.
     * @param item the item to be removed from the list
     */
    public void remove(Item item) {
        item_list.remove(item);
        this.total_value = this.total_value - item.getValue();
    }

    /**
     * Returns the item list.
     * @return the item list
     */
    public ArrayList<Item> getList() {
        return item_list;
    }

    /**
     * Returns the total estimated value of the item list.
     * @return the total estimated value of the item list
     */
    public float getTotalValue() {
        return this.total_value;
    }
}

