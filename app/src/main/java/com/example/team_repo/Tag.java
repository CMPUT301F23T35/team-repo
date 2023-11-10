package com.example.team_repo;

import java.io.Serializable;

public class Tag implements Serializable {
    private String tagString;
    private boolean isSelected = false;

    /**
     * Empty constructor
     */
    Tag(){

    }

    /**
     * Constructor for a tag with tag string provided
     * @param tag
     */
    Tag(String tag){
        this.tagString  = tag;
    }

    /**
     * Return the string content of a tag
     * @return
     */
    String getTagString(){
        return this.tagString;
    }

    /**
     * Check if a tag is selected
     * @return
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Change a tag's state to selceted
     * @param selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
