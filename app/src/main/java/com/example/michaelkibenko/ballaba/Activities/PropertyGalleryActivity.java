package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.example.michaelkibenko.ballaba.Adapters.PropertyGalleryRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyGalleryActivityLayoutBinding;

import java.util.ArrayList;

public class PropertyGalleryActivity extends BaseActivity {

    public static final String PHOTOS_EXTRAS_KEY = "property extras key";
    private PropertyGalleryActivityLayoutBinding binding;
    private PropertyGalleryRecyclerViewAdapter adapter;
    private ArrayList<String> photos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.property_gallery_activity_layout);
        photos = getIntent().getStringArrayListExtra(PHOTOS_EXTRAS_KEY);
        adapter = new PropertyGalleryRecyclerViewAdapter(this, photos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.propertyDescriptionGalleryRecyclerView.setLayoutManager(linearLayoutManager);
        binding.propertyDescriptionGalleryRecyclerView.setAdapter(adapter);
    }
}
