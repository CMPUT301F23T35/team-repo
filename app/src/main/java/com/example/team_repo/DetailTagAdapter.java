package com.example.team_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * The DetailTagAdapter class extends RecyclerView.Adapter.
 * It is used to display a list of tags for selecting.
 */
public class DetailTagAdapter extends RecyclerView.Adapter<DetailTagAdapter.TagViewHolder>{
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
     * @param tagList the new list of tags
     */
    public void setTagList(ArrayList<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * Constructor of the TagAdapter
     * @param context the context of the tag page
     * @param tagList the list of all tags created
     */
    public DetailTagAdapter(Context context, ArrayList<Tag> tagList){
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
    public DetailTagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.detail_tag_content, parent, false);
        return new DetailTagAdapter.TagViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull DetailTagAdapter.TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tvTag.setText(tag.getTagString());
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

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_item_tag_content); // track the TextView
        }
    }

}
