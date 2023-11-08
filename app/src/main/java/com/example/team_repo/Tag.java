package com.example.team_repo;

public class Tag {
    private String tagString;

    private boolean isSelected = false;

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
