package com.example.team_repo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {

    private ItemList item_list;
    ItemAdapter item_adapter;
    private String userID;
    private FirebaseFirestore db;

    private ArrayList<Tag> tag_list;


    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc. This method also provides a Bundle
     * containing the activity's previously frozen state, if there was one.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied in onSaveInstanceState(Bundle). Note: Otherwise, it is
     *                            null.
     */
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
        tag_list = (ArrayList<Tag>) extras.get("taglist");
        AddTagAdapter tag_adapter = new AddTagAdapter(this, tag_list);
        tag_list_view.setAdapter(tag_adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tag_list_view.setLayoutManager(layoutManager);

        item_list = new ItemList();
        ListView item_list_view = findViewById(R.id.itemListView);
        item_adapter = new ItemAdapter(SelectActivity.this, item_list.getList(), true);
        item_list_view.setAdapter(item_adapter);
        readItemListFromDB(item_list);

        Button delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMultiple();
            }
        });

        Button add_tags_button = findViewById(R.id.add_tags_button);
        add_tags_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTags();
            }
        });
    }


    /**
     * Deletes multiple items from the Firestore database associated with the current user.
     * The deletion is performed in a batch to improve efficiency. The method iterates through
     * the list of items in the item_list and identifies items marked as checked for deletion.
     * Upon successful deletion, the checked items are removed from the local item_list, and the
     * adapter is notified to update the UI.
     */
    public void deleteMultiple(){
        WriteBatch writeBatch = db.batch();
        ItemList delete_list = new ItemList();
        for(Item item: item_list.getList()){
            if(item.checked){
                delete_list.add(item);
                DocumentReference documentReference = db.collection("users").document(userID).collection("items").document(item.itemRef);
                writeBatch.delete(documentReference);
                item.checked = false;
            }
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                item_list.removeAll(delete_list);
                for (Item item : item_list.getList()) {
                    item.checked = false;
                }
                item_adapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * Adds selected tags to items that are marked for modification. The method iterates through
     * the tag_list to identify selected tags and then iterates through the item_list to identify
     * items marked as checked for modification. For each checked item, the selected tags are added
     * to its existing tags, and the update is committed to the Firestore database. A Toast message
     * is displayed to indicate successful tag addition.
     */
    public void addTags(){
        WriteBatch writeBatch = db.batch();
        ArrayList<Tag> add_tag_list = new ArrayList<>();
        for(Tag tag: tag_list){
            if(tag.isSelected()){
                add_tag_list.add(tag);
            }
        }

        for(Item item: item_list.getList()){
            if(item.checked){
                ArrayList<Tag> origin_tags = item.getTags();
                ArrayList<Tag> updated_tags = merge_tag_lists(add_tag_list, origin_tags);
                DocumentReference documentReference = db.collection("users").document(userID).collection("items").document(item.itemRef);
                Map<String, Object> updates = new HashMap<>();
                updates.put("tags", updated_tags);
                writeBatch.update(documentReference, updates);

                Toast.makeText(this, "Tags added successfully!", Toast.LENGTH_SHORT).show();
            }
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                readItemListFromDB(item_list);
            }
        });
    }

    private ArrayList<Tag> merge_tag_lists(ArrayList<Tag> add_list, ArrayList<Tag> origin_list){
        for (Tag tag: origin_list){
            if(add_list.contains(tag)){
                continue;
            }
            add_list.add(tag);
        }

        return add_list;
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

    /**
     * Reads the list of items from the Firestore database associated with the current user.
     * This method retrieves items from the "items" collection under the user's document, and
     * populates the provided ItemList with the retrieved items. The method uses a listener to
     * asynchronously handle the completion of the database query and updates the local item_list
     * accordingly. After updating the local list, the adapter is notified to refresh the UI.
     *
     * @param itemList The ItemList to be populated with items from the database.
     */
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