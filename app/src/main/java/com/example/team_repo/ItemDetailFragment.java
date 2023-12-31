package com.example.team_repo;


import android.app.DatePickerDialog;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The ItemDetailFragment class is a Fragment responsible for displaying and managing
 * the details of an item. It provides functionalities such as editing item details,
 * handling image selections, and interacting with Firebase for image storage.
 */
public class ItemDetailFragment extends Fragment {

    private Item mItem;
    private EditText DatePurchase;
    private Calendar calendar;
    private ArrayList<Tag> tagList;
    private ArrayList<Tag> detailTagList;
    private ArrayList<Tag> selectedTags;
    private RecyclerView.LayoutManager layoutManager;
    private AddTagAdapter tagAdapter;
    private DetailTagAdapter detailTagAdapter;

    // private ImageView itemImageView;
    private ViewPager imageViewPager;
    private PhotoUtility photoUtility;
    private OnItemUpdatedListener updateListener;
    private RecyclerView tagRecyclerView;
    private List<ImageView> imageViewList;
    private List<String> imageNameList;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean noImage = true;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Empty public constuctor
     */
    public ItemDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor with given an item
     * @param item item to be displayed
     * @return an item detail fragment
     */
    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item); // Ensure Item implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Receive item from bundle
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (Item) getArguments().getSerializable("item");

        }
    }


    /**
     * Creates the view for the Item Details page fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return the created view for the Item Details page fragment
     */
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

        detailTagList = mItem.getTags();

        detailTagAdapter = new DetailTagAdapter(getContext(), detailTagList);
        tagRecyclerView = view.findViewById(R.id.detail_tags_recycler_view);
        tagRecyclerView.setAdapter(detailTagAdapter);

        // set the layout manager of the recycler view
        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);

//        itemImageView = view.findViewById(R.id.itemImageView);
        photoUtility = new PhotoUtility(this);

        // Setup listeners for image update buttons
        view.findViewById(R.id.editImageButton).setOnClickListener(v -> photoUtility.choosePhoto());
        view.findViewById(R.id.takePhotoButton).setOnClickListener(v -> photoUtility.takePhoto());
        view.findViewById(R.id.deleteImageButton).setOnClickListener(v -> deleteImage());

        // Populate the views with item data
        nameTextView.setText(mItem.getName());
        dateTextView.setText("Date: "+ mItem.getPurchase_date());
        valueTextView.setText("Value: " + String.valueOf(mItem.getValue()));
        descriptionTextView.setText("Desc: " + mItem.getDescription());
        makeTextView.setText("Make: " + mItem.getMake());
        modelTextView.setText("Model: " + mItem.getModel());
        serialNumberTextView.setText("Serial: " + mItem.getSerial_number());

        //Toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        // Set the image if available, otherwise set a placeholder
        imageViewPager = view.findViewById(R.id.vp_itemImage);
        imageViewList = new ArrayList<>();


        // download image from firebase storage
        ImageUtils.downloadItemImages(mItem.getItemID(), new ImageUtils.OnBitmapsReadyListener() {

            @Override
            public void onBitmapsReady(List<Bitmap> bitmaps, List<String> imageNames) {
                // check if getActivity() is null
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        // UI changes can only be done in the main (UI) thread (downloading images is done in a background thread)
                        @Override
                        public void run() {
                            imageNameList = imageNames;
                            Context context = getContext(); // check if current context is alive
                            if (context != null) {
                                if (bitmaps.isEmpty()) {
                                    ImageView imageView = new ImageView(context);
                                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                                    imageViewList.add(imageView);
                                } else {
                                    for (Bitmap bitmap : bitmaps) {
                                        ImageView imageView = new ImageView(context);
                                        imageView.setImageBitmap(bitmap);
                                        imageViewList.add(imageView);
                                    }
                                    noImage = false;
                                }

                                viewPagerAdapter = new ViewPagerAdapter(imageViewList);
                                imageViewPager.setAdapter(viewPagerAdapter);
                            }
                        }
                    });
                }
            }
        });



        view.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editExpenseInputDialog(nameTextView, dateTextView, descriptionTextView, makeTextView, modelTextView, serialNumberTextView, valueTextView);
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if getActivity() is null
                if (getActivity() == null) {
                    return;
                }
                mItem.setAllNull();
                // delete from database
                ((MainActivity) getActivity()).deleteItemFromDB(mItem);
                // Inside a Fragment or Activity
                Toast.makeText(getContext(), "Item has been deleted", Toast.LENGTH_SHORT).show();
                // pause for 700ms
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().onBackPressed();  // return to the home page
            }
        });

        return view;

    }

    /**
     * Deletes the image associated with the current item. This method sets the placeholder image,
     * removes the current image path from the item, and deletes the image from Firebase Storage.
     */
    private void deleteImage() {
        // Set the placeholder image and remove the current image path
        // itemImageView.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder drawable resource
        mItem.setImagePath(null); // Clear the image path
        if (imageViewList.size() == 0 || noImage){
            Toast.makeText(getContext(), "No image to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentIndex = imageViewPager.getCurrentItem();
        imageViewList.remove(currentIndex); // remove current image
        viewPagerAdapter.notifyDataSetChanged();

        if (imageViewList.size() > 0) {
            // not empty after removing
            if (currentIndex == imageViewList.size()) {
                // if the last image is removed, set the current item to the previous image
                imageViewPager.setCurrentItem(currentIndex - 1, true);
            } else {
                imageViewPager.setCurrentItem(currentIndex, true);
            }

        } else {
            // empty after removing
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            imageViewList.add(imageView);
            viewPagerAdapter.notifyDataSetChanged();
            imageViewPager.setCurrentItem(0);
            noImage = true;
        }

        // delete image from firebase storage
        // Create a storage reference from our app
        StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("images/" + mItem.getItemID() + "/" + imageNameList.get(currentIndex));

        // delete the file
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // successfully deleted
                Toast.makeText(getContext(), "Image " + (currentIndex+1) + " deleted", Toast.LENGTH_SHORT).show();
                imageNameList.remove(currentIndex);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // failed to delete
                Toast.makeText(getContext(), "Failed to delete image " + (currentIndex+1), Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * Handles the result of image selection from the gallery or capture from the camera.
     *
     * @param requestCode The request code that was specified when launching the activity.
     * @param resultCode  The result code returned by the activity.
     * @param data        An Intent that carries the result data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (requestCode == PhotoUtility.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle gallery image selection
                Uri selectedImageUri = data.getData();
                bitmap = photoUtility.handleImageOnActivityResult(selectedImageUri);

                // save the bitmap to firebase storage
                // ImageUtils.uploadImageToFirebaseStorage(bitmap, mItem.getItemID());
                String currentTime = String.valueOf(System.currentTimeMillis());
                String newFileName = mItem.getItemID() + "/" + currentTime;
                ImageUtils.uploadImageToFirebaseStorage(bitmap, newFileName);
                imageNameList.add(currentTime + ".jpg");
                Toast.makeText(getContext(), "New image added.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PhotoUtility.REQUEST_CODE_TAKE && resultCode == Activity.RESULT_OK) {
            // Handle camera image capture
            Uri capturedImageUri = photoUtility.getImageUri();
            bitmap = photoUtility.handleImageOnActivityResult(capturedImageUri);

            // save the bitmap to firebase storage
            // ImageUtils.uploadImageToFirebaseStorage(bitmap, mItem.getItemID());
            // generate a unique file name using current time stamp
            String currentTime = String.valueOf(System.currentTimeMillis());
            String newFileName = mItem.getItemID() + "/" + currentTime;
            imageNameList.add(currentTime + ".jpg");
            ImageUtils.uploadImageToFirebaseStorage(bitmap, newFileName);
            Toast.makeText(getContext(), "New image added.", Toast.LENGTH_SHORT).show();
        }

        if (bitmap != null) {
            // itemImageView.setImageBitmap(bitmap);
            // add the new image to the view pager
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bitmap);

            // if there is no image before, remove the placeholder image
            if (noImage) {
                imageViewList.remove(0);
                noImage = false;
            }

            imageViewList.add(imageView);
            viewPagerAdapter.notifyDataSetChanged();

            int position = imageViewList.size() - 1;
            imageViewPager.setCurrentItem(position);

            // Create a unique filename based on the current timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timestamp + "_";

            // Save the bitmap to a file and get the path using ImageUtils
            String imagePath = ImageUtils.saveBitmapToFile(requireContext(), bitmap, imageFileName);

            if (imagePath != null) {
                // Store the image path in the item
                mItem.setImagePath(imagePath);
            }
        }
    }



    /**
     * Interface definition for a callback to be invoked when an item is updated.
     * Implementing classes or objects should handle the {@code onItemUpdated} method
     * to receive notifications about item updates.
     */
    public interface OnItemUpdatedListener {
        void onItemUpdated(Item item);
    }


    /**
     * Called when the fragment is attached to the context. This method is typically used to
     * initialize the fragment and establish communication with the hosting activity.
     *
     * @param context The context to which the fragment is attached.
     * @throws ClassCastException If the hosting context does not implement the required
     *                            {@link OnItemUpdatedListener} interface.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            updateListener = (OnItemUpdatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemUpdatedListener");
        }
    }


    /**
     * Notifies the registered update listener that an item has been updated.
     * If an update listener is registered, the {@code onItemUpdated} method of the listener
     * is called, providing the updated item as a parameter.
     *
     * @param item The item that has been updated and is being notified to the update listener.
     */
    private void notifyItemUpdated(Item item) {
        if (updateListener != null) {
            updateListener.onItemUpdated(item);
        }
    }


    /**
     * Displays an AlertDialog for editing item details. This method populates the AlertDialog
     * with the existing data of the provided item and allows the user to modify and confirm the changes.
     *
     * @param nameTextView          TextView for displaying the item name.
     * @param dateTextView          TextView for displaying the purchase date of the item.
     * @param descriptionTextView   TextView for displaying the description of the item.
     * @param makeTextView          TextView for displaying the make of the item.
     * @param modelTextView         TextView for displaying the model of the item.
     * @param serialNumberTextView  TextView for displaying the serial number of the item.
     * @param valueTextView         TextView for displaying the estimated value of the item.
     */
    public void editExpenseInputDialog(TextView nameTextView, TextView dateTextView, TextView descriptionTextView, TextView makeTextView, TextView modelTextView, TextView serialNumberTextView, TextView valueTextView) {
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

        // Populate the views with item data
        ItemName.setText(mItem.getName());
        Description.setText(mItem.getDescription());
        DatePurchase.setText(mItem.getPurchase_date());
        ItemMake.setText(mItem.getMake());
        ItemModel.setText(mItem.getModel());
        ItemSerial.setText(mItem.getSerial_number());
        EstimatedValue.setText(String.valueOf(mItem.getValue()));
        tagList = ((MainActivity) getActivity()).getTagList();

        // set "selected" status of tags according to the item
        for (Tag tag : tagList) {
            tag.setSelected(mItem.getTags().contains(tag));
        }
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
                    showDatePickerDialog(DatePurchase);
                    // showDatePickerDialog(dateTextView);
                }
            }
        });

        // edit text get clicked -> show date picker dialog
        DatePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(DatePurchase);
                // showDatePickerDialog(dateTextView);

            }
        });

        dialogBuilder.setPositiveButton("Confirm", (dialog, which) -> {
            String name = ItemName.getText().toString();
            String date = DatePurchase.getText().toString();
            String item_description = Description.getText().toString();
            String make = ItemMake.getText().toString();
            String model = ItemModel.getText().toString();
            String serial = ItemSerial.getText().toString();
            String valueString = EstimatedValue.getText().toString();


                mItem.setName(name);
                nameTextView.setText(mItem.getName());

                if (!item_description.isEmpty()) {
                    mItem.setDescription(item_description);
                    descriptionTextView.setText("Desc: " + (mItem.getDescription()));
                }

                if (!make.isEmpty()) {
                    mItem.setMake(make);
                    makeTextView.setText("Make: " + (mItem.getMake()));
                }

                if (!model.isEmpty()) {
                    mItem.setModel(model);
                    modelTextView.setText("Model: " + (mItem.getModel()));
                }

                if (!serial.isEmpty()) {
                    mItem.setSerial_number(serial);
                    serialNumberTextView.setText("Serial: " + mItem.getSerial_number());
                }

                // Create an item with the received name and other default values or set appropriate values.
                if (!isValidDate(date)) {
                    // TODO: handle invalid date
                    mItem.setPurchase_date("0000-00-00");
                    dateTextView.setText("Date: " + mItem.getPurchase_date());
                } else {
                    mItem.setPurchase_date(date);
                    dateTextView.setText("Date: " + mItem.getPurchase_date());
                }

                float value;
                if (valueString.isEmpty()) {
                    value = (float) 0;
                } else {
                    value = Float.parseFloat(EstimatedValue.getText().toString());
                }
                valueTextView.setText("Value: " + String.valueOf(value));
                mItem.setValue(value);

                // set the selected tags to the item
                selectedTags = new ArrayList<>();
                for (Tag tag : tagList) {
                    if (tag.isSelected()) {
                        selectedTags.add(tag);
                    }
                }
                mItem.setTags(selectedTags);
                // refresh the tag recycler view
                detailTagList.clear();
                detailTagList.addAll(selectedTags);
                detailTagAdapter.notifyDataSetChanged();

                ((MainActivity) getActivity()).updateItemToDB(mItem);


                ItemName.setText("");
                DatePurchase.setText("");
                Description.setText("");
                ItemMake.setText("");
                ItemModel.setText("");
                ItemSerial.setText("");
                EstimatedValue.setText("");

                // give a message to show that the item is added successfully
                Toast.makeText(getActivity(), "Item changed successfully!", Toast.LENGTH_SHORT).show();


        });
        AlertDialog dialog = dialogBuilder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fragments_rounded_corner, null));
        }
        dialog.show();

        // Create listeners for when the user clicks one of the scan buttons on the dialog
        ImageButton ItemDescriptionCameraButton = dialogView.findViewById(R.id.ItemDescriptionCameraButton);
        ImageButton ItemSerialCameraButton = dialogView.findViewById(R.id.ItemSerialCameraButton);
        ItemDescriptionCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getContext() instanceof MainActivity) {
                    ((MainActivity) dialogView.getContext()).showScanFragment(true, dialog);
                    dialog.dismiss();
                }
            }
        });

        ItemSerialCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getContext() instanceof MainActivity) {
                    ((MainActivity) dialogView.getContext()).showScanFragment(false, dialog);
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * Update the date label using the selected date from the date picker dialog
     * @param DatePurchase  TextView for displaying the purchase date of the item.
     */
    private void updateLabel(TextView DatePurchase) {
        String myFormat = "yyyy-MM-dd"; // date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DatePurchase.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Show a date picker dialog
     * @param DatePurchase  TextView for displaying the purchase date of the item.
     */
    private void showDatePickerDialog(TextView DatePurchase){

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // month starts from 0
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(DatePurchase); // update the date label
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

        // Other methods can be added here if necessary

}

