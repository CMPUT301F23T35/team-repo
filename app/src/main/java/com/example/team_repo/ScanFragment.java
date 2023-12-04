package com.example.team_repo;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

/**
 * The fragment for scanning a serial number or barcode.
 */
public class ScanFragment extends Fragment {
    private PhotoUtility photo_utility;
    private Button confirm_button;
    private ImageView scanned_photo;
    private EditText scanned_string_textview;

    static private boolean scan_for_description; // true if user is scanning for a description

    // If user called ScanFragment from a dialog, fragment will be null, and vice versa.
    static private AlertDialog previous_dialog = null;
    static private AddFragment addFragment = null;

    /**
     * Constructor when ScanFragment was called from a dialog.
     * @param scan_for_description:  a boolean value representing whether or not a description is to be scanned for
     * @param dialog the dialog that the user used to navigate to the scan fragment.
     */
    public static ScanFragment newInstance(boolean scan_for_description, AlertDialog dialog) {
        ScanFragment myFragment = new ScanFragment();
        myFragment.previous_dialog = dialog;
        myFragment.addFragment = null;
        myFragment.scan_for_description = scan_for_description;

        return myFragment;
    }

    /**
     * Constructor when ScanFragment was called from AddFragment.
     * @param scan_for_description:  a boolean value representing whether or not a description is to be scanned for
     * @param addFragment the AddFragment that the user used to navigate to the scan fragment.
     */
    public static ScanFragment newInstance(boolean scan_for_description, AddFragment addFragment) {
        ScanFragment myFragment = new ScanFragment();
        myFragment.previous_dialog = null;
        myFragment.addFragment = addFragment;
        myFragment.scan_for_description = scan_for_description;

        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // Initialize fragment depending on whether or not user is scanning for a description
        TextView scanImageInstructions = view.findViewById(R.id.scanImageInstructions);
        TextView scannedStringHeader = view.findViewById(R.id.scannedStringHeader);
        if (scan_for_description) {
            scanImageInstructions.setText(R.string.scan_image_desc_instructions);
            scannedStringHeader.setText(R.string.scanned_item_desc_header);
        }
        else {
            scanImageInstructions.setText(R.string.scan_image_sn_instructions);
            scannedStringHeader.setText(R.string.scanned_item_sn_header);
        }

        scanned_string_textview = view.findViewById(R.id.itemScannedString);

        // Keep track of whether or not the toolbar at the top of the screen was visible in the previous fragment.
        // ScanFragment will have the toolbar hidden.
        // When it's time to exit ScanFragment, restore the top toolbar to its original visibility.
        LinearLayout toolbarLinearLayout = requireActivity().findViewById(R.id.select_toolbar);
        int original_visibility = toolbarLinearLayout.getVisibility();
        toolbarLinearLayout.setVisibility(View.GONE);

        // Go back when user clicks the back button of the top toolbar.
        Toolbar toolbar = view.findViewById(R.id.item_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                getActivity().onBackPressed();
                if (previous_dialog != null) {
                    previous_dialog.show();
                }
            }
        });

        // Implement camera features
        photo_utility = new PhotoUtility(this);
        Button camera_button = view.findViewById(R.id.btn_camera);
        Button gallery_button = view.findViewById(R.id.btn_gallery);
        Button delete_button = view.findViewById(R.id.btn_delete);
        scanned_photo = view.findViewById(R.id.scannedImageView);

        camera_button.setOnClickListener(v -> {
            photo_utility.takePhoto();
        });

        gallery_button.setOnClickListener(v -> {
            photo_utility.choosePhoto();
        });

        delete_button.setOnClickListener(v -> {
            photo_utility.deletePhoto(scanned_photo, R.drawable.baseline_image_not_supported_24);
            scanned_string_textview.setText(null);
        });

        // If user confirms that they want to use what they scanned for their item
        confirm_button = view.findViewById(R.id.btn_confirm);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                getActivity().onBackPressed();
                sendInformationBack();
            }
        });
        return view;
    }

    /**
     * Send the scanned information back to the location that ScanFragment was called from.
     */
    private void sendInformationBack() {
        String scanned_string = scanned_string_textview.getText().toString();
        EditText box_to_write_in = null;

        // If ScanFragment was called from a dialog, reopen the dialog and insert new information.
        if (previous_dialog != null) {
            if (scan_for_description) {
                box_to_write_in = previous_dialog.findViewById(R.id.Description);
            }
            else {
                box_to_write_in = previous_dialog.findViewById(R.id.ItemSerial);
            }

            if (scanned_string != null) {
                box_to_write_in.setText(scanned_string);
            }

            previous_dialog.show();
        }
        // If ScanFragment was called from AddFragment, send information to AddFragment.
        else if (addFragment != null) {
            addFragment.setScannedInformation(scan_for_description, scanned_string);
        }
    }

    /**
     * If there was any problem with scanning, show an error.
     */
    private void showError() {
        scanned_string_textview.setText(null);
        Toast.makeText(getContext(), "Unable to scan image. Try again.", Toast.LENGTH_SHORT).show();
    }

    /**
     * If user chose to scan for a description and inputted an image, scan the image for barcode.
     * The barcode raw value will be taken to look up a description of the item if it exists.
     * @param bitmap - the bitmap of the image to be scanned
     */
    private void scanBarcode(Bitmap bitmap) {
        // Disable button while scanning is occurring
        confirm_button.setEnabled(false);

        // Process the image for a barcode
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        BarcodeScanner scanner = BarcodeScanning.getClient();
        scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // If the scanner worked but was not able to find any barcode
                        if (barcodes.size() == 0) {
                            showError();
                        }
                        // If the scanner worked and was able to find a barcode
                        else {
                            Barcode barcode = barcodes.get(0);
                            String raw_value = barcode.getRawValue();

                            try {
                                // Send the raw value to a thread that looks up the barcode description.
                                BarcodeLookupThread lookup_thread = new BarcodeLookupThread(raw_value);
                                Thread thread = new Thread(lookup_thread);
                                thread.start();
                                thread.join();  // do not process anything in the main thread until the lookup thread finishes

                                String scanned_description = lookup_thread.getDescription();
                                scanned_string_textview.setText(scanned_description);
                            }
                            catch (Exception e) {
                                showError();
                            }
                        }
                        confirm_button.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // If the scanner did not work
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError();
                        confirm_button.setEnabled(true);
                    }
                });
    }

    /**
     * If user chose to scan for a serial number and inputted an image, scan the image for text.
     * @param bitmap - the bitmap of the image to be scanned
     */
    private void scanSerialNumber(Bitmap bitmap) {
        // Disable button while scanning is occurring
        confirm_button.setEnabled(false);

        // Process image for text
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        scanned_string_textview.setText(text.getText());
                        confirm_button.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError();
                        confirm_button.setEnabled(true);
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
                    photo_utility.setImageUri(data.getData());
                }
                Bitmap bitmap = photo_utility.handleImageOnActivityResult(photo_utility.getImageUri());
                setScannedImage(bitmap);
            }
        }
    }

    /**
     * Set the scanned image to the given bitmap, and try scanning the image
     * @param bitmap the bitmap of the image
     */
    public void setScannedImage(Bitmap bitmap) {
        if (bitmap != null) {
            scanned_photo.setImageBitmap(bitmap);
            if (scan_for_description) {
                scanBarcode(bitmap);
            }
            else {
                scanSerialNumber(bitmap);
            }
        }
    }
}
