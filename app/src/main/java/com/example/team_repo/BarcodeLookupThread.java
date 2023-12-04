package com.example.team_repo;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Creates a thread that takes the raw value of a barcode and looks up the associated product description, if it exists.
 * If the product exists, the getDescription() method will return it.
 * Looking up a barcode description requires internet access.
 * Most of this code comes from the documentation for using the Barcode Look-up API in Java.
 * Credit: <a href="https://www.barcodelookup.com/api-documentation">...</a>
 */
public class BarcodeLookupThread implements Runnable {
    private volatile String scanned_description;    // volatile for accessibility in the main thread
    private String barcode_raw_value;

    /**
     * Constructs a new BarcodeLookupThread with the specified barcode raw value.
     * @param barcode_raw_value The raw value of the barcode to be processed by the thread.
     */
    public BarcodeLookupThread(String barcode_raw_value) {
        this.barcode_raw_value = barcode_raw_value;
    }

    @Override
    public void run() {
        try {
            // Using the specified barcode raw value, look up matching products and obtain a product list.
            // Note: the API only allows a limited number of look-ups per month.
            //       the api_key may need to be replaced if too many look-ups are done.
            String api_key = "tzfbik9nqzz774dqafkdsnmbjeldjs";
            URL url = new URL("https://api.barcodelookup.com/v3/products?barcode=" + barcode_raw_value + "&formatted=y&key=" + api_key);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            StringBuilder data = new StringBuilder();
            while (null != (str= reader.readLine())) {
                data.append(str);
            }
            Gson g = new Gson();
            RootObject value = g.fromJson(data.toString(), RootObject.class);  // value is an object containing a list of Product object

            scanned_description = value.products[0].description;
        }
        // If an exception occurs while trying to obtain the barcode, ignore the error.
        // Doing so will leave scanned_description as a null object.
        catch (Exception ignore) {
        }
    }

    /**
     * Returns a string representing the description that matches the given barcode raw value.
     * String may be null if a description was not obtained.
     * @return scanned_description: the description that matches the given barcode raw value
     */
    public String getDescription() {
        return scanned_description;
    }

    // Class definitions necessary for looking up the barcode from the API and constructing a product list.

    class Store {
        public String name;
        public String price;
        public String link;
        public String currency;
        public String currency_symbol;
    }

    class Review {
        public String name;
        public String rating;
        public String title;
        public String review;
        public String date;
    }

    class Product {
        public String barcode_number;
        public String barcode_formats;
        public String mpn;
        public String model;
        public String asin;
        public String title;
        public String category;
        public String manufacturer;
        public String brand;
        public String[] contributors;
        public String age_group;
        public String ingredients;
        public String nutrition_facts;
        public String color;
        public String format;
        public String multipack;
        public String size;
        public String length;
        public String width;
        public String height;
        public String weight;
        public String release_date;
        public String description;
        public Object[] features;
        public String[] images;
        public Store[] stores;
        public Review[] reviews;
    }

    /**
     * Represents an object containing an array of products.
     */
    class RootObject {
        Product[] products;
    }
}
