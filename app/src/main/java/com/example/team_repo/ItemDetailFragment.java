package com.example.team_repo;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class ItemDetailFragment extends Fragment {

    private Item mItem;
    private ImageView itemImageView;
    private PhotoUtility photoUtility;

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
        if (mItem.getImage() != null) {
            itemImageView.setImageBitmap(mItem.getImage());
        } else {
            itemImageView.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder drawable resource
        }

        // Set up the toolbar
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    private void deleteImage() {
        // Set the placeholder image and remove the current image data
        itemImageView.setImageResource(R.drawable.baseline_image_not_supported_24); // Placeholder drawable resource
        mItem.setImage(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == PhotoUtility.REQUEST_CODE_CHOOSE && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle gallery image selection
                bitmap = photoUtility.handleImageOnActivityResult(data.getData());
            }
        } else if (requestCode == PhotoUtility.REQUEST_CODE_TAKE && resultCode == getActivity().RESULT_OK) {
            // Handle camera image capture
            bitmap = photoUtility.handleImageOnActivityResult(photoUtility.getImageUri());
        }

        if (bitmap != null) {
            itemImageView.setImageBitmap(bitmap);
            mItem.setImage(bitmap);
        }
    }

    // Other methods can be added here if necessary
}
