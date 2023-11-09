package com.example.team_repo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< Updated upstream
import android.widget.AdapterView;
=======
import android.widget.Button;
>>>>>>> Stashed changes
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
<<<<<<< Updated upstream
=======
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
>>>>>>> Stashed changes
import java.util.Date;

/**
 * The fragment for the home page of the app, displaying the user's item list and total estimated value.
 */
public class HomeFragment extends Fragment {

    private ItemAdapter itemAdapter;
    private ItemList item_list;
    private ListView item_list_view;
    private TextView total_value_view;
    private ImageView profile_picture;

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

        // Test items (delete later)
//        Calendar cal1 = Calendar.getInstance();
//        cal1.set(2020, 0, 1);
////        Date date1 = cal1.getTime();
//          Item item1 = new Item("Name", "date", 12.34F, "Description", "Make", "Model", "Serial number", "Comment");
//          item_list.add(item1);
//        Item item2 = new Item("Table", date1, 3.01F, "Table", "Table", "Table", "Table", "Table");
//        item_list.add(item2);
//        Item item3 = new Item("ABCDEFGHIJKLMNOPQRSTUVWXYZ", date1, 1234567.89F, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        item_list.add(item3);
//        Item item4 = new Item("frog hat", date1, 0.01F, "This is the best hat ever", "First Edition", "First Model", "1L0V3FR0GH4T5", "");
//        item_list.add(item4);
//        Item item5 = new Item("", date1, 0F, "", "", "", "", "");
//        item_list.add(item5);

        // Attach the items in the item list to the item adapter


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
        profile_picture.setImageResource(R.drawable.default_profile_image);


<<<<<<< Updated upstream
        //view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
//        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addExpenseInputDialog();
////                monthlyChargeList.total_monthly_charges();
//            }
//        });

//        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int editPosition = item_list_view.getCheckedItemPosition();
//                if (editPosition != AdapterView.INVALID_POSITION) {
//                    editExpenseInputDialog(editPosition);
//                    item_list_view.clearChoices();
////                monthlyChargeList.total_monthly_charges();
//                }
//            }
//        });



=======
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpenseInputDialog();
//                monthlyChargeList.total_monthly_charges();
            }
        });
        Button btnSort = view.findViewById(R.id.sortFilterItemsButton);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
>>>>>>> Stashed changes
        return view;
    }

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_sortfilter, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        RadioGroup sortOptions = dialogView.findViewById(R.id.sortOptions);

        // Add a listener for the "Sort" button in the dialog
        Button btnSort = dialogView.findViewById(R.id.btnSort);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = sortOptions.getCheckedRadioButtonId();
                // Handle the selected sorting option
                RadioButton radio_button_20 = sortOptions.findViewById(R.id.radioNameAscending);
                int radio_button_20_id = radio_button_20.getId();
                handleSorting(radio_button_20_id, selectedId);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Method to handle sorting based on the selected option
    @SuppressLint("NonConstantResourceId")
    private void handleSorting(int id_radio, int selectedId) {
        // Implement sorting based on the selected option
//        RadioButton radio_button_20 = id_radio.findViewById(R.id.radioNameAscending);
//        int radio_button_20_id = radio_button_20.getId();
        if (id_radio == selectedId) {
            Context context = getContext(); // Replace with your activity or fragment's context
            CharSequence message = "This is a toast message!";
            int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration

            Toast toast = Toast.makeText(context, message, duration);
            toast.show();

            // Sort by item name ascending
            Collections.sort(item_list.getList(), nameComparator);
            itemAdapter.notifyDataSetChanged();
        }
        else{
                throw new IllegalStateException("Unexpected value: " + selectedId);
        }
    }


    Comparator<Item> nameComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getName().compareTo(item2.getName());
        }
    };



    public void addExpenseInputDialog() {
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
            float value = Float.parseFloat(EstimatedValue.getText().toString());

//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
//            Date date = null;
//            try {
//                date = dateFormat.parse(dateString);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }

            if (name != null && !name.isEmpty()) {
                // Create an item with the received name and other default values or set appropriate values.
//                Calendar cal = Calendar.getInstance();
//                Date date = cal.getTime();

                String pattern = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                Date dateObject = null;
                String date_new = null;
                try {
                    dateObject = dateFormat.parse(date);
                    date_new = String.valueOf(dateObject);
                } catch (ParseException e) {
                    date_new = getString(R.string.no_date_available);
                }
                Item newItem = new Item(name, date_new, value, item_description, make, model, serial, "");
                item_list.add(newItem);
                itemAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                refresh();
            }


        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
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
                item_list.getList().get(editPosition).setDate(date);
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
                item_list.getList().get(editPosition).setSerialNumber(serial);
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
     * Refreshes the home page
     * May be used each time when there is a data change
     */
    public void refresh() {
        // update header
        updateProfilePicture();
        // TODO: update item list

        FragmentActivity current_activity = getActivity();
        current_activity.findViewById(R.id.totalValueTextView);
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
}