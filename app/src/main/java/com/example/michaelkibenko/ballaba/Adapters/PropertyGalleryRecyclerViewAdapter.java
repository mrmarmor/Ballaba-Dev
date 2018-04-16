package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyGalleryRecyclerViewAdapter extends RecyclerView.Adapter<PropertyGalleryRecyclerViewAdapter.PropertyGalleryViewHolder> {
    private final Context context;
    private final ArrayList<HashMap<String, String>> items;
    private LayoutInflater inflater;
    private float linearWidth, linearHeight, gridSize;
    private @PropertyGalleryActivity.VIEW_TYPES String currentViewType;

    public PropertyGalleryRecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> items, @PropertyGalleryActivity.VIEW_TYPES String currentViewType){
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearWidth = context.getResources().getDimension(R.dimen.property_gallery_item_width_linear);
        linearHeight = context.getResources().getDimension(R.dimen.property_gallery_item_height_linear);
        gridSize = context.getResources().getDimension(R.dimen.property_gallery_item_size_grid);
        this.currentViewType = currentViewType;
    }


    @Override
    public void onBindViewHolder(@NonNull final PropertyGalleryViewHolder holder, int position) {
        final String photoUrl = items.get(position).get("photo_url");
        Glide.with(context).load(photoUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                //TODO set x icon
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.photo.setImageDrawable(resource);
                holder.progressBar.setVisibility(View.GONE);
                return true;
            }
        }).into(holder.photo);

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PropertyGalleryActivity)context).showPhotoFullScreen(photoUrl);
            }
        });
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
        public ProgressBar progressBar;
        public PropertyGalleryViewHolder(View itemView) {
            super(itemView);
            layout = (ConstraintLayout)itemView;
            photo = itemView.findViewById(R.id.property_photo);
            progressBar = itemView.findViewById(R.id.galleryPhoto_progressBar);
        }
    }

    public String getCurrentViewType() {
        return currentViewType;
    }

    public void setCurrentViewType(String currentViewType) {
        this.currentViewType = currentViewType;
    }
}
