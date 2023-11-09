package com.example.team_repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

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
}