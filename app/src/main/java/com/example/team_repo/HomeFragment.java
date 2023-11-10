package com.example.team_repo;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * The fragment for the home page of the app, displaying the user's item list and total estimated value.
 */
public class HomeFragment extends Fragment {

    private ItemAdapter itemAdapter;
    private ItemList item_list;
    private ListView item_list_view;
    private TextView total_value_view;
    private ImageView profile_picture;
    private Calendar calendar;
    private EditText DatePurchase;
    private AddTagAdapter tagAdapter;  // the adapter of the tags
    private LinearLayoutManager layoutManager;
    private ArrayList<Tag> tagList;

    private ArrayList<Tag> selectedTags;


    private ItemViewModel itemViewModel;

    /**
     * Creates the view for the home page fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return the created view for the home page fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        item_list = itemViewModel.getItemList();
        item_list.updateValue();
        item_list.removeNullItem();
        //item_list = new ItemList();


        ItemList add_item_list = ((MainActivity) getActivity()).getAdd_item_list();
        // check add_item_list is empty or not
        Log.d("HomeFragment", "add_item_list: " + add_item_list);
        ((MainActivity) getActivity()).setAdd_item_list(new ItemList());
        item_list.addAll(add_item_list);


        // Attach the items in the item list to the adapter

        item_list_view = view.findViewById(R.id.homepageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList());
        item_list_view.setAdapter(itemAdapter);



        item_list_view = view.findViewById(R.id.homepageListView);
        //item_adapter = new ItemAdapter(this.getContext(), item_list.getList());
        item_list_view.setAdapter(itemAdapter);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList());

        item_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        item_list_view.setAdapter(itemAdapter);
        // Display the total estimated value
        total_value_view = view.findViewById(R.id.totalValueTextView);
        total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));

        // Display profile photo
        profile_picture = view.findViewById(R.id.homepageProfilePicture);
        // check the firebase storage, set the profile picture
        ImageUtils.downloadImageFromFirebaseStorage(((MainActivity) getActivity()).getEmail(), new ImageUtils.OnBitmapReadyListener() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                if (bitmap != null){
                    profile_picture.setImageBitmap(bitmap);
                } else {
                    profile_picture.setImageResource(R.drawable.default_profile_image);
                }
            }
        });

        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpenseInputDialog();
//                monthlyChargeList.total_monthly_charges();
            }
        });

        return view;
    }

    public void editExpenseInputDialog(int editPosition) {
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
                item_list.getList().get(editPosition).setName(name);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!date.isEmpty()) {
                item_list.getList().get(editPosition).setPurchase_date(date);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!item_description.isEmpty()) {
                item_list.getList().get(editPosition).setDescription(item_description);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!make.isEmpty()) {
                item_list.getList().get(editPosition).setMake(make);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!model.isEmpty()) {
                item_list.getList().get(editPosition).setModel(model);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!serial.isEmpty()) {
                item_list.getList().get(editPosition).setSerial_number(serial);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            if (!valueString.isEmpty()) {
                float value = Float.parseFloat(EstimatedValue.getText().toString());
                item_list.getList().get(editPosition).setValue(value);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Refreshes the home page.
     * May be used each time when there is a data change.
     */
    public void refresh() {
        if (isAdded()){
            // update header
            updateProfilePicture();

            // TODO: update item list


            ItemList add_item_list = ((MainActivity) getActivity()).getAdd_item_list();
            ((MainActivity) getActivity()).setAdd_item_list(new ItemList());
            item_list.addAll(add_item_list);
            itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

            total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));
        } else {
            Log.d("HomeFragment", "Not added");
        }
    }

    /**
     * Updates the profile picture, get the picture from MainActivity(bitmap_profile)
     */
    private void updateProfilePicture() {
        // Display profile photo
        Log.d("HomeFragment", "Check null pointer: " + ((MainActivity) getActivity()));
        Bitmap profileBitmap = ((MainActivity) getActivity()).getBitmap_profile();

        if (profileBitmap != null) {
            // not the default profile picture
            profile_picture.setImageBitmap(profileBitmap);

        } else {
            // get the picture fro firebase storage
            ImageUtils.downloadImageFromFirebaseStorage(((MainActivity) getActivity()).getEmail(), new ImageUtils.OnBitmapReadyListener() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    if (bitmap != null){
                        profile_picture.setImageBitmap(bitmap);
                    } else {
                        profile_picture.setImageResource(R.drawable.default_profile_image);
                    }
                }
            });


        }
    }


    public void addExpenseInputDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);

        final EditText ItemName = dialogView.findViewById(R.id.ItemName);
        final EditText Description = dialogView.findViewById(R.id.Description);
        DatePurchase = dialogView.findViewById(R.id.DatePurchase);
        final EditText ItemMake = dialogView.findViewById(R.id.ItemMake);
        final EditText ItemModel = dialogView.findViewById(R.id.ItemModel);
        final EditText ItemSerial = dialogView.findViewById(R.id.ItemSerial);
        final EditText EstimatedValue = dialogView.findViewById(R.id.EstimatedValue);
        final RecyclerView tagRecyclerView = dialogView.findViewById(R.id.tagRecyclerView);

        tagList = ((MainActivity)getActivity()).getTagList();

        tagAdapter = new AddTagAdapter(getContext(), tagList);
        tagRecyclerView.setAdapter(tagAdapter);

        // set the layout manager of the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);

        calendar = Calendar.getInstance();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogView);

        // edit text get focus -> show date picker dialog
        DatePurchase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        // edit text get clicked -> show date picker dialog
        DatePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        dialogBuilder.setPositiveButton("Confirm", (dialog, which) -> {
            String name = ItemName.getText().toString();
            String date = DatePurchase.getText().toString();
            String item_description = Description.getText().toString();
            String make = ItemMake.getText().toString();
            String model = ItemModel.getText().toString();
            String serial = ItemSerial.getText().toString();

            float value;
            if (EstimatedValue.getText().toString().isEmpty()){
                value = (float)0;
            }
            else {
                value = Float.parseFloat(EstimatedValue.getText().toString());
            }

            if (!name.isEmpty()) {
                // Create an item with the received name and other default values or set appropriate values.
                if (!isValidDate(date)) {
                    // TODO: handle empty date
                    if (date.isEmpty()) {
                        date = "0000-00-00";
                    }
                    else {
                        Toast.makeText(getActivity(), "Invalid date format, please use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

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

                item_list.add(newItem);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));
                ((MainActivity) getActivity()).addItemToDB(newItem);

                ItemName.setText("");
                DatePurchase.setText("");
                Description.setText("");
                ItemMake.setText("");
                ItemModel.setText("");
                ItemSerial.setText("");
                EstimatedValue.setText("");

                // give a message to show that the item is added successfully
                Toast.makeText(getActivity(), "Item added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // give a message to show that the item is not added successfully
                Toast.makeText(getActivity(), "Item should have a name", Toast.LENGTH_SHORT).show();
            }


        });

        AlertDialog dialog = dialogBuilder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fragments_rounded_corner, null));
        }
        dialog.show();
    }

    /**
     * Update the date label using the selected date from the date picker dialog
     */
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; // date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        HomeFragment.this.DatePurchase.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Show a date picker dialog
     */
    private void showDatePickerDialog(){

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // month starts from 0
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
        sdf.setLenient(false); // set to false to check date strictly
        try {
            // try to parse the string to date
            Date date = sdf.parse(dateStr);
            return true; // if success, the date is valid
        } catch (ParseException e) {
            // throw exception if the date string is invalid
            return false;
        }
    }


}