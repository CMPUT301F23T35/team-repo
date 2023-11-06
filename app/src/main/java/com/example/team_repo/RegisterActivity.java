package com.example.team_repo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mUsername = findViewById(R.id.register_username);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        createAccount = findViewById(R.id.btn_create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: give the username, email and password to main activity
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                // check if the username, email and password are empty
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the blanks", Toast.LENGTH_SHORT).show();
                }else {
                    // jump to MainActivity with username, email and password
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }

            }
        });

        skip = findViewById(R.id.tv_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // jump to MainActivity with default account
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("username", "DefaultUser");
                intent.putExtra("email", "DefaultEmail");
                intent.putExtra("password", "DefaultPassword");
                startActivity(intent);
            }
        });

    }

}