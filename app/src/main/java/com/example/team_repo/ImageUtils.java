package com.example.team_repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * The ImageUtils class provides static utility methods for various image-related operations
 * such as converting image paths to Bitmaps, saving Bitmaps to files, and handling Firebase
 * storage operations (upload, download, and delete images).
 *
 */
public class ImageUtils {


    /**
     * Converts an image path to a Bitmap object.
     *
     * @param imagePath The path of the image file to be converted
     * @return A Bitmap object of the image, or null if the path is null or empty
     */
    public static Bitmap convertImagePathToBitmap(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null; // Handle null or empty path as needed
        }

        // Decode the image file into a Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888; // You can change this based on your needs
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * Saves a Bitmap to a file in the application's private storage directory.
     *
     * @param context The context used to access the application's private directory
     * @param bitmap The Bitmap to be saved
     * @param filename The name of the file to save the bitmap to
     * @return The absolute path of the saved image file or null if saving failed
     */
    public static String saveBitmapToFile(Context context, Bitmap bitmap, String filename) {
        // Create an image file name
        String imageFileName = filename + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);
        String imagePath = imageFile.getAbsolutePath();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            // Compress bitmap to jpeg with 100% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imagePath;
    }

    /**
     * Uploads an image represented by a Bitmap to Firebase Storage.
     *
     * @param bitmap The Bitmap to upload
     * @param filename The name of the file under which the image will be stored in Firebase
     */
    public static void uploadImageToFirebaseStorage(Bitmap bitmap, String filename) {
        // Get Firebase storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to "images/filename.jpg"
        StorageReference imageRef = storageRef.child("images/" + filename + ".jpg");

        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the file
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // Task completed successfully
        });
    }

    /**
     * Downloads an image from Firebase Storage and provides it as a Bitmap.
     *
     * @param filename The name of the file to be downloaded from Firebase Storage
     * @param listener A callback listener that receives the Bitmap once it's ready
     */
    public static void downloadImageFromFirebaseStorage(String filename, OnBitmapReadyListener listener) {
        // Get Firebase storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to "images/filename.jpg"
        StorageReference imageRef = storageRef.child("images/" + filename + ".jpg");

        // Download the file as a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            // Convert bytes data to bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Invoke callback with the Bitmap
            listener.onBitmapReady(bitmap);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.d("ImageUtils", "Failed to download image from Firebase storage");
            listener.onBitmapReady(null);  // require listener to handle null result
        });
    }

    // Interface for callback when bitmap is ready
    public interface OnBitmapReadyListener {
        void onBitmapReady(Bitmap bitmap);
    }

    /**
     * Deletes an image from Firebase Storage.
     *
     * @param filename The name of the file to be deleted from Firebase Storage
     */
    public static void deleteImageFromFirebaseStorage(String filename) {
        // Get Firebase storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to "images/filename.jpg"
        StorageReference imageRef = storageRef.child("images/" + filename + ".jpg");

        // Delete the file
        imageRef.delete().addOnSuccessListener(aVoid -> {
            // File deleted successfully
        }).addOnFailureListener(exception -> {
            // no such file exists, error
            Log.e("ImageUtils", "Failed to delete image from Firebase storage");

        });
    }


}
