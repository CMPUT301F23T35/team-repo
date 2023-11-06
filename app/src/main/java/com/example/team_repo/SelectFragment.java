package com.example.team_repo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectFragment extends Fragment {

    private ItemAdapter itemAdapter;
    private ItemList item_list;
    private ListView item_list_view;
    //private FirebaseFirestore db;
    //private CollectionReference citiesRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select, container, false);

        // Attach the items in the item list to the adapter
        item_list_view = view.findViewById(R.id.selectPageListView);
        itemAdapter = new ItemAdapter(this.getContext(), item_list.getList());
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
