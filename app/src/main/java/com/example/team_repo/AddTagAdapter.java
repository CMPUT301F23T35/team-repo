package com.example.team_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * The AddTagAdapter class extends RecyclerView.Adapter.
 * It is used to display a list of tags for selecting.
 */
public class AddTagAdapter extends RecyclerView.Adapter<AddTagAdapter.TagViewHolder>{
    // This is the adapter for the tag page
    Context context;  // the context of the tag page
    ArrayList<Tag> tagList;

    /**
     * Get the list of all tags created now
     * @return the ArrayList<Tag>
     */
    public ArrayList<Tag> getTags() {
        return tagList;
    }

    /**
     * Change the list of tags
     * @param tagList
     */
    public void setTagList(ArrayList<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * Constructor of the TagAdapter
     * @param context the context of the tag page
     * @param tagList the list of all tags created
     */
    public AddTagAdapter(Context context, ArrayList<Tag> tagList){
        this.context = context;
        this.tagList = tagList;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder holding a View of the given view type.
     */
    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tag_content, parent, false);
        return new TagViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AddTagAdapter.TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tvTag.setText(tag.getTagString());

        holder.tagCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // update tag selected status
                tag.setSelected(isChecked);

            }
        });


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items
     */
    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        // Currently the item_tag_content.xml only has a TextView and a delete button
        public TextView tvTag;
        public CheckBox tagCheckBox;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_item_tag_content); // track the TextView
            tagCheckBox = itemView.findViewById(R.id.cb_tag);  // track the delete button
        }
    }

}
