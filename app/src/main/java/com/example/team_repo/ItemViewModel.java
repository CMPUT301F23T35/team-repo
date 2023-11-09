package com.example.team_repo;

import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private ItemList itemList;

    public ItemViewModel() {
        itemList = new ItemList();
    }

    public ItemList getItemList() {
        return itemList;
    }
}
