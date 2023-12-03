package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MainActivity handles the initialization of information, bottom navigation bar,
 * transaction between major fragments and connecting to database;
 */
public class MainActivity extends AppCompatActivity implements ItemDetailFragment.OnItemUpdatedListener {
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
    private ItemList add_item_list;  // the list of items shown in the home page
    private ArrayList<Tag> tagList;  // the list of all tags created
    private ItemAdapter itemAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemsRef;
    private CollectionReference userRef;
    private DocumentReference userDocRef;
    private ItemDetailFragment itemDetailFragment;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUser(new OnUserDataLoadedListener() {
            @Override
            public void onUserDataLoaded() {
                selectedFragment(0);
            }
        });


        // listener of the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    selectedFragment(0);
                } else if (item.getItemId() == R.id.add) {
                    selectedFragment(1);
                } else if (item.getItemId() == R.id.camera) {
                    selectedFragment(2);
                } else if (item.getItemId() == R.id.tag) {
                    selectedFragment(3);
                } else if (item.getItemId() == R.id.profile) {
                    selectedFragment(4);
                }
                return true;
            }
        });

    }


    /**
     * After click on the bottom navigation bar,
     * call this function to show the corresponding fragment
     *
     * @param position the position of the button clicked
     */
    private void selectedFragment(int position) {
        // modify the fragments using FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);

        // set the header of the app
        if (bitmap_profile != null) {
            headerPicture.setImageBitmap(bitmap_profile);
        } else {
            // get the image from the firebase storage, named email + ".jpg"
            ImageUtils.downloadImageFromFirebaseStorage(getEmail(), new ImageUtils.OnBitmapReadyListener() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    if (bitmap != null) {
                        headerPicture.setImageBitmap(bitmap);
                    } else {
                        headerPicture.setImageResource(R.drawable.default_profile_image);
                    }
                }
            });

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
                // add page has been defined, refresh it and show it
                addFragment.refresh();
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
     *
     * @param fragmentTransaction the transaction of the fragments
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        // if a fragment has been defined, hide it
        if (itemDetailFragment != null) {
            fragmentTransaction.hide(itemDetailFragment);
            homeFragment = null;
        }
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
            homeFragment = null;
        }
        if (addFragment != null) {
            fragmentTransaction.hide(addFragment);
            addFragment = null;
        }
        if (cameraFragment != null) {
            fragmentTransaction.hide(cameraFragment);
            cameraFragment = null;  // release the camera
        }
        if (tagFragment != null) {
            fragmentTransaction.hide(tagFragment);
            tagFragment = null;
        }
        if (profileFragment != null) {
            fragmentTransaction.hide(profileFragment);
            profileFragment = null;
        }


        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ScanFragment) {
                onBackPressed();
            }
        }
    }


    /**
     * Read user information and stored items from database.
     *
     * @param listener listener for checking if the user data is loaded
     */
    private void initializeUser(OnUserDataLoadedListener listener) {
        // get the username, email and password from the RegisterActivity or LoginActivity
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        userId = getIntent().getStringExtra("userId");

        Log.d("LogMain", "Check id: " + userId);
        // get the user document reference according to id
        userRef = db.collection("users");
        userDocRef = userRef.document(userId);

        // set the header of the app
        tv_header = findViewById(R.id.tv_header);
        String header = "Hello, " + username + "!";
        tv_header.setText(header);

        // initialize the bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbarLinearLayout = findViewById(R.id.select_toolbar);

        // initialize the header picture
        headerPicture = findViewById(R.id.headerPicture);

        // initialize the item list adding into the home page, filled in the AddFragment
        add_item_list = new ItemList();

        // initialize the tag list, filled in the AddFragment
        tagList = new ArrayList<>();

        // check userDocRef if it has a "tags" collection
        userDocRef.collection("tags").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // get all tags from the collection and add them to tagList
                for (int i = 0; i < task.getResult().size(); i++) {
                    // get the tag name from the collection
                    String tag_name = task.getResult().getDocuments().get(i).getId();
                    Tag tag = new Tag(tag_name);
                    tagList.add(tag);

                }
            } else {
                // if the collection does not exist, create a new one
                userDocRef.collection("tags");
                // add the tagList into the database
                for (Tag tag : tagList) {
                    userDocRef.collection("tags").document(tag.getTagString()).set(tag);
                }
            }
        });


        userDocRef.collection("items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // get all items from the collection and add them to add_item_list
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // check document id
                    Log.d("LogMain", document.getId() + " => " + document.getData());
                    Item item = document.toObject(Item.class); // document -> item
                    item.itemRef = document.getId();
                    add_item_list.add(item); // add all

                    // use listener to make sure the tagList is loaded before the AddFragment is created
                    if (listener != null) {
                        listener.onUserDataLoaded();
                    }
                }

            } else {
                // if the collection does not exist, nothing happens
                // a new collection will be created when the user adds an item
                Log.d("LogMain", "No such document");
                if (listener != null) {
                    listener.onUserDataLoaded();
                }
            }
        }).addOnFailureListener(e -> {
            Log.d("LogMain", "Error getting documents: ", e);
        });

    }

    /**
     * Add an item to the database
     *
     * @param item an item to be added
     */
    public void addItemToDB(Item item) {
        // add the item to the database
        userDocRef.collection("items").add(item.toMap());
    }

    /**
     * delete an item from the datd base
     *
     * @param item an item to be deleted
     */
    public void deleteItemFromDB(Item item) {
        String itemId = item.getItemID();

        db.collection("users").document(userId).collection("items")  // ref
                .whereEqualTo("itemID", itemId)  // query with id
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // technically, there should be only one document
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference().delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("logDB", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("logDB", "Error deleting document", e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // handle error
                    }
                });

    }

    /**
     * update username, email and password of a user in the database
     */
    public void updateProfileToDB() {
        // update current user's profile to the database
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("email", email);
        updates.put("password", password);
        userDocRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // handle success
                        Log.d("logDB", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // handle error
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("logDB", "Error updating document", e);
                        Toast.makeText(MainActivity.this, "Error updating profile to DB", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * Update all fields of an item
     *
     * @param item the item with new item information
     */
    public void updateItemToDB(Item item) {
        // update all fields of the item, use the item id to find the item
        String itemId = item.getItemID();
        Map<String, Object> updates = new HashMap<>();
        // name, purchase_date, value, description, make, model, serial_number, tags, image
        updates.put("name", item.getName());
        updates.put("purchase_date", item.getPurchase_date());
        updates.put("value", item.getValue());
        updates.put("description", item.getDescription());
        updates.put("make", item.getMake());
        updates.put("model", item.getModel());
        updates.put("serial_number", item.getSerial_number());
        updates.put("comment", item.getComment());
        updates.put("tags", item.getTags());
        updates.put("image", item.getImagePath());

        db.collection("users").document(userId).collection("items")  // ref
                .whereEqualTo("itemID", itemId)  // query with id
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // technically, there should be only one document
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference().update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("logDB", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // handle error
                                            Log.w("logDB", "Error updating document", e);
                                            Toast.makeText(MainActivity.this, "Error updating document to DB", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // handle error
                    }
                });

    }

    /**
     * add a tag to the database
     *
     * @param tag the tag to be added
     */
    public void addTagToDB(Tag tag) {
        userDocRef.collection("tags").document(tag.getTagString()).set(tag);
    }

    /**
     * delete a tag from DB, and remove the tag from all items that contain the tag
     *
     * @param tagToRemove tag to be deleted
     */
    public void removeTagFromDB(final Tag tagToRemove) {
        // Query all items that contain the tag
        db.collection("users").document(userId).collection("items")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // for each item, get a list of tags hashmap
                            List<HashMap<String, Object>> currentTags = (List<HashMap<String, Object>>) documentSnapshot.get("tags");
                            if (currentTags != null) {
                                // Iterate and remove the tag if it matches
                                Iterator<HashMap<String, Object>> iterator = currentTags.iterator();
                                while (iterator.hasNext()) {
                                    HashMap<String, Object> tagMap = iterator.next();
                                    Tag tag = new Tag((String) tagMap.get("tagString"));
                                    if (tag.equals(tagToRemove)) {
                                        iterator.remove();
                                    }
                                }
                                // Update the item's tags
                                documentSnapshot.getReference().update("tags", currentTags);
                            }

                        }

                        // Now, delete the tag itself from the tags collection
                        userDocRef.collection("tags").document(tagToRemove.getTagString())
                                .delete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("logDB", "Error querying items with tag", e);
                    }
                });
    }

    //getters and setters of username, email, password and header's bitmap_profile

    /**
     * Return username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change username
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
        tv_header.setText("Hello, " + username + "!");

    }

    /**
     * return the email of the user
     *
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Change email information
     *
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * return user's password
     *
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Change user's password
     *
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return user's photo
     *
     * @return photo
     */
    public Bitmap getBitmap_profile() {
        return bitmap_profile;
    }

    /**
     * Change user's photo
     *
     * @param bitmap_profile photo
     */
    public void setBitmap_profile(Bitmap bitmap_profile) {
        this.bitmap_profile = bitmap_profile;
        Log.d("MainActivity", "setBitmap_profile() called, bitmap_profile: " + bitmap_profile);
    }

    /**
     * return the itemlist for the home page
     *
     * @return itemlist
     */
    public ItemList getAdd_item_list() {
        return add_item_list;
    }

    /**
     * Return the database user id
     *
     * @return userid
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Change the item list
     *
     * @param add_item_list new item list
     */
    public void setAdd_item_list(ItemList add_item_list) {
        this.add_item_list = add_item_list;
    }

    /**
     * return a tag list
     *
     * @return a tag list
     */
    public ArrayList<Tag> getTagList() {
        return tagList;
    }


    /**
     * Change the tag list
     *
     * @param tagList new tag list
     */
    public void setTagList(ArrayList<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * Transfer to item detail fragment
     *
     * @param item the item to be diaplayed
     */
    public void showItemDetailFragment(Item item) {
        // Create a new fragment instance and pass the item to it
        itemDetailFragment = ItemDetailFragment.newInstance(item);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, itemDetailFragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * A listener for loading user data
     */
    public interface OnUserDataLoadedListener {
        void onUserDataLoaded();
    }

    /**
     * Notify the adapter that the dataset has changed
     *
     * @param item the item to be updated
     */
    @Override
    public void onItemUpdated(Item item) {
        // Notify the adapter that the dataset has changed
        if (itemAdapter != null) {
            itemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * A callback interface for getting the item list from the database
     */
    public interface ItemListCallback {
        void onCallback(ItemList itemList);
    }


    /**
     * Get the item list from the database
     * How to use:
     * getItemListFromDB(new ItemListCallback() {
     *      \@Override public void onCallback(ItemList itemList) {
     *          // use the itemList here
     *      }
     * });
     * @param callback the callback function, use the list here
     */
    public void getItemListFromDB(ItemListCallback callback) {
        ItemList itemList = new ItemList();
        userDocRef.collection("items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // get all items from the collection and add them to itemList
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("mainlog", document.getId() + " => " + document.getData());
                    Item item = document.toObject(Item.class);
                    item.itemRef = document.getId();
                    itemList.add(item);

                }
                callback.onCallback(itemList); // Call back with the loaded list


            } else {
                callback.onCallback(itemList); // Call back with empty list
            }
        });
    }


    /**
     * Transfer to scan fragment
     */
    public void showScanFragment(int position, AlertDialog dialog) {
        // Replace whatever is in the fragment_container view with ScanFragment,
        // and add the transaction to the back stack
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ScanFragment.newInstance(position, dialog))
                .addToBackStack(null)
                .commit();

    }
    /**
     * Transfer to scan fragment
     */
    public void showScanFragment(int position, AddFragment addFragment) {
        // Replace whatever is in the fragment_container view with ScanFragment,
        // and add the transaction to the back stack
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ScanFragment.newInstance(position, addFragment))
                .addToBackStack(null)
                .commit();

    }










}