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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ScanFragment extends Fragment {
    private PhotoUtility photoUtility;
    private Button cameraButton;  // press to open camera to set photo
    private Button galleryButton;  // select from gallery to set photo
    private Button deleteButton;  // delete photo
    private Button changeButton;  // delete photo
    private ImageView scannedPhoto;
    private EditText scanned_string_textview;
    private String scanned_string;
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
        });

        galleryButton.setOnClickListener(v -> {
            photoUtility.choosePhoto();
        });

        deleteButton.setOnClickListener(v -> {
            photoUtility.deletePhoto(scannedPhoto, R.drawable.baseline_image_not_supported_24);
            scanned_string_textview.setText("Nothing has been scanned yet.");
            scanned_string_textview.setTextColor(Color.RED);
        });

        changeButton = view.findViewById(R.id.btn_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public String getScannedInformation(){
        return scanned_string;
    }

    public void scanSerialNumber(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                scanned_string_textview.setText(text.getText());
                scanned_string_textview.setTextColor(Color.BLACK);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        scanned_string_textview.setText("Unable to scan serial number from image. Try again.");
                        scanned_string_textview.setTextColor(Color.RED);
                    }
                });
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
                    scanSerialNumber(bitmap);
                }
            }
        }
    }
}
