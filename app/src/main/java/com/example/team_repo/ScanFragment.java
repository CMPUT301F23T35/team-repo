package com.example.team_repo;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ScanFragment extends Fragment {
    private PhotoUtility photoUtility;
    private Button cameraButton;  // press to open camera to set photo
    private Button galleryButton;  // select from gallery to set photo
    private Button deleteButton;  // delete photo
    private Button changeButton;  // delete photo
    private ImageView scannedPhoto;
    private TextView scanned_string_textview;
    static private int position;

    /**
     * Constructor
     * @param position the position of the clicked icon.
     *                 If position = 0, user clicked to scan for description.
     *                 If position = 1, user clicked to scan for barcode.
     */
    public static ScanFragment newInstance(int position) {
        ScanFragment myFragment = new ScanFragment();
        myFragment.position = position;

        //Bundle args = new Bundle();
        //myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        LinearLayout toolbarLinearLayout = getActivity().findViewById(R.id.select_toolbar);
        int original_visibility = toolbarLinearLayout.getVisibility();
        toolbarLinearLayout.setVisibility(View.GONE);

        Toolbar toolbar = view.findViewById(R.id.item_toolbar);
        //Toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                getActivity().onBackPressed();
            }
        });

        TextView scanImageInstructions = view.findViewById(R.id.scanImageInstructions);
        TextView scannedStringHeader = view.findViewById(R.id.scannedStringHeader);
        if (position == 0) {
            scanImageInstructions.setText(R.string.scan_image_desc_instructions);
            scannedStringHeader.setText(R.string.scanned_item_desc_header);
        }
        else {
            scanImageInstructions.setText(R.string.scan_image_sn_instructions);
            scannedStringHeader.setText(R.string.scanned_item_sn_header);
        }

        scanned_string_textview = view.findViewById(R.id.itemScannedString);
        scanned_string_textview.setText("Nothing has been scanned yet.");
        scanned_string_textview.setTextColor(Color.RED);

        photoUtility = new PhotoUtility(this);

        cameraButton = view.findViewById(R.id.btn_camera);
        galleryButton = view.findViewById(R.id.btn_gallery);
        deleteButton = view.findViewById(R.id.btn_delete);
        scannedPhoto = view.findViewById(R.id.scannedImageView);

        cameraButton.setOnClickListener(v -> {
            photoUtility.takePhoto();
            scanPhoto();
        });

        galleryButton.setOnClickListener(v -> {
            photoUtility.choosePhoto();
            scanPhoto();
        });

        deleteButton.setOnClickListener(v -> {
            photoUtility.deletePhoto(scannedPhoto, R.drawable.baseline_image_not_supported_24);
        });

        changeButton = view.findViewById(R.id.btn_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                sendBackScannedInformation();
            }
        });

        return view;
    }

    public void sendBackScannedInformation(){
        if (position == 1 || position == 3) {
            String description = (String) scanned_string_textview.getText();
        }
        if (position == 2 || position == 3) {
            String serial_number = (String) scanned_string_textview.getText();
        }
        getActivity().onBackPressed();
    }

    public void scanPhoto() {
        scanned_string_textview.setText("Nothing has been scanned yet.");
        scanned_string_textview.setTextColor(Color.RED);
    }

    /**
     * Handles the result of image selection from the gallery or capture from the camera.
     *
     * @param requestCode The request code that was specified when launching the activity.
     * @param resultCode The result code returned by the activity.
     * @param data An Intent that carries the result data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtility.getRequestCodeTake() || requestCode == PhotoUtility.getRequestCodeChoose()) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == PhotoUtility.REQUEST_CODE_CHOOSE && data != null) {
                    photoUtility.setImageUri(data.getData());
                }
                Bitmap bitmap = photoUtility.handleImageOnActivityResult(photoUtility.getImageUri());
                if (bitmap != null) {
                    scannedPhoto.setImageBitmap(bitmap);
                }
            }
        }
    }
}
