package com.example.team_repo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class TagFragment extends Fragment{
    private RecyclerView tagRecyclerView;  // the recycler view of the tag page
    private EditText tagEditText;  // the edit text of the tag page
    private TagAdapter tagAdapter;  // the adapter of the tags
    private ArrayList<Tag> tagList;  // the list of all tags created
    private LinearLayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Tag1"));
        tags.add(new Tag("Tag2"));
        tags.add(new Tag("Tag3"));
        tags.add(new Tag("Tag4"));
        tags.add(new Tag("Tag5"));
        tags.add(new Tag("Tag6"));
        tags.add(new Tag("Tag7"));
        tags.add(new Tag("Tag8"));
        tags.add(new Tag("Tag9"));
        tagAdapter = new TagAdapter(getContext(), tags);
        tagRecyclerView = view.findViewById(R.id.tagRecyclerView);
        tagRecyclerView.setAdapter(tagAdapter);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);
        return view;



    }
}