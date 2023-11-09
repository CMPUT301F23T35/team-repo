package com.example.team_repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static Bitmap convertImagePathToBitmap(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null; // Handle null or empty path as needed
        }

        // Decode the image file into a Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888; // You can change this based on your needs
        return BitmapFactory.decodeFile(imagePath, options);
    }


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

    public void uploadImageToFirebaseStorage(Bitmap bitmap, String filename) {
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
}
