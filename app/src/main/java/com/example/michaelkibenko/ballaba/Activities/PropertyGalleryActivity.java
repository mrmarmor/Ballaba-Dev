package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Adapters.PropertyGalleryRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyGalleryActivityLayoutBinding;

import java.util.ArrayList;

import static com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity.VIEW_TYPES.GRID;
import static com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity.VIEW_TYPES.LINEAR;

public class PropertyGalleryActivity extends BaseActivity {

    public @interface VIEW_TYPES{
        String GRID = "grid";
        String LINEAR = "linear";
    }

    private PropertyGalleryActivityLayoutBinding binding;
    private PropertyGalleryRecyclerViewAdapter adapter;
    private ConstraintLayout virtualTour;
    private BallabaPropertyFull propertyFull;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private @VIEW_TYPES String currentViewType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.property_gallery_activity_layout);
        propertyFull = BallabaSearchPropertiesManager.getInstance(this).getPropertyFull();
        currentViewType = SharedPreferencesManager.getInstance(this).getGalleryViewType(null);
        if(currentViewType == null){
            SharedPreferencesManager.getInstance(this).putGalleryViewType(VIEW_TYPES.LINEAR);
            currentViewType = LINEAR;
        }
        adapter = new PropertyGalleryRecyclerViewAdapter(this, propertyFull.photos, currentViewType);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        changeViewType(currentViewType);
        binding.propertyDescriptionGalleryRecyclerView.setAdapter(adapter);
        binding.propertyDescriptionGalleryRecyclerView.setHasFixedSize(true);
        virtualTour = (ConstraintLayout) findViewById(R.id.virtualTourLayout);
        virtualTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyGalleryActivity.this, VirtualTourActivity.class);
                startActivity(intent);
            }
        });

        binding.galleryChangeViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentViewType.equals(GRID)){
                    changeViewType(LINEAR);
                }else{
                    changeViewType(GRID);
                }
            }
        });

        binding.propertyPrice.setText(propertyFull.price);
        binding.galleryXWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeViewType(@VIEW_TYPES String viewType){
        if(!viewType.equals(currentViewType)){
            SharedPreferencesManager.getInstance(this).putGalleryViewType(viewType);
            currentViewType = viewType;
        }
        adapter.setCurrentViewType(viewType);
        if(viewType.equals(GRID)){
            binding.propertyDescriptionGalleryRecyclerView.setLayoutManager(gridLayoutManager);
            binding.galleryChangeViewType.setBackground(getResources().getDrawable(R.drawable.list_white_24, getTheme()));
        }else{
            binding.propertyDescriptionGalleryRecyclerView.setLayoutManager(linearLayoutManager);
            binding.galleryChangeViewType.setBackground(getResources().getDrawable(R.drawable.gallery_white_24, getTheme()));
        }

        adapter.notifyDataSetChanged();
    }

    public void showPhotoFullScreen(String photoUrl){
        binding.fullPhotoContainer.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        Glide.with(this).load(photoUrl).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                //TODO set x icon
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.photoContainer.setImageDrawable(resource);
                binding.fullPhotoProgressBar.setVisibility(View.GONE);
                return true;
            }
        }).into(binding.photoContainer);

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fullPhotoContainer.setVisibility(View.GONE);
            }
        });
    }

}
