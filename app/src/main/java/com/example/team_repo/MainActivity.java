package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;  // the bottom navigation bar
    private HomeFragment homeFragment;  // the home page
    private AddFragment addFragment;  // the add page
    private CameraFragment cameraFragment;  // the camera page
    private TagFragment tagFragment;  // the tag page
    private ProfileFragment profileFragment;  // the profile page
    private LinearLayout toolbarLinearLayout;  // the top toolbar
    private TextView tv_header;  // the header of the app
    private String username;  // the username of the user
    private String email;  // the email of the user
    private String password;  // the password of the user

    private Bitmap bitmap_profile;  // the profile photo of the user
    private ImageView headerPicture;  // the profile photo of the user in the header


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the username, email and password from the RegisterActivity or LoginActivity
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        // set the header of the app
        tv_header = findViewById(R.id.tv_header);
        String header = "Hello, " + username + "!";
        tv_header.setText(header);

        // initialize the bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbarLinearLayout = findViewById(R.id.toolbar);

        // initialize the header picture
        headerPicture = findViewById(R.id.headerPicture);

        // default selection is the Home Page
        selectedFragment(0);

        // listener of the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    selectedFragment(0);
                } else if (item.getItemId() == R.id.add){
                    selectedFragment(1);
                } else if (item.getItemId() == R.id.camera){
                    selectedFragment(2);
                } else if (item.getItemId() == R.id.tag){
                    selectedFragment(3);
                } else if (item.getItemId() == R.id.profile){
                    selectedFragment(4);
                }
                return true;
            }
        });
    }

    /**
     * After click on the bottom navigation bar,
     * call this function to show the corresponding fragment
     * @param position the position of the button clicked
     */
    private void selectedFragment(int position) {
        // modify the fragments using FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);

        // set the header of the app
        if (bitmap_profile != null){
            headerPicture.setImageBitmap(bitmap_profile);
        } else {
            headerPicture.setImageResource(R.drawable.default_profile_image);
        }

        if (position == 0) {
            // show the home page
            if (homeFragment == null) {
                // home page has not been defined, define it
                homeFragment = new HomeFragment();
                toolbarLinearLayout.setVisibility(View.GONE);
                fragmentTransaction.add(R.id.fragment_container, homeFragment);

            } else {
                // home page has been defined, show it
                toolbarLinearLayout.setVisibility(View.GONE);
                homeFragment.refresh();
                fragmentTransaction.show(homeFragment);
                Log.d("MainActivity", "selectedFragment() called, homeFragment: " + homeFragment);

            }
        }

        if (position == 1) {
            // show the add page
            if (addFragment == null) {
                // add page has not been defined, define it
                addFragment = new AddFragment();
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.add(R.id.fragment_container, addFragment);

            } else {
                // add page has been defined, show it
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.show(addFragment);

            }
        }

        if (position == 2) {
            // show the camera page
            if (cameraFragment == null) {
                // camera page has not been defined, define it
                cameraFragment = new CameraFragment();
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.add(R.id.fragment_container, cameraFragment);

            } else {
                // camera page has been defined, show it
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.show(cameraFragment);

            }
        }

        if (position == 3) {
            // show the tag page
            if (tagFragment == null) {
                // tag page has not been defined, define it
                tagFragment = new TagFragment();
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.add(R.id.fragment_container, tagFragment);

            } else {
                // tag page has been defined, show it
                toolbarLinearLayout.setVisibility(View.VISIBLE);
                fragmentTransaction.show(tagFragment);

            }
        }

        if (position == 4) {
            // show the profile page
            if (profileFragment == null) {
                // profile page has not been defined, define it
                profileFragment = new ProfileFragment();
                toolbarLinearLayout.setVisibility(View.GONE);
                fragmentTransaction.add(R.id.fragment_container, profileFragment);

            } else {
                // profile page has been defined, show it
                toolbarLinearLayout.setVisibility(View.GONE);
                fragmentTransaction.show(profileFragment);

            }
        }

        // commit the transaction
        fragmentTransaction.commit();



    }

    /**
     * Hide all fragments
     * when click on the bottom navigation bar, call this method to hide all the fragments first
     * @param fragmentTransaction the transaction of the fragments
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        // if a fragment has been defined, hide it
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (addFragment != null) {
            fragmentTransaction.hide(addFragment);
        }
        if (cameraFragment != null) {
            fragmentTransaction.hide(cameraFragment);
        }
        if (tagFragment != null) {
            fragmentTransaction.hide(tagFragment);
        }
        if (profileFragment != null) {
            fragmentTransaction.hide(profileFragment);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        tv_header.setText("Hello, " + username + "!");

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Bitmap getBitmap_profile() {
        return bitmap_profile;
    }

    public void setBitmap_profile(Bitmap bitmap_profile) {
        this.bitmap_profile = bitmap_profile;
        Log.d("MainActivity", "setBitmap_profile() called, bitmap_profile: " + bitmap_profile);
    }



}