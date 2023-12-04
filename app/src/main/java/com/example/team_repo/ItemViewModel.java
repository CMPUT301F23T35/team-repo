package com.example.team_repo;

import androidx.lifecycle.ViewModel;


/**
 * ItemViewModel is a ViewModel class that holds and manages UI-related data in a lifecycle-conscious way.
 * This class allows data to survive configuration changes such as screen rotations. It holds an instance
 * of ItemList, which represents a list of items.
 */
public class ItemViewModel extends ViewModel {
    private ItemList itemList;

    /**
     * Constructor for ItemViewModel. Initializes the itemList.
     */
    public ItemViewModel() {
        itemList = new ItemList();
    }

    /**
     * Gets the ItemList managed by this ViewModel.
     *
     * @return The ItemList instance containing the list of items.
     */
    public ItemList getItemList() {
        return itemList;
    }
}
