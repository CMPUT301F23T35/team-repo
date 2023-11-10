package com.example.team_repo;

import java.io.Serializable;

public class Tag implements Serializable {
    private String tagString;

    private boolean isSelected = false;

    Tag(){

    }
    Tag(String tag){
        this.tagString  = tag;
    }

    String getTagString(){
        return this.tagString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
