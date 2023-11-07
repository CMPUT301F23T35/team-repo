package com.example.team_repo;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private ItemAdapter itemAdapter;
    private ItemList item_list;
    private ListView item_list_view;
    private TextView total_value_view;

    private FirebaseFirestore db;
    private CollectionReference itemsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        item_list = new ItemList();


        // Test items (delete later)
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020, 0, 1);
        Date date1 = cal1.getTime();
        Item item1 = new Item("Name", date1, 12.34F, "Description", "Make", "Model", "Serial number", "Comment");
        item_list.add(item1);
        Item item2 = new Item("Table", date1, 3.01F, "Table", "Table", "Table", "Table", "Table");
        item_list.add(item2);
        Item item3 = new Item("ABCDEFGHIJKLMNOPQRSTUVWXYZ", date1, 1234567.89F, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        item_list.add(item3);
        Item item4 = new Item("frog hat", date1, 0.01F, "This is the best hat ever", "First Edition", "First Model", "1L0V3FR0GH4T5", "");
        item_list.add(item4);
        Item item5 = new Item("", date1, 0F, "", "", "", "", "");
        item_list.add(item5);


        // Test firedatabase(delete later)
        HashMap<String, ArrayList<Item>> data = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        data.put("item_list", item_list.getList());

        db.collection("items").document("item_list")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });




        // Attach the items in the item list to the adapter
        item_list_view = view.findViewById(R.id.selectPageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList());
        item_list_view.setAdapter(itemAdapter);

        // Display the total estimated value
        total_value_view = view.findViewById(R.id.totalValueTextView);
        total_value_view.setText(String.format("%.2f", item_list.getTotalValue()));

        // Display profile photo
        // TODO: display non-default photo
        ImageView profile_picture = view.findViewById(R.id.homepageProfilePicture);
        profile_picture.setImageResource(R.drawable.default_profile_image);

        // Navigate to select activity
        Button select = view.findViewById(R.id.selectItemsButton);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}