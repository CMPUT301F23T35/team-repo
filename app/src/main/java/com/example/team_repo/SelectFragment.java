package com.example.team_repo;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class SelectFragment extends Fragment {

    private ItemAdapter itemAdapter;
    private ArrayList<Item> item_list;
    private ListView item_list_view;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select, container, false);

        // Attach the items in the item list to the adapter
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("items").document("test1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document_snap = task.getResult();
                    if (document_snap.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document_snap.getData());
                        item_list = (ArrayList<Item>) document_snap.get("item_list");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent (@Nullable DocumentSnapshot snapshot,
                                 @Nullable FirebaseFirestoreException e){
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        item_list_view = view.findViewById(R.id.selectPageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list);
        item_list_view.setAdapter(itemAdapter);

        return view;
    }


    //Select from list
    /*Button select_button = view.findViewById(R.id.selectItemsButton);
        select_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            item_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        }
    });

        item_list_view.setOnItemClickListener((parent, view1, position, id) ->

    {
        boolean checked = item_list.get(position).checked;
        if (!checked) {
            item_list.get(position).checked = true;
        } else {
            item_list.get(position).checked = false;
        }
        itemAdapter.notifyDataSetChanged();
    });*/
}
