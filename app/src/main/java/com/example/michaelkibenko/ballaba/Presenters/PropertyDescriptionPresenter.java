package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Adapters.AttachmentsRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsExtendedBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionImageBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPriceBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 08/04/2018.
 */

public class PropertyDescriptionPresenter {
    private final String TAG = PropertyDescriptionPresenter.class.getSimpleName();
    public static final String PROPERTY_IMAGE = "Prop_image";

    private Activity activity;
    private ActivityPropertyDescriptionBinding binder;
    private PropertyDescriptionPriceBinding binderPrice;
    private PropertyDescriptionAttachmentsBinding binderAttach;
    private PropertyDescriptionAttachmentsExtendedBinding binderAttachExt;
    //private PropertyDescriptionImageBinding binderImage;
    private Intent propertyIntent;
    private BallabaResponseListener listener;

    public PropertyDescriptionPresenter(Context context, ActivityPropertyDescriptionBinding binding){
        this.activity = (Activity)context;
        this.binder = binding;

        propertyIntent = activity.getIntent();

        initProperty();
    }

    private void initProperty(){
        binderPrice = binder.propertyDescriptionPrice;
        binderAttach = binder.propertyDescriptionAttachments;
        binderAttachExt = binder.propertyDescriptionAttachmentsExtended;
        fetchDataFromServer(propertyIntent.getStringExtra(PropertyDescriptionActivity.PROPERTY));
        ImageView imageFrame = binder.propertyDescriptionImage.propertyDescriptionMainImage;//findViewById(R.id.propertyDescription_mainImage);

        Glide.with(activity)
             .load(propertyIntent.getStringExtra(PROPERTY_IMAGE))
             .into(imageFrame);
    }

    private void fetchDataFromServer(final String PROPERTY_ID){
        ConnectionsManager.getInstance(activity).getPropertyById(PROPERTY_ID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager
                            .getInstance(activity).parsePropertiesFull(((BallabaOkResponse)entity).body);

                    Log.d(TAG, "properties: " + propertyFull.formattedAddress+":"+propertyFull.street);

                    displayDataOnScreen(propertyFull);
                    //callback.resolve(entity);
                }else {
                    Log.d(TAG, "properties: 500" );
                    //callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d(TAG, "properties: error" );

                //callback.reject(entity);
            }
        });
    }

    private void displayDataOnScreen(BallabaPropertyFull propertyFull){
        binderPrice.propertyDescriptionPricePriceTextView.setText("₪" +
                StringUtils.getInstance(true, activity).formattedNumberWithComma(propertyFull.price));
        binderPrice.propertyDescriptionPriceAddressTextView.setText(propertyFull.formattedAddress);
        binderPrice.propertyDescriptionPriceDateOfEntranceTextView.setText(propertyFull.entry_date);
        binderPrice.propertyDescriptionPriceRentalPeriodTextView.setText(propertyFull.rentPeriod);
        binderPrice.propertyDescriptionPriceLandlordNameTextView.setText(propertyFull.landlords.get("first_name"));
        binderPrice.propertyDescriptionPriceLandlordCityTextView.setText(propertyFull.landlords.get("city"));
        binderPrice.propertyDescriptionPriceFullDescriptionTextView.setText(propertyFull.description);

        binderAttach.propertyDescriptionAttachmentsNumberOfRoomsTextView.setText(propertyFull.roomsNumber);
        binderAttach.propertyDescriptionAttachmentsSizeTextView.setText(propertyFull.size);
        binderAttach.propertyDescriptionAttachmentsBathroomsTextView.setText(propertyFull.bathrooms);
        binderAttach.propertyDescriptionAttachmentsToiletsTextView.setText(propertyFull.toilets);

        initAttachmentExtendedRecyclerView(propertyFull);
        //displayDynamicDataOnScreen(propertyFull, propertyFull.attachments);

        Glide.with(activity)
             .load(propertyFull.landlords.get("profile_image"))
             .into(binderPrice.propertyDescriptionPriceLandlordProfileImage);
    }

    private void initAttachmentExtendedRecyclerView(BallabaPropertyFull propertyFull){
        RecyclerView attachmentsRV = binderAttachExt.propertyDescriptionAttachmentsExtendedRecyclerView;
        AttachmentsRecyclerAdapter attachAdapter = new AttachmentsRecyclerAdapter(activity, propertyFull.attachments/*propertyFull.attachments*/);
        StaggeredGridLayoutManager sgLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        attachmentsRV.setLayoutManager(sgLayoutManager);
        attachmentsRV.setAdapter(attachAdapter);
    }

    private void displayDynamicDataOnScreen(BallabaPropertyFull propertyFull, @NonNull HashMap<String, String> attachments){
        for (int i = 0; i < attachments.size(); i++) {
            TextView tv = getDynamicTextViews(attachments.get("attachment_type"), propertyFull);
            addView(((ConstraintLayout)binderAttachExt.getRoot()), tv);
        }
        //TextView tv = binderAttach.propertyDescriptionAttachmentsSizeTextView;
        //tv.setText("dgfdsfg");

        //binderAttachExt.getRoot()

    }
    @BindingAdapter("bind:addView")
    public static void addView(ConstraintLayout rootView, TextView textView) {
        //rootView.removeAllViews();
        rootView.addView(textView);
    }
    public TextView getDynamicTextViews(String attachmentType, BallabaPropertyFull propertyFull) {
        TextView textView = new TextView(activity);
        textView.setText(attachmentType);

        return textView;
    }

    public void onClickContinue(){
        Toast.makeText(activity, "continue", Toast.LENGTH_SHORT).show();
    }

    public void onClickToGallery(){
        Toast.makeText(activity, "to gallery", Toast.LENGTH_SHORT).show();
    }

    public void onClickToStreetView(){
        Toast.makeText(activity, "to street view", Toast.LENGTH_SHORT).show();
    }
}
