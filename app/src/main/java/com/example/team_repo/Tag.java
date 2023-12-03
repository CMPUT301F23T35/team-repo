package com.example.team_repo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Tag class represents a tag that could be selected.
 */
public class Tag implements Serializable, Comparable<Tag>{
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
     * @return the string of a tag
     */
    public String getTagString(){
        return this.tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    /**
     * Check if a tag is selected
     * @return whether a tag is selected
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

    /**
     * Check if the String of two tags are the same,
     * override to use contains() method in ItemDetailFragment.java
     * @param o the object to be compared
     * @return True if tagString is the same, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagString, tag.tagString);
    }

    /**
     * override to use contains() method in ItemDetailFragment.java
     * @return the hashcode of the tagString
     */
    @Override
    public int hashCode() {
        return Objects.hash(tagString);
    }


    @Override
    public int compareTo(Tag otherTag) {
        // Implement your comparison logic here
        return this.tagString.compareToIgnoreCase(otherTag.tagString);
    }

}
