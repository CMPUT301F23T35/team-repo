package com.example.team_repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Creates the views for an item list and its items.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> item_list;
    private Context context;

    /**
     * Initializes the item adapter.
     * @param context the current context of the app
     * @param item_list the item list to create the views for
     */
    public ItemAdapter(Context context, ArrayList<Item> item_list) {
        super(context, 0, item_list);
        this.context = context;
        this.item_list = item_list;
    }

    /**
     * Creates the views for each item of the item list
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the created view
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false);
        }

    
        // Get the current item to create the view for
        final Item item = item_list.get(position);

        // Get the TextViews and ImageView from the activity for the item
        ImageView item_image = view.findViewById(R.id.itemImageView);
        TextView item_name = view.findViewById(R.id.itemName);
        TextView item_make = view.findViewById(R.id.itemMake);
        TextView item_value = view.findViewById(R.id.itemValue);
        TextView item_purchase_date = view.findViewById(R.id.itemPurchaseDate);

        // Set the ImageView to the item's photo (or a default image)
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            Bitmap bitmap = ImageUtils.convertImagePathToBitmap(item.getImagePath());
            item_image.setImageBitmap(bitmap);
        } else {
            item_image.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder image
        }

        // Set the item's name, make, value, and purchase date with checks for length
        item_name.setText(item.getName().length() <= 12 ? item.getName() : item.getName().substring(0, 11) + "...");
        item_make.setText(item.getMake().length() <= 15 ? item.getMake() : item.getMake().substring(0, 14) + "...");
        item_value.setText(String.format("%.2f", item.getValue()).length() <= 15 ?
                String.format("%.2f", item.getValue()) :
                String.format("%.2f", item.getValue()).substring(0, 14) + "...");
        item_purchase_date.setText(item.getPurchase_date()); // Assuming getDate() returns a String


        // Set the onClickListener for the entire item view
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming MainActivity has a method called 'showItemDetailFragment'
                // which replaces the current fragment with ItemDetailFragment
                if (context instanceof MainActivity) {
                    ((MainActivity) context).showItemDetailFragment(item);

                }
            }
        });

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
        
        String date = item.getPurchase_date();
        item_purchase_date.setText(date);

        return view;
    }


    public Item getItem(int position) {
        return item_list.get(position);
    }

}
