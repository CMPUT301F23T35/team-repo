package com.example.team_repo;


import android.app.DatePickerDialog;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
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


import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collections;
import java.util.Comparator;

import java.util.Date;
import java.util.List;
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

    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

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
        ((MainActivity) getActivity()).setAdd_item_list(new ItemList());
        item_list.addAll(add_item_list);


        // Attach the items in the item list to the adapter

        item_list_view = view.findViewById(R.id.homepageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList(), false);
        item_list_view.setAdapter(itemAdapter);


        item_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        item_list_view.setAdapter(itemAdapter);
        // Display the total estimated value
        total_value_view = view.findViewById(R.id.totalValueTextView);
        total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));

        // Display profile photo
        profile_picture = view.findViewById(R.id.homepageProfilePicture);


        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

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

        profile_picture.setImageResource(R.drawable.default_profile_image);



        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
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

        Button select = view.findViewById(R.id.selectItemsButton);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                intent.putExtra("taglist",((MainActivity)getActivity()).getTagList());
                intent.putExtra("userID", ((MainActivity) getActivity()).getUserId());
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Displays a dialog for sorting items based on different criteria. The dialog contains radio buttons
     * for various sorting options such as sorting by make, description, value, and date. The selected
     * sorting option is then applied to the item list.
     */
    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_sortfilter, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        RadioGroup sortOptions = dialogView.findViewById(R.id.sortOptions);
        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);


        EditText makeFilterEditText = dialogView.findViewById(R.id.makeFilterEditText);
        Button applyFilterButton = dialogView.findViewById(R.id.applyFilterButton);
        Button clearFilterButton = dialogView.findViewById(R.id.clearFilterButton);
        Button filterByDateButton = dialogView.findViewById(R.id.filterByDateButton);


        // Add a listener for the "Sort" button in the dialog
        Button btnSort = dialogView.findViewById(R.id.btnSort);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = sortOptions.getCheckedRadioButtonId();
                // Handle the selected sorting option
                RadioButton radio_button_make = sortOptions.findViewById(R.id.radioMakeAsc);
                RadioButton radio_button_makeDesc = sortOptions.findViewById(R.id.radiomakeDesc);
                RadioButton radio_button_Date = sortOptions.findViewById(R.id.radioDateAsc);
                RadioButton radio_button_DateDesc = sortOptions.findViewById(R.id.radioDateDesc);
                RadioButton radio_button_Value = sortOptions.findViewById(R.id.radioValueAscending);
                RadioButton radio_button_ValueDesc = sortOptions.findViewById(R.id.radioValueDesc);
                RadioButton radio_button_Description = sortOptions.findViewById(R.id.radioDescripAsc);
                RadioButton radio_button_DescriptionDesc = sortOptions.findViewById(R.id.radioDescripDesc);
                RadioButton radio_button_TagAsc = sortOptions.findViewById(R.id.radioTagAsc);
                RadioButton radio_button_TagDesc = sortOptions.findViewById(R.id.radioTagDesc);

                if(selectedId == radio_button_make.getId()){
                handleSorting(radio_button_make.getId(), selectedId,MakeComparator,true);
                dialog.dismiss();
            }   if(selectedId == radio_button_makeDesc.getId()){
                    handleSorting(radio_button_makeDesc.getId(), selectedId,MakeComparator,false);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_Description.getId()){
                    handleSorting(radio_button_Description.getId(), selectedId,DescripComparator,true);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_DescriptionDesc.getId()){
                    handleSorting(radio_button_DescriptionDesc.getId(), selectedId,DescripComparator,false);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_Value.getId()){
                    handleSorting(radio_button_Value.getId(), selectedId,valueComparator,true);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_ValueDesc.getId()){
                    handleSorting(radio_button_ValueDesc.getId(), selectedId,valueComparator,false);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_Date.getId()){
                    handleSorting(radio_button_Date.getId(), selectedId,dateComparator,true);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_DateDesc.getId()){
                    handleSorting(radio_button_DateDesc.getId(), selectedId,dateComparator,false);
                    dialog.dismiss();
                }

                if(selectedId == radio_button_TagDesc.getId()){
                    handleSorting(radio_button_TagDesc.getId(), selectedId,TagComparator,false);
                    dialog.dismiss();
                }
                if(selectedId == radio_button_TagAsc.getId()){
                    handleSorting(radio_button_TagAsc.getId(), selectedId,TagComparator,true);
                    dialog.dismiss();
                }

                }
        });

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyMakeFilter(makeFilterEditText.getText().toString().trim());
                dialog.dismiss();
            }
        });

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyDescriptionFilter(searchEditText.getText().toString().trim());


                dialog.dismiss();
            }
        });

        clearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilter();
                dialog.dismiss();
            }
        });


        filterByDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFilterDialog();
                dialog.dismiss();
            }
        });


        Button filterByTagButton = dialogView.findViewById(R.id.filterByTagButton);
        filterByTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagFilterDialog();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showTagFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_tag_filter, null);
        builder.setView(dialogView);

        // Initialize tag list and adapter
        tagList = ((MainActivity) getActivity()).getTagList();
        tagAdapter = new AddTagAdapter(getContext(), tagList);

        RecyclerView tagRecyclerView = dialogView.findViewById(R.id.tagRecyclerView);
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tagRecyclerView.setAdapter(tagAdapter);

        Button applyTagFilterButton = dialogView.findViewById(R.id.applyTagFilterButton);
        Button clearTagFilterButton = dialogView.findViewById(R.id.clearTagFilterButton);

        AlertDialog tagFilterDialog = builder.create();

        applyTagFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTagFilter();
                tagFilterDialog.dismiss();
            }
        });

        clearTagFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTagFilter();
                tagFilterDialog.dismiss();
            }
        });

        tagFilterDialog.show();
    }

    private void applyTagFilter() {
        selectedTags = tagAdapter.getSelectedTags();
        ArrayList<Item> originalList = item_list.getList();
        ArrayList<Item> filteredList = new ArrayList<>();

        for (Item item : originalList) {
            if (item.getTags().containsAll(selectedTags)) {
                filteredList.add(item);
            }
        }

        itemAdapter.updateItemList(filteredList);
    }

//    private void applyTagFilter() {
//        selectedTags = tagAdapter.getTags();
//        ArrayList<Item> originalList = item_list.getList();
//        ArrayList<Item> filteredList = new ArrayList<>();
//
//        for (Item item : originalList) {
//            if (containsAnySelectedTag(item.getTags(), selectedTags)) {
//                filteredList.add(item);
//            }
//        }
//
//        itemAdapter.updateItemList(filteredList);
//    }
//
//    private boolean containsAnySelectedTag(List<Tag> itemTags, List<Tag> selectedTags) {
//        for (Tag selectedTag : selectedTags) {
//            for (Tag itemTag : itemTags) {
//                if (itemTag.equals(selectedTag)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private void clearTagFilter() {
        // Clear the tag filter and show the original list
        itemAdapter.updateItemList(item_list.getList());
    }

    private void applyMakeFilter(String makeFilter) {
        ArrayList<Item> originalList = item_list.getList();
        ArrayList<Item> filteredList = new ArrayList<>();
        boolean check = false;

        if (!makeFilter.isEmpty()) {
            for (Item item : originalList) {
                if (item.getMake().toLowerCase().contains(makeFilter.toLowerCase())) {
                    filteredList.add(item);
                    check = true;

                }

            }

            if (check != true){

                filteredList.addAll(originalList);
                Toast.makeText(getActivity(), "No such Make exists", Toast.LENGTH_SHORT).show();
//
            }
            itemAdapter.updateItemList(filteredList);
        }
//        } else {
//            // If the filter is empty, show the original list
//            filteredList.addAll(originalList);
//        }

        // Update the adapter with the filtered list

    }


    private void applyDescriptionFilter(String descriptionFilter) {
        ArrayList<Item> originalList = item_list.getList();
        ArrayList<Item> filteredList = new ArrayList<>();
        boolean check = false;

        if (!descriptionFilter.isEmpty()) {
            for (Item item : originalList) {
                if (item.getDescription().toLowerCase().contains(descriptionFilter.toLowerCase())) {
                    filteredList.add(item);
                    check = true;

                }

            }

            if (check != true){

                filteredList.addAll(originalList);
                Toast.makeText(getActivity(), "No such Make exists", Toast.LENGTH_SHORT).show();
//
            }
            itemAdapter.updateItemList(filteredList);
        }
//        } else {
//            // If the filter is empty, show the original list
//            filteredList.addAll(originalList);
//        }

        // Update the adapter with the filtered list

    }


    // Method to show the date filter dialog
// Inside your HomeFragment class
    private void showDateFilterDialog() {
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, month);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Show the end date picker dialog after selecting the start date
                showEndDatePickerDialog();
            }
        };

        // Show the start date picker dialog
        new DatePickerDialog(requireContext(), startDateSetListener,
                startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void showEndDatePickerDialog() {
        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Perform filtering based on the selected date range
                filterItemsByDateRange();
            }
        };

        // Show the end date picker dialog
        new DatePickerDialog(requireContext(), endDateSetListener,
                endDateCalendar.get(Calendar.YEAR),
                endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

//    private void filterItemsByDateRange() {
//        ArrayList<Item> originalList = item_list.getList();
//        ArrayList<Item> filteredList = new ArrayList<>();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//
//        for (Item item : originalList) {
//            try {
//                Date itemDate = sdf.parse(item.getPurchase_date());
//                Date startDate = startDateCalendar.getTime();
//                Date endDate = endDateCalendar.getTime();
//
//                if (itemDate != null && (itemDate.equals(startDate) || itemDate.equals(endDate) ||
//                        (itemDate.after(startDate) && itemDate.before(endDate)))) {
//                    filteredList.add(item);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        itemAdapter.updateItemList(filteredList);
//        // Notify the adapter that the data has changed
//        itemAdapter.notifyDataSetChanged();
//    }

    private void filterItemsByDateRange() {
        ArrayList<Item> originalList = item_list.getList();
        ArrayList<Item> filteredList = new ArrayList<>();

        for (Item item : originalList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Date itemDate = sdf.parse(item.getPurchase_date());
                Date startDate = startDateCalendar.getTime();
                Date endDate = endDateCalendar.getTime();

                Calendar itemCalendar = Calendar.getInstance();
                itemCalendar.setTime(itemDate);

                // Set the time fields to zero to compare only dates
                itemCalendar.set(Calendar.HOUR_OF_DAY, 0);
                itemCalendar.set(Calendar.MINUTE, 0);
                itemCalendar.set(Calendar.SECOND, 0);
                itemCalendar.set(Calendar.MILLISECOND, 0);

                startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                startDateCalendar.set(Calendar.MINUTE, 0);
                startDateCalendar.set(Calendar.SECOND, 0);
                startDateCalendar.set(Calendar.MILLISECOND, 0);

                endDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                endDateCalendar.set(Calendar.MINUTE, 0);
                endDateCalendar.set(Calendar.SECOND, 0);
                endDateCalendar.set(Calendar.MILLISECOND, 0);

                if ((itemCalendar.equals(startDateCalendar) || itemCalendar.equals(endDateCalendar) ||
                        (itemCalendar.after(startDateCalendar) && itemCalendar.before(endDateCalendar)))) {
                    filteredList.add(item);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        itemAdapter.updateItemList(filteredList);
        // Notify the adapter that the data has changed
        itemAdapter.notifyDataSetChanged();
    }


    private void clearFilter() {


        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        // Get the original list from Firebase
        ((MainActivity) getActivity()).getItemListFromDB(new MainActivity.ItemListCallback() {
            @Override
            public void onCallback(ItemList originalItemList) {
                ArrayList<Item> originalList = originalItemList.getList();

                // Update the adapter with the original list
                itemAdapter.updateItemList(originalList);

                // Use 'previousItems' list as needed
                // ...

                // Notify the user or perform any other actions
                Toast.makeText(getActivity(), "Filter cleared", Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Method to handle sorting based on the selected option
    /**
     * Handles sorting of the item list based on the selected sorting option.
     *
     * @param id_radio     The ID of the radio button representing the selected sorting option.
     * @param selectedId   The ID of the currently selected sorting option.
     * @param comp         The comparator used for sorting the item list.
     * @param ascOrDesc    A boolean indicating whether the sorting should be in ascending or descending order.
     */
    @SuppressLint("NonConstantResourceId")
    private void handleSorting(int id_radio, int selectedId, Comparator comp, boolean ascOrDesc) {
        // Implement sorting based on the selected option
//        RadioButton radio_button_20 = id_radio.findViewById(R.id.radioNameAscending);
//        int radio_button_20_id = radio_button_20.getId();
        if (id_radio == selectedId) {
            Context context = getContext(); // Replace with your activity or fragment's context
            CharSequence message = "The List has been Sorted!";
            int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration

            Toast toast = Toast.makeText(context, message, duration);
            toast.show();

            // Sort by item name ascending
            if (!ascOrDesc) {
                Collections.sort(item_list.getList(), Collections.reverseOrder(comp));}
            else{
            Collections.sort(item_list.getList(), comp);}
            itemAdapter.notifyDataSetChanged();
        }
        else{
            Context context = getContext(); // Replace with your activity or fragment's context
            CharSequence message = "Sorting Failed!";
            int duration = Toast.LENGTH_SHORT; // or Toast.LENGTH_LONG for a longer duration

            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
                throw new IllegalStateException("Unexpected value: " + selectedId);
        }
    }

    /**
     * Comparator for sorting items based on their "make" attribute in a case-insensitive manner.
     */
    Comparator<Item> MakeComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getMake().compareToIgnoreCase(item2.getMake());
        }
    };

    /**
     * Comparator for sorting items based on their "description" attribute in a case-insensitive manner.
     */
    Comparator<Item> DescripComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getDescription().compareToIgnoreCase(item2.getDescription());
        }
    };




    Comparator<Item> TagComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            // Assuming getTags() returns a List<String> for tags
            ArrayList<Tag> tags1 = item1.getTags();
            ArrayList<Tag> tags2 = item2.getTags();

            // Compare the tags lexicographically
            int minSize = Math.min(tags1.size(), tags2.size());
            for (int i = 0; i < minSize; i++) {
                int tagComparison = tags1.get(i).compareTo(tags2.get(i));
                if (tagComparison != 0) {
                    return tagComparison;
                }
            }

            // If all common tags are the same, compare based on the number of tags
            return Integer.compare(tags1.size(), tags2.size());
        }
    };


    /**
     * Comparator for sorting items based on their "value" attribute.
     */
    Comparator<Item> valueComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            float value1 = item1.getValue();
            float value2 = item2.getValue();

            // Compare the float values
            if (value1 < value2) {
                return -1;
            } else if (value1 > value2) {
                return 1;
            } else {
                return 0; // values are equal
            }
        }
    };

    /**
     * Comparator for sorting items based on their "purchase date" attribute.
     */
    Comparator<Item> dateComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            String date1 = item1.getPurchase_date();
            String date2 = item2.getPurchase_date();

            // Compare the Date objects
            return date1.compareTo(date2);
        }
    };

    /**
     * Refreshes the home page.
     * May be used each time when there is a data change.
     */
    public void refresh() {
        if (isAdded()){
            // update header
            updateProfilePicture();

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

    /**
     * Display a dialog fragment for adding an item
     */
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

        ImageButton ItemDescriptionCameraButton = dialogView.findViewById(R.id.ItemDescriptionCameraButton);
        ImageButton ItemSerialCameraButton = dialogView.findViewById(R.id.ItemSerialCameraButton);
        ItemDescriptionCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getContext() instanceof MainActivity) {
                    ((MainActivity) dialogView.getContext()).showScanFragment(0, dialog);
                    dialog.dismiss();
                }
            }
        });
        ItemSerialCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getContext() instanceof MainActivity) {
                    ((MainActivity) dialogView.getContext()).showScanFragment(1, dialog);
                    dialog.dismiss();
                }
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        // get the item list from the firebase
        ((MainActivity)getActivity()).getItemListFromDB(new MainActivity.ItemListCallback() {
            @Override
            public void onCallback(ItemList itemList) {
                item_list.clear();
                item_list.addAll(itemList);
                itemAdapter.notifyDataSetChanged(); // notice the adapter that the data has changed
                refresh();
            }
        });
    }

}