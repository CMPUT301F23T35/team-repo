package com.example.team_repo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemDetailFragment extends Fragment {

    private Item mItem;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item); // Make sure Item implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (Item) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        TextView nameTextView = view.findViewById(R.id.itemNameTextView);
        TextView dateTextView = view.findViewById(R.id.itemDateTextView);
        TextView valueTextView = view.findViewById(R.id.itemValueTextView);
        TextView descriptionTextView = view.findViewById(R.id.itemDescriptionTextView);
        TextView makeTextView = view.findViewById(R.id.itemMakeTextView);
        TextView modelTextView = view.findViewById(R.id.itemModelTextView);
        TextView serialNumberTextView = view.findViewById(R.id.itemSerialNumberTextView);
        TextView commentTextView = view.findViewById(R.id.itemCommentTextView);
        ImageView imageView = view.findViewById(R.id.itemImageView);

        // Set the item details in the TextViews and ImageView
        nameTextView.setText(mItem.getName());

        // Check if the date is available and valid before formatting
        Object dateObject = mItem.getDate(); // Make sure this method exists and returns a Date object or null
        if (dateObject instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateTextView.setText(sdf.format((Date) dateObject));
        } else {
            dateTextView.setText(getString(R.string.no_date_available)); // Replace with your string for no available date
        }


        valueTextView.setText(String.valueOf(mItem.getValue()));
        descriptionTextView.setText(mItem.getDescription());
        makeTextView.setText(mItem.getMake());
        modelTextView.setText(mItem.getModel());
        serialNumberTextView.setText(mItem.getSerialNumber());
        commentTextView.setText(mItem.getComment());

        // Set a placeholder image from the drawable resources
        imageView.setImageResource(R.drawable.ic_launcher_background);

        // TODO: Set up click listeners for edit and delete buttons

        return view;
    }
}
