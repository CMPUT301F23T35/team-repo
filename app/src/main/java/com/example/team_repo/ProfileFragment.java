package com.example.team_repo;

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private ImageView profilePagePicture;  // avatar
    private Button cameraButton;  // press to open camera to set avatar
    private Button GalleryButton;  // select from gallery to set avatar
    private Button deleteButton;  // set avatar to default
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button saveChanges;  // press to save changes
    private Uri imageUri;  // the uri of the image
    final private int REQUEST_CODE_TAKE = 1;  // request code for taking photo
    final private int REQUEST_CODE_CHOOSE = 0;  // request code for choosing photo

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUsername = view.findViewById(R.id.profile_username);
        mEmail = view.findViewById(R.id.profile_email);
        mPassword = view.findViewById(R.id.profile_password);
        saveChanges = view.findViewById(R.id.btn_save_changes);
        profilePagePicture = view.findViewById(R.id.profilePagePicture);
        cameraButton = view.findViewById(R.id.btn_camera);
        GalleryButton = view.findViewById(R.id.btn_gallery);
        deleteButton = view.findViewById(R.id.btn_delete);

        // set the current username, email and password
        mUsername.setText(((MainActivity)getActivity()).getUsername());
        mEmail.setText(((MainActivity)getActivity()).getEmail());
        mPassword.setText(((MainActivity)getActivity()).getPassword());
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                // check if the username, email and password are empty
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please fill in all the blanks", Toast.LENGTH_SHORT).show();
                }else {
                    // update the values in main activity
                    ((MainActivity)getActivity()).setUsername(username);
                    ((MainActivity)getActivity()).setEmail(email);
                    ((MainActivity)getActivity()).setPassword(password);

                    // show an update message
                    Toast.makeText(getContext(), "Profile successfully updated!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });

        GalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto(view);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePagePicture.setImageResource(R.drawable.ic_register_username);
                ((MainActivity)getActivity()).setBitmap_profile(null);
                Log.d("ProfileFragment", "deleteButton clicked");
            }
        });


        return view;

    }

    public void takePhoto(View view) {
        // check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // open the camera
            doTake();
        } else {
            Toast.makeText(getContext(), "Please allow camera permission", Toast.LENGTH_SHORT).show();
            // request the camera permission
            // request code == 1 means camera permitted , go to onRequestPermissionsResult
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            // if the camera permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // open the camera
                doTake();
            } else {
                Toast.makeText(getContext(), "Please allow camera permission", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 0){
            // if the read/write permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // open the gallery
                openGallery();
            } else {
                Toast.makeText(getContext(), "Please allow read/write permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void doTake(){
        // create a temporary file to store the image
        File imageTemp = new File(getActivity().getExternalCacheDir(), "imageTemp.jpg");
        if (imageTemp.exists()){
            // if the file exists, delete it
            imageTemp.delete();
        }

        try {
            imageTemp.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        // contentProvider is used to share files between apps
        imageUri = FileProvider.getUriForFile(getActivity(), "com.example.team_repo.fileprovider", imageTemp);

        // open the camera
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");  // set the action to take photo
        // uri represents the location to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  // set the uri of the image
        startActivityForResult(intent, REQUEST_CODE_TAKE);  // start the activity
        // REQUEST_CODE_TAKE is the request code for taking photo, go to onActivityResult

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE){
            // taking photon result
            if (resultCode == RESULT_OK) {
                // get the image from the uri
                try {
                    // get the image stream from the uri
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    // decode the stream to bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    profilePagePicture.setImageBitmap(bitmap);  // set the image to the image view
                    ((MainActivity)getActivity()).setBitmap_profile(bitmap);  // set the image to the main activity

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE){
            if (data == null){
                return;
            }
            handleImage(data);
        }

    }


    private void handleImage(Intent data){
        imageUri = data.getData();
        if (imageUri != null) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profilePagePicture.setImageBitmap(bitmap);
                ((MainActivity)getActivity()).setBitmap_profile(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    public void choosePhoto(View view) {
        if (Build.VERSION.SDK_INT <= 32){
            // check if the read/write permission is granted
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // open the camera
                openGallery();
            } else {
                Toast.makeText(getContext(), "Please allow read/write permission", Toast.LENGTH_SHORT).show();
                // request the read/write permission
                // request code == 0 means permitted read/write, go to onRequestPermissionsResult
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        } else {
            // check if the read/write permission is granted
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                // open the camera
                openGallery();
            } else {
                Toast.makeText(getContext(), "Please allow read/write permission", Toast.LENGTH_SHORT).show();
                // request the read/write permission
                // request code == 0 means permitted read/write, go to onRequestPermissionsResult
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 0);
            }
        }


    }

    private void openGallery(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_CHOOSE);
        // REQUEST_CODE_CHOOSE is the request code for choosing photo, go to onActivityResult

    }

}