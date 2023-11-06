package com.example.team_repo;

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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

        // initialize the components
        mUsername = view.findViewById(R.id.profile_username);
        mEmail = view.findViewById(R.id.profile_email);
        mPassword = view.findViewById(R.id.profile_password);
        saveChanges = view.findViewById(R.id.btn_save_changes);
        profilePagePicture = view.findViewById(R.id.profilePagePicture);
        cameraButton = view.findViewById(R.id.btn_camera);
        GalleryButton = view.findViewById(R.id.btn_gallery);
        deleteButton = view.findViewById(R.id.btn_delete);

        // fill the profile information that already have
        mUsername.setText(((MainActivity)getActivity()).getUsername());
        mEmail.setText(((MainActivity)getActivity()).getEmail());
        mPassword.setText(((MainActivity)getActivity()).getPassword());

        // press to save changes, and update the values in main activity
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

        // press to open camera to set avatar, and update the bitmap in main activity
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });

        // press to select from gallery to set avatar, and update the bitmap in main activity
        GalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto(view);
            }
        });

        // press to set avatar to default, and update the bitmap in main activity
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePagePicture.setImageResource(R.drawable.ic_register_username);
                ((MainActivity)getActivity()).setBitmap_profile(null);
            }
        });

        return view;
    }

    /**
     * open the camera to take photo
     * Called when the user taps the camera button
     *
     * @param view didn't use this parameter
     */
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

    /**
     * Called when the user has granted or denied one or more permission requests.
     * Called by takePhoto() and choosePhoto()
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){  // camera permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // permission granted
                doTake();

            } else {
                Toast.makeText(getContext(), "Please allow camera permission", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 0){  // read/write permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // permission granted
                openGallery();
            } else {
                Toast.makeText(getContext(), "Please allow read/write permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * open the camera to take photo
     * Called by takePhoto()
     */
    private void doTake(){
        // create a temporary file to store the image
        File imageTemp = new File(getActivity().getExternalCacheDir(), "imageTemp.jpg");

        if (imageTemp.exists()){
            // if the file with the same name exists, delete it
            imageTemp.delete();
        }

        try {
            imageTemp.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        // contentProvider is used to share files between apps （set the authority in AndroidManifest.xml）
        imageUri = FileProvider.getUriForFile(getActivity(), "com.example.team_repo.fileprovider", imageTemp);

        // open the camera
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");  // set the action to take photo

        // uri represents the location to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  // set the uri of the image
        startActivityForResult(intent, REQUEST_CODE_TAKE);  // start the activity
        // REQUEST_CODE_TAKE is the request code for taking photo, go to onActivityResult

    }

    /**
     * Called when camera activity or gallery activity returns
     * Called by doTake() and choosePhoto() to handle the image
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE){
            // taking photo result

            if (resultCode == RESULT_OK) {
                // get the image from the uri
                try {
                    // get the image stream from the uri, and decode it to bitmap
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // update the header images (current fragment and main activity)
                    profilePagePicture.setImageBitmap(bitmap);
                    ((MainActivity)getActivity()).setBitmap_profile(bitmap);

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE){
            // choosing photo result
            if (data == null){
                // if no image is selected, return
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


                // deal with the orientation of the image
                bitmap = rotateImageIfRequired(getContext(), bitmap, imageUri);

                // update the header images (current fragment and main activity)
                profilePagePicture.setImageBitmap(bitmap);
                ((MainActivity)getActivity()).setBitmap_profile(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(getContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * select from gallery to set avatar
     * @param view didn't use this parameter
     */
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

    /**
     * open the gallery to select photo
     * Called by choosePhoto()
     */
    private void openGallery(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_CHOOSE);
        // REQUEST_CODE_CHOOSE is the request code for choosing photo, go to onActivityResult

    }

    /**
     * deal with the orientation of the image
     * Called by handleImage()
     * @param context the context
     * @param img the bitmap of the image
     * @param selectedImage the uri of the image
     * @return the bitmap of the image after rotation
     * @throws IOException if the uri is invalid
     */
    private Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        ei = new ExifInterface(input);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    /**
     * rotate the image
     * Called by rotateImageIfRequired()
     * @param img the bitmap of the image
     * @param degree the degree to rotate
     * @return the bitmap of the image after rotation
     */
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }




}