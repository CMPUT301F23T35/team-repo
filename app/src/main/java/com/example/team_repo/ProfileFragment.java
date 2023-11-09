package com.example.team_repo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private ImageView profilePagePicture;  // avatar
    private Button cameraButton;  // press to open camera to set avatar
    private Button galleryButton;  // select from gallery to set avatar
    private Button deleteButton;  // set avatar to default
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button saveChanges;  // press to save changes

    private PhotoUtility photoUtility;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // initialize the components
        mUsername = view.findViewById(R.id.profile_username);
        mEmail = view.findViewById(R.id.profile_email);
        mPassword = view.findViewById(R.id.profile_password);
        saveChanges = view.findViewById(R.id.btn_save_changes);
        profilePagePicture = view.findViewById(R.id.profilePagePicture);
        cameraButton = view.findViewById(R.id.btn_camera);
        galleryButton = view.findViewById(R.id.btn_gallery);
        deleteButton = view.findViewById(R.id.btn_delete);

        // Instantiate the PhotoUtility
        photoUtility = new PhotoUtility(this);

        // Fill the profile information if already available
        mUsername.setText(((MainActivity)getActivity()).getUsername());
        mEmail.setText(((MainActivity)getActivity()).getEmail());
        mPassword.setText(((MainActivity)getActivity()).getPassword());

        // Button listeners using PhotoUtility
        cameraButton.setOnClickListener(v -> photoUtility.takePhoto());

        galleryButton.setOnClickListener(v -> photoUtility.choosePhoto());

        deleteButton.setOnClickListener(v -> {
            photoUtility.deletePhoto(profilePagePicture, R.drawable.default_profile_image);
            ((MainActivity)getActivity()).setBitmap_profile(null);
        });

        // Save changes button listener
        saveChanges.setOnClickListener(view1 -> {
            String username = mUsername.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            // check if the username, email, and password are empty
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please fill in all the blanks", Toast.LENGTH_SHORT).show();
            } else {
                // Update the values in MainActivity
                ((MainActivity)getActivity()).setUsername(username);
                ((MainActivity)getActivity()).setEmail(email);
                ((MainActivity)getActivity()).setPassword(password);

                // update the values in the database
                ((MainActivity)getActivity()).updateProfileToDB();

                // Show a success message
                Toast.makeText(getContext(), "Profile successfully updated!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Handle camera or gallery result
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
                    profilePagePicture.setImageBitmap(bitmap);
                    ((MainActivity)getActivity()).setBitmap_profile(bitmap);
                }
            }
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PhotoUtility.REQUEST_CODE_TAKE || requestCode == PhotoUtility.REQUEST_CODE_CHOOSE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == PhotoUtility.REQUEST_CODE_TAKE) {
                    photoUtility.takePhoto();
                } else if (requestCode == PhotoUtility.REQUEST_CODE_CHOOSE) {
                    photoUtility.choosePhoto();
                }
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
