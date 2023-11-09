package com.example.team_repo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
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


        Toolbar toolbar = view.findViewById(R.id.item_toolbar);
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
//        Object dateObject = mItem.getDate(); // Make sure this method exists and returns a Date object or null
//        if (dateObject instanceof Date) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            dateTextView.setText(sdf.format((Date) dateObject));
//        } else {
//            dateTextView.setText(getString(R.string.no_date_available)); // Replace with your string for no available date
//        }

        dateTextView.setText(mItem.getDate());


        valueTextView.setText(String.valueOf(mItem.getValue()));
        descriptionTextView.setText(mItem.getDescription());
        makeTextView.setText(mItem.getMake());
        modelTextView.setText(mItem.getModel());
        serialNumberTextView.setText(mItem.getSerialNumber());
        commentTextView.setText(mItem.getComment());

        // Set a placeholder image from the drawable resources
        imageView.setImageResource(R.drawable.ic_launcher_background);


        //Toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        // TODO: Set up click listeners for edit and delete buttons


        view.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editExpenseInputDialog(nameTextView, dateTextView, descriptionTextView, makeTextView, modelTextView, serialNumberTextView, valueTextView);
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItem.setAllNull();
                // Inside a Fragment or Activity
                Toast.makeText(getContext(), "Item has been deleted, Return to the Home Page", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void editExpenseInputDialog(TextView nameTextView, TextView dateTextView, TextView descriptionTextView, TextView makeTextView, TextView modelTextView, TextView serialNumberTextView, TextView valueTextView) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_add, null);

        final EditText ItemName = dialogView.findViewById(R.id.ItemName);
        final EditText Description = dialogView.findViewById(R.id.Description);
        final EditText DatePurchase = dialogView.findViewById(R.id.DatePurchase);
        final EditText ItemMake = dialogView.findViewById(R.id.ItemMake);

        final EditText ItemModel = dialogView.findViewById(R.id.ItemModel);
        final EditText ItemSerial = dialogView.findViewById(R.id.ItemSerial);
        final EditText EstimatedValue = dialogView.findViewById(R.id.EstimatedValue);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle("Add an Item");

        dialogBuilder.setPositiveButton("Confirm", (dialog, which) -> {
            String name = ItemName.getText().toString();
            String date = DatePurchase.getText().toString();
            String item_description = Description.getText().toString();
            String make = ItemMake.getText().toString();
            String model = ItemModel.getText().toString();
            String serial = ItemSerial.getText().toString();
//            float value = Float.parseFloat(EstimatedValue.getText().toString());
            String valueString = EstimatedValue.getText().toString();

            if (!name.isEmpty()) {
                mItem.setName(name);
                nameTextView.setText(mItem.getName());
            }

            if (!date.isEmpty()) {
                mItem.setDate(date);
                String date_new = mItem.getDate();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                Date dateObject = null;
                try {
                    dateObject = dateFormat.parse(date_new);
                    dateTextView.setText(String.valueOf(dateObject));
                } catch (ParseException e) {
                    dateTextView.setText(getString(R.string.no_date_available));
                }

            }

            if (!item_description.isEmpty()) {
                mItem.setDescription(item_description);
                descriptionTextView.setText((mItem.getDescription()));
            }

            if (!make.isEmpty()) {
                mItem.setMake(make);
                makeTextView.setText((mItem.getDescription()));
            }

            if (!model.isEmpty()) {
                mItem.setModel(model);
                modelTextView.setText((mItem.getDescription()));
            }

            if (!serial.isEmpty()) {
                mItem.setSerialNumber(serial);
                serialNumberTextView.setText(mItem.getSerialNumber());
            }

            if (!valueString.isEmpty()) {

                float value = Float.parseFloat(EstimatedValue.getText().toString());
                mItem.setValue(value);
                valueTextView.setText(String.valueOf(mItem.getValue()));

            }

        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }




}
