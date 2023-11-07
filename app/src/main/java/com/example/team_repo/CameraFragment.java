package com.example.team_repo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.camera.view.PreviewView;

public class CameraFragment extends Fragment {
    private CameraHandler cameraHandler;
    private PreviewView previewView;
    private ImageButton captureButton;
    private final ActivityResultLauncher<String> activityResultLauncher;

    public CameraFragment() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    // Callback to handle the result of camera permission request.
                    if (result) {
                        // If permission is granted, start the camera.
                        if (cameraHandler != null) {
                            cameraHandler.startCamera();
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    // Called to have the fragment instantiate its user interface view.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // Initialize the UI components.
        previewView = view.findViewById(R.id.cameraPreview);
        captureButton = view.findViewById(R.id.capture);

        // Initialize the camera handler to manage camera operations.
        cameraHandler = new CameraHandler(getActivity(), previewView, captureButton, activityResultLauncher);
        // Request camera permissions as soon as the view is created.
        cameraHandler.requestCameraPermission();

        return view;
    }

    @Override
    // When the fragment is resumed, ensure the camera starts if permissions are granted.
    public void onResume() {
        super.onResume();
        if (cameraHandler != null) {
            cameraHandler.startCamera();
        }
    }

    @Override
    // When the fragment is paused, stop the camera to release resources.
    public void onPause() {
        super.onPause();
        if (cameraHandler != null) {
            cameraHandler.stopCamera();
        }
    }

}

