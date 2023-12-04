package com.example.team_repo;

import android.os.AsyncTask;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class BarcodeSearchThread implements Runnable {
    private volatile String scanned_description;
    private String barcode_raw_value;

    /**
     * Constructs a new BarcodeSearchThread with the specified barcode raw value.
     *
     * @param barcode_raw_value The raw value of the barcode to be processed by the thread.
     */
    public BarcodeSearchThread(String barcode_raw_value) {
        this.barcode_raw_value = barcode_raw_value;
    }

    @Override
    public void run() {
        try {
            String api_key = "tzfbik9nqzz774dqafkdsnmbjeldjs";

            URL url = new URL("https://api.barcodelookup.com/v3/products?barcode=" + barcode_raw_value + "&formatted=y&key=" + api_key);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = "";
            String data = "";
            while (null != (str= br.readLine())) {
                data+=str;
            }

            Gson g = new Gson();

            RootObject value = g.fromJson(data, RootObject.class);

            scanned_description = value.products[0].description;
        } catch (Exception ignore) {
        }
    }

    public String getDescription() {
        return scanned_description;
    }

    /**
     * make comment later but make sure to credit the API
     * @param
     * @return
     */
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
     * Represents a root object containing an array of products.
     */
    class RootObject {
        Product[] products;
    }
}
