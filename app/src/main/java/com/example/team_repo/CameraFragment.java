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
                    if (result) {
                        if (cameraHandler != null) {
                            cameraHandler.startCamera();
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        previewView = view.findViewById(R.id.cameraPreview);
        captureButton = view.findViewById(R.id.capture);

        cameraHandler = new CameraHandler(getActivity(), previewView, captureButton, activityResultLauncher);
        cameraHandler.requestCameraPermission();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraHandler != null) {
            cameraHandler.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Optionally unbind the camera resources on pause
    }
}

