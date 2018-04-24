package com.example.michaelkibenko.ballaba.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;

public class PropertyImageFragment extends Fragment {
    public static String PHOTO_URL_KEY = "PROPERTY_URL_KEY";
    public static String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";

    private Context context;
    private String propertyId;
    private String photoUrl;
    public static PropertyImageFragment newInstance(String photoUrl, String propertyId) {
        Bundle args = new Bundle();
        args.putString(PHOTO_URL_KEY, photoUrl);
        args.putString(PROPERTY_ID_KEY, propertyId);
        PropertyImageFragment fragment = new PropertyImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prop_image_layout, container, false);
        Bundle args = getArguments();
        photoUrl = args.getString(PHOTO_URL_KEY);
        propertyId = args.getString(PROPERTY_ID_KEY);
        ImageView photo = (ImageView)v.findViewById(R.id.property_photo_IV);
        Glide.with(context).load(photoUrl).into(photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullProperty(propertyId);
            }
        });
        return v;
    }

    private void showFullProperty(String propertyId){
        Intent intent = new Intent(context, PropertyDescriptionActivity.class);
        intent.putExtra(PropertyDescriptionActivity.PROPERTY, propertyId);
        intent.putExtra(PropertyDescriptionPresenter.PROPERTY_IMAGE
                , photoUrl);
        context.startActivity(intent);
    }
}
