package com.example.team_repo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsername;  // input username
    private EditText mPassword;  // input password
    private Button login;  // press to check validation and login
    private Toolbar toolbar;  // toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // return to RegisterActivity
        toolbar = findViewById(R.id.login_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUsername = findViewById(R.id.login_username);
        mPassword = findViewById(R.id.login_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: give the username, email and password to main activity
                // TODO: check if the username and password are correct
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                // check if the username, email and password are empty
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,
                            "Please fill in all the blanks",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // jump to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });

    }
}