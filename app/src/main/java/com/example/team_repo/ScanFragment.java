package com.example.team_repo;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScanFragment extends Fragment {
    /**
     * Constructor
     */
    public static ScanFragment newInstance() {
        ScanFragment myFragment = new ScanFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        LinearLayout toolbarLinearLayout = getActivity().findViewById(R.id.select_toolbar);
        int original_visibility = toolbarLinearLayout.getVisibility();
        toolbarLinearLayout.setVisibility(View.GONE);




        Toolbar toolbar = view.findViewById(R.id.item_toolbar);
        //Toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLinearLayout.setVisibility(original_visibility);
                getActivity().onBackPressed();
            }
        });


        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();



        return view;
    }
}
