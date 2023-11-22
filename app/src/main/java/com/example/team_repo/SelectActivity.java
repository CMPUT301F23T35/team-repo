package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    private ItemList item_list;
    ItemAdapter item_adapter;
    private String userID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Bundle extras = getIntent().getExtras();
        db = FirebaseFirestore.getInstance();
        userID = (String)extras.get("userID");

        Toolbar toolbar = findViewById(R.id.select_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView tag_list_view = findViewById(R.id.TagListView);
        ArrayList<Tag> tag_list = (ArrayList<Tag>) extras.get("taglist");
        AddTagAdapter tag_adapter = new AddTagAdapter(this, tag_list);
        tag_list_view.setAdapter(tag_adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tag_list_view.setLayoutManager(layoutManager);

        item_list = new ItemList();
        ListView item_list_view = findViewById(R.id.itemListView);
        item_adapter = new ItemAdapter(SelectActivity.this, item_list.getList());
        item_list_view.setAdapter(item_adapter);
        readItemListFromDB(item_list);

        Button delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMultiple();
            }
        });

        Button add_button = findViewById(R.id.add_tag_button);
    }

    public void deleteMultiple(){
        WriteBatch writeBatch = db.batch();
        ItemList delete_list = new ItemList();
        for(Item item: item_list.getList()){
            if(item.checked){
                delete_list.add(item);
                DocumentReference documentReference = db.collection("users").document(userID).collection("items").document(item.itemRef);
                writeBatch.delete(documentReference);
            }
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                item_list.removeAll(delete_list);
                item_adapter.notifyDataSetChanged();
            }
        });
    }


   /* public void deleteItemFromDB(Item item){

        db.collection("users").document(userID).collection("items").document(item.itemRef)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        item_list.remove(item);
                        item_adapter.notifyDataSetChanged();
                    }
                });
    }*/

    public void readItemListFromDB(ItemList itemList){
        db.collection("users").document(userID).collection("items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            item_list.clear();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Item item = document.toObject(Item.class);
                                item.itemRef = document.getId();
                                item_list.add(item);
                            }
                            item_adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}