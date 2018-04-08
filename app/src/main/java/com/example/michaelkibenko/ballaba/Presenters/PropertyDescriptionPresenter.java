package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;

import java.util.ArrayList;

/**
 * Created by User on 08/04/2018.
 */

public class PropertyDescriptionPresenter {
    private final String TAG = PropertyDescriptionPresenter.class.getSimpleName();
    public static final String PROPERTY_IMAGE = "Prop_image";

    private Activity activity;
    private ActivityPropertyDescriptionBinding binder;
    private Intent propertyIntent;

    public PropertyDescriptionPresenter(Context context, ActivityPropertyDescriptionBinding binding){
        this.activity = (Activity)context;
        this.binder = binding;

        propertyIntent = activity.getIntent();

        initProperty();
    }

    private void initProperty(){
        View includedMainImage = binder.getRoot().findViewById(R.id.propertyDescription_mainImage);

        Glide.with(activity)
             .load(propertyIntent.getStringExtra(PROPERTY_IMAGE))
             .into((ImageView)includedMainImage.findViewById(R.id.propertyDescription_mainImage));

    }

}
