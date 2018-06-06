package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;

import static com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter.REQ_CODE_SHOW_FULL_PROPERTY;

public class PropertyImageFragment extends Fragment {
    public static String PHOTO_URL_KEY = "PROPERTY_URL_KEY";
    public static String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";

    private Context context;
    private String propertyId;
    private String photoUrl;
    public static PropertyImageFragment newInstance(String photoUrl, String propertyId, int position) {
        Bundle args = new Bundle();
        args.putString(PHOTO_URL_KEY, photoUrl);
        args.putString(PROPERTY_ID_KEY, propertyId);
        args.putInt(PropertyDescriptionPresenter.PROPERTY_POSITION, position);
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
        final int position = args.getInt(PropertyDescriptionPresenter.PROPERTY_POSITION);
        ImageView photo = (ImageView)v.findViewById(R.id.property_photo_IV);
        Glide.with(context).load(photoUrl).into(photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullProperty(propertyId, position);
            }
        });
        return v;
    }

    private void showFullProperty(final String propertyId, final int position){
        Intent intent = new Intent(context, PropertyDescriptionActivity.class);

        intent.putExtra(PropertyDescriptionPresenter.PROPERTY_POSITION, position);
        intent.putExtra(PropertyDescriptionActivity.PROPERTY, propertyId);
        intent.putExtra(PropertyDescriptionPresenter.PROPERTY_IMAGE, photoUrl);

        context.startActivity(intent);
    }

}
