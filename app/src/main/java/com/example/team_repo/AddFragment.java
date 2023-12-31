package com.example.team_repo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * The AddFragment class extends Fragment. It is used to display a fragment for taking input item information and add item to the items.
 * This fragment is integrated in MainActivity to create a new AddFragment.
 */
public class AddFragment extends Fragment{
    private EditText ItemName;
    private EditText Description;
    private EditText DatePurchase;
    private EditText ItemMake;

    private EditText ItemModel;
    private EditText ItemSerial;
    private EditText EstimatedValue;
    private Button BtnConfirm;
    private RecyclerView tagRecyclerView;  // the recycler view of the tag page
    private Calendar calendar;
    private AddTagAdapter tagAdapter;  // the adapter of the tags
    private LinearLayoutManager layoutManager;
    private ArrayList<Tag> tagList;
    private ArrayList<Tag> selectedTags;

    private ImageButton ItemDescriptionCameraButton;
    private ImageButton ItemSerialCameraButton;

    /**
     * Creates the view for the add page fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return the created view for the add page fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // initialize the views
        ItemName = view.findViewById(R.id.ItemName);
        Description = view.findViewById(R.id.Description);
        DatePurchase = view.findViewById(R.id.DatePurchase);
        ItemMake = view.findViewById(R.id.ItemMake);
        ItemModel = view.findViewById(R.id.ItemModel);
        ItemSerial = view.findViewById(R.id.ItemSerial);
        EstimatedValue = view.findViewById(R.id.EstimatedValue);

        BtnConfirm = view.findViewById(R.id.btn_confirm);
        tagRecyclerView = view.findViewById(R.id.tagRecyclerView);
        tagList = ((MainActivity)getActivity()).getTagList();
        // set all tags to unselected
        for (Tag tag : tagList) {
            tag.setSelected(false);
        }
        tagAdapter = new AddTagAdapter(getContext(), tagList);
        tagRecyclerView.setAdapter(tagAdapter);

        // set the layout manager of the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);

        calendar = Calendar.getInstance();

        // pop a date picker dialog when edit text get focus
        DatePurchase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        // pop a date picker dialog when edit text is clicked
        DatePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        // get input strings, check errors, and add the item to the item list
        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ItemName.getText().toString();
                String date = DatePurchase.getText().toString();
                String item_description = Description.getText().toString();
                String make = ItemMake.getText().toString();
                String model = ItemModel.getText().toString();
                String serial = ItemSerial.getText().toString();
                float value;
                if (EstimatedValue.getText().toString().isEmpty()){
                    // empty value will cause error
                    value = (float)0;
                }
                else {
                    value = Float.parseFloat(EstimatedValue.getText().toString());
                }

                if (!name.isEmpty()) {
                    // Create an item with the received name and other default values or set appropriate values.
                    if (!isValidDate(date)) {
                        if (date.isEmpty()) {
                            // TODO: handle empty date
                            date = "0000-00-00";
                        }
                        else {
                            Toast.makeText(getActivity(), "Invalid date format, please use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // TODO: handle empty description, make, model, serial, value

                    Item newItem = new Item(name, date, value, item_description, make, model, serial, "");

                    // set itemID using time stamp
                    long timestamp = System.currentTimeMillis();
                    newItem.setItemID(String.valueOf(timestamp));


                    // set the selected tags to the item
                    selectedTags = new ArrayList<>();
                    for (Tag tag : tagList) {
                        if (tag.isSelected()) {
                            selectedTags.add(tag);
                        }
                    }
                    newItem.setTags(selectedTags);

                    // give the new item to main activity
                    ItemList add_item_list = ((MainActivity) getActivity()).getAdd_item_list();
                    add_item_list.add(newItem);
                    ((MainActivity) getActivity()).setAdd_item_list(add_item_list);
                    ((MainActivity) getActivity()).addItemToDB(newItem);

                    cleanEt();

                    // successfully add message
                    Toast.makeText(getActivity(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                    // set the tag list to unselected
                    for (Tag tag : tagList) {
                        tag.setSelected(false);
                    }
                    tagAdapter.setTagList(tagList);
                    tagRecyclerView.setAdapter(tagAdapter);
                } else {
                    // error message
                    Toast.makeText(getActivity(), "Item should have a name", Toast.LENGTH_SHORT).show();
                }

            }

        });

        AddFragment currentFragment = this;

        // Create listeners for when the user clicks one of the scan buttons on the fragment
        ItemDescriptionCameraButton = view.findViewById(R.id.ItemDescriptionCameraButton);
        ItemSerialCameraButton = view.findViewById(R.id.ItemSerialCameraButton);
        ItemDescriptionCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getContext() instanceof MainActivity) {
                    ((MainActivity) view.getContext()).showScanFragment(true, currentFragment);
                }
            }
        });

        ItemSerialCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getContext() instanceof MainActivity) {
                    ((MainActivity) view.getContext()).showScanFragment(false, currentFragment);
                }
            }
        });

        return view;
    }

    /**
     * Keep the tag list updated, called in MainActivity
     */
    public void refresh(){
        tagList = ((MainActivity)getActivity()).getTagList();
        tagAdapter.setTagList(tagList);
        tagRecyclerView.setAdapter(tagAdapter);

        // set the layout manager of the recycler view
        tagRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Clean the edit text, called in confirm button on click
     */
    private void cleanEt(){
        ItemName.setText("");
        DatePurchase.setText("");
        Description.setText("");
        ItemMake.setText("");
        ItemModel.setText("");
        ItemSerial.setText("");
        EstimatedValue.setText("");
    }

    /**
     * Update the date label using the selected date from the date picker dialog
     */
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; // date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        AddFragment.this.DatePurchase.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Show a date picker dialog
     */
    private void showDatePickerDialog(){

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(); // update the date label
            }
        };

        // create and show a date picker dialog
        new DatePickerDialog(getActivity(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Check if the date string is valid
     * @param dateStr the date string to be checked
     * @return true: valid; false: invalid
     */
    public boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);  // set to false to check date strictly
        try {
            Date date = sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * From ScanFragment, call this method after the user scans for a serial number or description.
     * This method will input the scanned information into the respective EditText box.
     * @param scan_for_description: a boolean value representing whether or not a description was scanned for
     *                            If null, change nothing.
     * @param text the text that was scanned
     */
    public void setScannedInformation(boolean scan_for_description, String text) {
        if (text != null) {
            if (scan_for_description) {
                Description.setText(text);
            } else {
                ItemSerial.setText(text);
            }
        }
    }

}