package com.example.team_repo;



import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SelectActivity extends AppCompatActivity {

    private Button edit;
    private Button delete;
    private Button view;

    private SelectFragment selectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Toolbar
        Toolbar toolbar = (Toolbar) SelectActivity.this.findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //


        edit = findViewById(R.id.edit_button);
        delete = findViewById(R.id.delete_button);
        view = findViewById(R.id.view_button);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (selectFragment == null) {
            selectFragment = new SelectFragment();
            fragmentTransaction.add(R.id.fragmentContainerView, selectFragment);

        } else {
            fragmentTransaction.show(selectFragment);
        }
        fragmentTransaction.commit();
    }
}