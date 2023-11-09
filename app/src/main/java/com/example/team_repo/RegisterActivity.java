package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUsername;  // input username
    private EditText mEmail;  // input email
    private EditText mPassword;  // input password
    private Button createAccount;  // press to check validation and create account
    private TextView jumpToLogin;  // press to jump to login page
    private TextView skip;  // press to use default account
    private FirebaseFirestore db;  // the database

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
        db = FirebaseFirestore.getInstance();
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
                    createAccount(username, email, password);
                }


            }
        });

        skip = findViewById(R.id.tv_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // jump to MainActivity with default account
                createAccount("DefaultName", "DefaultEmail", "DefaultPassword");
            }
        });

    }


    /**
     * Create account with username, email and password, and jump to MainActivity
     * @param username the username input
     * @param email the email input
     * @param password the password input
     */
    private void createAccount(String username, String email, String password) {


        // Add a new document with a generated ID
        db.collection("users")  // reference
                .whereEqualTo("email", email)  // query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().isEmpty()){
                            // email is unique, create account

                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            // TODO: encrypt the password ?
                            user.put("password", password);

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // create account successfully
                                            Toast.makeText(RegisterActivity.this, "Welcome, " + username +" !", Toast.LENGTH_SHORT).show();

                                            // get the user id
                                            String userId = documentReference.getId();
                                            // create sub collection here or later in Main Activity

                                            // jump to MainActivity with username, email and password
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("username", username);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            // create account failed
                                            Toast.makeText(RegisterActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else if (task.isSuccessful()) {
                            // email is DefaultEmail, login with default account
                            if (email.equals("DefaultEmail")){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                            } else {
                                // email is not unique
                                Toast.makeText(RegisterActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            // Error
                            Toast.makeText(RegisterActivity.this, "Unexpected error.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}