package com.example.team_repo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


/**
 * The CameraHandler class manages camera functionalities including starting and stopping the camera,
 * handling permissions, and capturing images.
 */
public class CameraHandler {
    private final Context context;
    private final PreviewView previewView;
    private final ImageButton captureButton;
    private final ActivityResultLauncher<String> activityResultLauncher;
    private boolean isCameraStarted = false;


    /**
     * Constructor for CameraHandler.
     *
     * @param context The context in which the handler is operating, usually the current activity
     * @param previewView The PreviewView to display the camera preview
     * @param captureButton The ImageButton to trigger image capture
     * @param activityResultLauncher The launcher for handling permission results
     */
    public CameraHandler(Context context, PreviewView previewView, ImageButton captureButton, ActivityResultLauncher<String> activityResultLauncher) {
        this.context = context;
        this.previewView = previewView;
        this.captureButton = captureButton;
        this.activityResultLauncher = activityResultLauncher;
    }

    /**
     * Requests camera permission using the provided ActivityResultLauncher.
     */
    public void requestCameraPermission() {
        activityResultLauncher.launch(Manifest.permission.CAMERA);
    }

    /**
     * Starts the camera preview if it hasn't been started already.
     */
    public void startCamera() {
        if (isCameraStarted) {
            return;
        }
        // Future to obtain the camera provider which manages the camera lifecycle.
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            // Attempt to start the camera and bind its lifecycle to the FragmentActivity.
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();

                ImageCapture imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((FragmentActivity) context, cameraSelector, preview, imageCapture);

                captureButton.setOnClickListener(v -> takePicture(imageCapture));

                isCameraStarted = true;
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Thread.currentThread().interrupt();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    /**
     * Stops the camera preview if it is currently running.
     */
    public void stopCamera() {
        if (!isCameraStarted) {
            return;
        }

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
                isCameraStarted = false;
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, "Error stopping camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Thread.currentThread().interrupt();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    /**
     * Captures an image and saves it to the external storage.
     *
     * @param imageCapture The ImageCapture instance used for taking pictures
     */
    private void takePicture(ImageCapture imageCapture) {
        // Content values to describe the saved image's metadata.
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        // Configuration for how and where the image should be saved.
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        context.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build();

        // Initiates an asynchronous image capture which will be processed in the provided executor.
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
            // Callback for when the image has been successfully saved.
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = outputFileResults.getSavedUri();
                String msg = "Photo capture succeeded: " + savedUri;
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }


            // Callback for when there is an error during image capture.
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(context, "Photo capture failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

