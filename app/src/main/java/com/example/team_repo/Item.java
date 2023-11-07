package com.example.team_repo;

import android.media.Image;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Item {
    // TODO:
    //  - Add more constructors when certain arguments aren't given/are optional
    //  - Make less tedious constructors??? (less arguments?)
    //  - Put more restrictions on getters/setters (e.g. make sure inputted value isn't negative)
    //  - Come up with method to add a new tag to the tag list???
    //  - Potentially deal with empty description/comment/make/model/etc???

    private String name;
    private String purchase_date;
    private float value;
    private String description;
    private String make;
    private String model;
    private String serial_number;
    private String comment;
    private ArrayList<String> tags;
    private Image image;

    // Constructor (all information provided)
    public Item(String name, String purchase_date, float value, String description, String make, String model, String serial_number, String comment, ArrayList<String> tags, Image image) {
        this.name = name;
        this.purchase_date = purchase_date;
        this.value = value;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serial_number = serial_number;
        this.comment = comment;
        this.tags = tags;
        this.image = image;
    }

    // Constructor (without tags or image)
    public Item(String name, String purchase_date, float value, String description, String make, String model, String serial_number, String comment) {
        this.name = name;
        this.purchase_date = purchase_date;
        this.value = value;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serial_number = serial_number;
        this.comment = comment;
        this.tags = null;
        this.image = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return purchase_date;
    }

    public void setDate(String purchase_date) {
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
