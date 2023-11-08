package com.example.team_repo;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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

    private ArrayList<String> selectedTags;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        item_list = new ItemList();

        // Test items (delete later)
//        Calendar cal1 = Calendar.getInstance();
//        cal1.set(2020, 0, 1);
//        Date date1 = cal1.getTime();
//        Item item1 = new Item("Name", date1, 12.34F, "Description", "Make", "Model", "Serial number", "Comment");
//        item_list.add(item1);
//        Item item2 = new Item("Table", date1, 3.01F, "Table", "Table", "Table", "Table", "Table");
//        item_list.add(item2);
//        Item item3 = new Item("ABCDEFGHIJKLMNOPQRSTUVWXYZ", date1, 1234567.89F, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        item_list.add(item3);
//        Item item4 = new Item("frog hat", date1, 0.01F, "This is the best hat ever", "First Edition", "First Model", "1L0V3FR0GH4T5", "");
//        item_list.add(item4);
//        Item item5 = new Item("", date1, 0F, "", "", "", "", "");
//        item_list.add(item5);

        // Attach the items in the item list to the adapter
        item_list_view = view.findViewById(R.id.homepageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList());
        item_list_view.setAdapter(itemAdapter);

        // Display the total estimated value
        total_value_view = view.findViewById(R.id.totalValueTextView);
        total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));

        // Display profile photo
        profile_picture = view.findViewById(R.id.homepageProfilePicture);
        profile_picture.setImageResource(R.drawable.default_profile_image);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpenseInputDialog();
//                monthlyChargeList.total_monthly_charges();
            }
        });

        item_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Item item = itemAdapter.getItem(position);

                checkItem(item);




            }
        });









        return view;
    }

    
    /**
     * Refreshes the home page
     * May be used each time when there is a data change
     */
    public void refresh() {
        // update header
        updateProfilePicture();

        // TODO: update item list

        ItemList add_item_list = ((MainActivity) getActivity()).getAdd_item_list();
        ((MainActivity) getActivity()).setAdd_item_list(new ItemList());
        item_list.addAll(add_item_list);
        itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

        total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));
    }

    /**
     * Updates the profile picture, get the picture from MainActivity(bitmap_profile)
     */
    private void updateProfilePicture() {
        // Display profile photo
        Bitmap profileBitmap = ((MainActivity) getActivity()).getBitmap_profile();

        if (profileBitmap != null) {
            // not the default profile picture
            profile_picture.setImageBitmap(profileBitmap);

        } else {
            // initialized or deleted
            profile_picture.setImageResource(R.drawable.default_profile_image);

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
//        dialogBuilder.setTitle("Add an Item");

        DatePurchase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

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

//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
//            Date date = null;
//            try {
//                date = dateFormat.parse(dateString);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }


            if (!name.isEmpty()) {
                // Create an item with the received name and other default values or set appropriate values.
//                Calendar cal = Calendar.getInstance();
//                Date date = cal.getTime();

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
                // set the selected tags to the item
                selectedTags = new ArrayList<>();
                for (Tag tag : tagList) {
                    if (tag.isSelected()) {
                        selectedTags.add(tag.getTagString());
                    }
                }
                newItem.setTags(selectedTags);

                item_list.add(newItem);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));

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


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; // date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        HomeFragment.this.DatePurchase.setText(sdf.format(calendar.getTime()));
    }

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


    public void checkItem(Item item){
        // Use log to check the item
        Log.d("ListViewClick", "Item Name: " + item.getName());
        Log.d("ListViewClick", "Purchase Date: " + item.getDate());
        Log.d("ListViewClick", "Value: " + item.getValue());
        Log.d("ListViewClick", "Description: " + item.getDescription());
        Log.d("ListViewClick", "Make: " + item.getMake());
        Log.d("ListViewClick", "Model: " + item.getModel());
        Log.d("ListViewClick", "Serial Number: " + item.getSerialNumber());
        Log.d("ListViewClick", "Comment: " + item.getComment());

        if (item.getTags() != null) {
            for (String tag : item.getTags()) {
                Log.d("ListViewClick", "Tag: " + tag);
            }
        }
        Log.d("ListViewClick", "divide-------------------------------divide");

    }


}