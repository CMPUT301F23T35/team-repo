package com.example.team_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> item_list;
    private Context context;

    public ItemAdapter(Context context, ArrayList<Item> item_list) {
        super(context, 0, item_list);
        this.context = context;
        this.item_list = item_list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false);
        }

        Item item = item_list.get(position);

        // Get the TextViews and ImageView views for the item
        ImageView item_image = view.findViewById(R.id.itemImageView);
        TextView item_name = view.findViewById(R.id.itemName);
        TextView item_make = view.findViewById(R.id.itemMake);
        TextView item_value = view.findViewById(R.id.itemValue);
        TextView item_purchase_date = view.findViewById(R.id.itemPurchaseDate);

        // Set the ImageView to the item's photo (or a default image)
        if (item.getImage() != null) {
            // TODO: display image
            item_image.setImageResource(R.drawable.baseline_image_not_supported_24);
        }
        else {
            item_image.setImageResource(R.drawable.baseline_image_not_supported_24);
        }

        // Set the item's name
        if (item.getName().length() <= 12) {
            item_name.setText(item.getName());
        }
        else {
            item_name.setText(item.getName().substring(0, 11) + "...");
        }

        // Set the item's make
        if (item.getMake().length() <= 15) {
            item_make.setText(item.getMake());
        }
        else {
            item_make.setText(item.getMake().substring(0, 14) + "...");
        }

        // Set the item's value
        if (String.format("%.2f", item.getValue()).length() <= 15) {
            item_value.setText(String.format("%.2f", item.getValue()));
        }
        else {
            item_value.setText(String.format("%.2f", item.getValue()).substring(0, 14) + "...");
        }

        // Set the item's date
        SimpleDateFormat date_format = new SimpleDateFormat("YYYY-MM-DD");
        String date = date_format.format(item.getDate());
        item_purchase_date.setText(date);

        return view;
    }

}
