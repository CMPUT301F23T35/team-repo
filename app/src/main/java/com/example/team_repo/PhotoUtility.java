
package com.example.team_repo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The PhotoUtility class provides a set of utilities for handling photo operations
 * such as taking a photo with the camera, choosing a photo from the gallery, and
 * image rotation correction. This class is specifically designed for use within
 * a Fragment context.
 */
public class PhotoUtility {

    private Fragment fragment;
    private Uri imageUri;
    public static final int REQUEST_CODE_TAKE = 1;
    public static final int REQUEST_CODE_CHOOSE = 0;


    /**
     * Constructor for PhotoUtility.
     *
     * @param fragment The fragment instance where the PhotoUtility is being used.
     */
    public PhotoUtility(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Initiates the process of taking a photo using the device's camera.
     * Checks for camera permission and requests it if not already granted.
     */
    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            doTake();
        } else {
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_TAKE);
        }
    }

    /**
     * Initiates the process of choosing a photo from the device's gallery.
     * Checks for read external storage permission and requests it if not already granted.
     */
    public void choosePhoto() {
        String permission = Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q ? android.Manifest.permission.READ_EXTERNAL_STORAGE : android.Manifest.permission.READ_MEDIA_IMAGES;
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{permission}, REQUEST_CODE_CHOOSE);
        }
    }

    /**
     * Deletes the currently selected photo and updates the ImageView with a default image.
     *
     * @param imageView The ImageView from which the image is to be deleted.
     * @param defaultImageRes The resource ID of the default image to be set after deletion.
     */
    public void deletePhoto(ImageView imageView, int defaultImageRes) {
        imageView.setImageResource(defaultImageRes);
        imageUri = null;
        // If you need to update the main activity or other parts of the application, do it here.
    }

    private void doTake() {
        File imageTemp = new File(fragment.requireActivity().getExternalCacheDir(), "imageTemp.jpg");
        if (imageTemp.exists()) {
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = FileProvider.getUriForFile(fragment.requireContext(), "com.example.team_repo.fileprovider", imageTemp);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, REQUEST_CODE_TAKE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, REQUEST_CODE_CHOOSE);
    }

    public Bitmap handleImageOnActivityResult(Uri imageUri) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = fragment.requireActivity().getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap = rotateImageIfRequired(bitmap, imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(fragment.getContext(), "Failed to get image", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(fragment.getContext(), "Failed to rotate image", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri imageUri) throws IOException {
        InputStream input = fragment.requireContext().getContentResolver().openInputStream(imageUri);
        ExifInterface ei;
        if (input != null) {
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
        return img;
    }




    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    // Getters and Setters
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    // Public access to the request codes
    public static int getRequestCodeTake() {
        return REQUEST_CODE_TAKE;
    }

    public static int getRequestCodeChoose() {
        return REQUEST_CODE_CHOOSE;
    }


}

