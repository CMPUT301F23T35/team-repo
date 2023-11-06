package com.example.team_repo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        /*@Override
        public boolean onSupportNavigateUp () {
            return super.onSupportNavigateUp();
        }
        */
    }
}