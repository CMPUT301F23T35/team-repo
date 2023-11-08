package com.example.team_repo;

import java.util.ArrayList;

public class ItemList {

    private ArrayList<Item> item_list;
    private float total_value;

    public ItemList() {
        this.item_list = new ArrayList<Item>();
        this.total_value = 0;
    }

    // Add item to list
    public void add(Item item) {
        item_list.add(item);
        this.total_value = this.total_value + item.getValue();
    }

    public void addAll(ItemList itemList) {
        for (Item item : itemList.getList()) {
            item_list.add(item);
            this.total_value = this.total_value + item.getValue();
        }
    }

    // Remove item from list
    public void remove(Item item) {
        item_list.remove(item);
        this.total_value = this.total_value - item.getValue();
    }

    public ArrayList<Item> getList() {
        return item_list;
    }

    public void setList(ArrayList<Item> item_list) {
        this.item_list = item_list;
    }

    public float getTotalValue() {
        return this.total_value;
    }
}

