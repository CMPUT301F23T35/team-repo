package com.example.team_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * TagAdapter adapts a tagList into view
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder>{
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
     * Constructor of the TagAdapter
     * @param context the context of the tag page
     * @param tagList the list of all tags created
     */
    public TagAdapter(Context context, ArrayList<Tag> tagList){
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
        View v = LayoutInflater.from(context).inflate(R.layout.tag_content, parent, false);
        return new TagViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tvTag.setText(tag.getTagString());

        holder.btnTagDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();  // get the current position
                if (currentPosition != RecyclerView.NO_POSITION) {  // if the position is valid
                    tagList.remove(currentPosition);  // tell the list that the item has been removed
                    notifyItemRemoved(currentPosition);  // tell the adapter to remove the item
                    notifyItemRangeChanged(currentPosition, tagList.size());  // tell the adapter that the item range has been removed
                    ((MainActivity)context).setTagList(tagList);  // update the tag list in the main activity
                    ((MainActivity) context).removeTagFromDB(tag);
                }

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
        // Currently the tag_content.xml only has a TextView and a delete button
        public TextView tvTag;
        public ImageButton btnTagDelete;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag_content); // track the TextView
            btnTagDelete = itemView.findViewById(R.id.btn_tag_delete);  // track the delete button
        }
    }

}
