package com.example.team_repo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class TagFragment extends Fragment{
    private RecyclerView tagRecyclerView;  // the recycler view of the tag page
    private EditText tagEditText;  // the edit text of the tag page、
    private Button btnAddTag;  // the button to add a tag
    private TagAdapter tagAdapter;  // the adapter of the tags
    private ArrayList<Tag> tagList;  // the list of all tags created
    private LinearLayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        // initialize the recycler view
        tagList = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext(), tagList);
        tagRecyclerView = view.findViewById(R.id.tagRecyclerView);
        tagRecyclerView.setAdapter(tagAdapter);

        // set the layout manager of the recycler view
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);

        // initialize the edit text and the button
        tagEditText = view.findViewById(R.id.editTag);
        btnAddTag = view.findViewById(R.id.btn_addTag);

        // add a tag to the list of tags
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagText = tagEditText.getText().toString().trim();
                if (!tagText.isEmpty()) {
                    tagList.add(new Tag(tagText));
                    tagAdapter.notifyDataSetChanged();
                    tagEditText.setText("");  // clear EditText
                }
            }
        });

        // add some default tags
        tagList.add(new Tag("Tag1"));
        tagList.add(new Tag("Tag2"));

        return view;
    }
}