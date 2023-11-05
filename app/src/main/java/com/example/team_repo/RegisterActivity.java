package com.example.team_repo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUsername;  // input username
    private EditText mEmail;  // input email
    private EditText mPassword;  // input password
    private Button createAccount;  // press to check validation and create account
    private TextView jumpToLogin;  // press to jump to login page
    private TextView skip;  // press to use default account

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        jumpToLogin = findViewById(R.id.tv_jump_to_login);
        jumpToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // jump to LoginActivity

            }
        });


    }
}