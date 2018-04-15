package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyGalleryRecyclerViewAdapter extends RecyclerView.Adapter<PropertyGalleryRecyclerViewAdapter.PropertyGalleryViewHolder> {
    private final Context context;
    private final ArrayList<String> items;
    private LayoutInflater inflater;
    private float linearWidth, linearHeight, gridSize;

    public PropertyGalleryRecyclerViewAdapter(Context context, ArrayList<String> items){
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearWidth = context.getResources().getDimension(R.dimen.property_gallery_item_width_linear);
        linearHeight = context.getResources().getDimension(R.dimen.property_gallery_item_height_linear);
        gridSize = context.getResources().getDimension(R.dimen.property_gallery_item_size_grid);
    }


    @Override
    public void onBindViewHolder(@NonNull PropertyGalleryViewHolder holder, int position) {
        Glide.with(context).load(items.get(position)).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public PropertyGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.gallery_photo_item, parent, false);
        return new PropertyGalleryViewHolder(v);
    }

    public class PropertyGalleryViewHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public ConstraintLayout layout;
        public PropertyGalleryViewHolder(View itemView) {
            super(itemView);
            layout = (ConstraintLayout)itemView;
            photo = itemView.findViewById(R.id.property_photo);
        }
    }
}
