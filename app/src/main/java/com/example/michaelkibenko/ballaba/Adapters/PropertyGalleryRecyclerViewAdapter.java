package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.michaelkibenko.ballaba.Adapters.PropertyGalleryRecyclerViewAdapter.ITEM_TYPES.HEADER;
import static com.example.michaelkibenko.ballaba.Adapters.PropertyGalleryRecyclerViewAdapter.ITEM_TYPES.ITEM;

public class PropertyGalleryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @interface ITEM_TYPES {
        int HEADER = 1;
        int ITEM = 2;
    }
    private final Context context;
    private final ArrayList<HashMap<String, String>> items;
    private LayoutInflater inflater;
    private float linearWidth, linearHeight, gridSize;
    private @PropertyGalleryActivity.VIEW_TYPES String currentViewType;
    private ArrayList<GalleryItem> realItems;

    public PropertyGalleryRecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> items, @PropertyGalleryActivity.VIEW_TYPES String currentViewType){
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearWidth = context.getResources().getDimension(R.dimen.property_gallery_item_width_linear);
        linearHeight = context.getResources().getDimension(R.dimen.property_gallery_item_height_linear);
        gridSize = context.getResources().getDimension(R.dimen.property_gallery_item_size_grid);
        this.currentViewType = currentViewType;
        realItems = generateRealItems();
    }

    @Override
    public int getItemViewType(int position) {
        return realItems.get(position).itemType;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(realItems.get(position).itemType == ITEM) {
            final PropertyGalleryItemViewHolder itemHolder = (PropertyGalleryItemViewHolder)holder;
            final String photoUrl = realItems.get(position).body;
            Glide.with(context).load(photoUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    //TODO set x icon
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    itemHolder.photo.setImageDrawable(resource);
                    itemHolder.progressBar.setVisibility(View.GONE);
                    return true;
                }
            }).into(itemHolder.photo);

            itemHolder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PropertyGalleryActivity) context).showPhotoFullScreen(photoUrl);
                }
            });
        }else{
            PropertyGalleryHeaderViewHolder tagHeader = (PropertyGalleryHeaderViewHolder)holder;
            tagHeader.tagTV.setText(realItems.get(position).body);
        }
    }

    @Override
    public int getItemCount() {
        return realItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            View v = inflater.inflate(R.layout.gallery_photo_item, parent, false);
            return new PropertyGalleryItemViewHolder(v);
        }else {
            View v = inflater.inflate(R.layout.gallery_item_tag_layout, parent, false);
            return new PropertyGalleryHeaderViewHolder(v);
        }
    }

    public class PropertyGalleryItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public ConstraintLayout layout;
        public ProgressBar progressBar;
        public PropertyGalleryItemViewHolder(View itemView) {
            super(itemView);
            layout = (ConstraintLayout)itemView;
            photo = itemView.findViewById(R.id.property_photo);
            progressBar = itemView.findViewById(R.id.galleryPhoto_progressBar);
        }
    }

    public class PropertyGalleryHeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView tagTV;
        public PropertyGalleryHeaderViewHolder(View itemView) {
            super(itemView);
            tagTV = (TextView)itemView.findViewById(R.id.property_gallery_tag_item);
        }
    }
    public String getCurrentViewType() {
        return currentViewType;
    }

    public void setCurrentViewType(String currentViewType) {
        this.currentViewType = currentViewType;
    }

    private ArrayList<GalleryItem> generateRealItems(){
        ArrayList<GalleryItem> returnable = new ArrayList<>();
        for (HashMap<String, String> item : items) {
            if(isHasThisTag(returnable, item.get("tags"))){
                returnable.add(new GalleryItem(ITEM_TYPES.ITEM, item.get("photo_url")));
            }else{
                returnable.add(new GalleryItem(ITEM_TYPES.HEADER, item.get("tags")));
                returnable.add(new GalleryItem(ITEM_TYPES.ITEM, item.get("photo_url")));
            }
        }

        return returnable;
    }

    private boolean isHasThisTag(ArrayList<GalleryItem> items, String tag){
        if(tag == null){
            return true;
        }
        for (GalleryItem item : items) {
            if(item.body.equals(tag)){
                return true;
            }
        }
        return false;
    }

    public class GalleryItem{
       public @ITEM_TYPES int itemType;
       public String body;
        public GalleryItem(int itemType, String body) {
            this.itemType = itemType;
            this.body = body;
        }
    }
}
