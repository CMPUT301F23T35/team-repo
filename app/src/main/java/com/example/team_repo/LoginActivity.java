package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail;  // input email
    private EditText mPassword;  // input password
    private Button login;  // press to check validation and login
    private Toolbar toolbar;  // toolbar
    private FirebaseFirestore db;  // the database
    private String email;
    private String password;

    // TODO: Forgot password ?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        if (email != null && password != null) {
            checkAndJump(email, password);
        }


        // return to RegisterActivity
        toolbar = findViewById(R.id.login_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            // click tool bar will close current activity
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // initialize username, password and login button
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                // check if the username, email and password are empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,
                            "Please fill in all the blanks",
                            Toast.LENGTH_SHORT).show();
                } else {

                    checkAndJump(email, password);

                }
            }

        });

    }

    /**
     * check if the username and password are correct, if correct, jump to MainActivity
     * @param email the email input
     * @param password the password input
     */
    private void checkAndJump(String email, String password) {
        db.collection("users")  // reference
                .whereEqualTo("email", email)  // query email
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                // email exists, check password
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String storedPassword = documentSnapshot.getString("password");
                                String name = documentSnapshot.getString("username");

                                if (password.equals(storedPassword)) {

                                    // password matches, give data, jump
                                    Toast.makeText(LoginActivity.this, "Welcome back, " + name + " !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    // get user id
                                    intent.putExtra("userId", documentSnapshot.getId());
                                    intent.putExtra("username", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                } else {

                                    // password does not match
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // email does not exist
                                Toast.makeText(LoginActivity.this, "Email is not registered", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error
                            Toast.makeText(LoginActivity.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}