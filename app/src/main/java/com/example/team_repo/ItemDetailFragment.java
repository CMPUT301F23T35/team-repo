package com.example.team_repo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemDetailFragment extends Fragment {

    private Item mItem;
    private ImageView itemImageView;
    private PhotoUtility photoUtility;
    private OnItemUpdatedListener updateListener;
    public ItemDetailFragment() {
        // Required empty public constructor
    }

    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item); // Ensure Item implements Serializable
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
        itemImageView = view.findViewById(R.id.itemImageView);
        photoUtility = new PhotoUtility(this);

        // Setup listeners for image update buttons
        view.findViewById(R.id.editImageButton).setOnClickListener(v -> photoUtility.choosePhoto());
        view.findViewById(R.id.takePhotoButton).setOnClickListener(v -> photoUtility.takePhoto());
        view.findViewById(R.id.deleteImageButton).setOnClickListener(v -> deleteImage());

        // Populate the views with item data
        nameTextView.setText(mItem.getName());
        dateTextView.setText(mItem.getDate());
        valueTextView.setText(String.valueOf(mItem.getValue()));
        descriptionTextView.setText(mItem.getDescription());
        makeTextView.setText(mItem.getMake());
        modelTextView.setText(mItem.getModel());
        serialNumberTextView.setText(mItem.getSerialNumber());
        commentTextView.setText(mItem.getComment());

        // Set the image if available, otherwise set a placeholder
        if (mItem.getImagePath() != null && !mItem.getImagePath().isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(mItem.getImagePath());
            itemImageView.setImageBitmap(bitmap);
        } else {
            // Set a default image if the path is null or empty
            itemImageView.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder drawable resource
        }

        // Set up the toolbar
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        return view;

    }

    private void deleteImage() {
        // Set the placeholder image and remove the current image path
        itemImageView.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder drawable resource
        mItem.setImagePath(null); // Clear the image path
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (requestCode == PhotoUtility.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle gallery image selection
                Uri selectedImageUri = data.getData();
                bitmap = photoUtility.handleImageOnActivityResult(selectedImageUri);
            }
        } else if (requestCode == PhotoUtility.REQUEST_CODE_TAKE && resultCode == Activity.RESULT_OK) {
            // Handle camera image capture
            Uri capturedImageUri = photoUtility.getImageUri();
            bitmap = photoUtility.handleImageOnActivityResult(capturedImageUri);
        }

        if (bitmap != null) {
            itemImageView.setImageBitmap(bitmap);

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


    public interface OnItemUpdatedListener {
        void onItemUpdated(Item item);
    }
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


    // Call this method when the item is updated
    private void notifyItemUpdated(Item item) {
        if (updateListener != null) {
            updateListener.onItemUpdated(item);
        }
    }


    // Other methods can be added here if necessary
    }
