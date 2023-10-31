package com.example.TeamRepo;

import java.util.ArrayList;
import java.util.Date;

public class Item {
    // TODO:
    //  - add more constructors when certain arguments aren't given/are optional
    //  - put restrictions on getters/setters (e.g. make sure inputted value isn't negative)
    //  - potentially deal with empty description/comment/make/model/etc.

    private Date purchase_date;
    private float value;
    private String description;
    private String make;
    private String model;
    private String serial_number;
    private String comment;
    private ArrayList<String> tags;
    private Photograph photograph;

    // Constructor (all information provided)
    public Item(Date purchase_date, float value, String description, String make, String model, String serial_number, String comment, ArrayList<String> tags, Photograph photograph) {
        this.purchase_date = purchase_date;
        this.value = value;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serial_number = serial_number;
        this.comment = comment;
        this.tags = tags;
        this.photograph = photograph;
    }

    public Date getDate() {
        return purchase_date;
    }

    public void setDate(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public void setSerialNumber(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Photograph getImage() {
        return photograph;
    }

    public void setImage(Photograph photograph) {
        this.photograph = photograph;
    }
}
