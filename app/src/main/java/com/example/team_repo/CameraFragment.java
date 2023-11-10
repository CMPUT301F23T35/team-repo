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


/**
 * The CameraFragment class extends Fragment and is responsible for handling the camera functionalities
 * within the application. It manages the user interface for camera operations, including displaying a
 * preview and capturing images. This fragment integrates with the CameraHandler to manage camera hardware
 * interactions.
 */
public class CameraFragment extends Fragment {
    private CameraHandler cameraHandler;
    private PreviewView previewView;
    private ImageButton captureButton;
    private final ActivityResultLauncher<String> activityResultLauncher;

    /**
     * Constructor for CameraFragment. Initializes the activity result launcher for handling
     * camera permission requests.
     */
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

    /**
     * Called to instantiate the fragment's user interface view. Initializes UI components and
     * sets up the camera handler.
     *
     * @param inflater LayoutInflater to inflate any views in the fragment
     * @param container If non-null, this is the parent view to which the fragment's UI should be attached
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return Return the View for the fragment's UI
     */
    @Nullable
    @Override
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

    /**
     * Called when the fragment is visible to the user and actively running. Ensures the camera is started
     * if permissions are granted.
     */
    @Override
    // When the fragment is resumed, ensure the camera starts if permissions are granted.
    public void onResume() {
        super.onResume();
        if (cameraHandler != null) {
            cameraHandler.startCamera();
        }
    }

    /**
     * Called when the Fragment is no longer resumed. Stops the camera to release resources.
     */
    @Override
    // When the fragment is paused, stop the camera to release resources.
    public void onPause() {
        super.onPause();
        if (cameraHandler != null) {
            cameraHandler.stopCamera();
        }
    }

}

