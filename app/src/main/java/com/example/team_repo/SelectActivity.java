package com.example.team_repo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SelectActivity extends AppCompatActivity {

    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}