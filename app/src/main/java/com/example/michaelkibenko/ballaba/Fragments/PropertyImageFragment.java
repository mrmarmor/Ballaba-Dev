package com.example.michaelkibenko.ballaba.Fragments;

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
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWelcomeActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;

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
        final ImageView photo = v.findViewById(R.id.property_photo_IV);
        final ProgressBar progressBar = v.findViewById(R.id.property_photo_loading_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(context).load(photoUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                photo.setImageDrawable(context.getDrawable(R.drawable.photo_home_grey_24));
                progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                photo.setImageDrawable(resource);
                progressBar.setVisibility(View.GONE);
                return true;
            }
        }).into(photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullProperty(propertyId, position);
            }
        });
        return v;
    }

    private void showFullProperty(final String propertyId, final int position){
        BallabaUser user = BallabaUserManager.getInstance().getUser();

        user.userCurrentPropertyObservedID = propertyId;
        boolean isScored = user.getIs_scored();

        if (isScored) {
            ConnectionsManager.getInstance(getActivity()).getPropertyPermission(propertyId, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Intent intent = new Intent(context, PropertyDescriptionActivity.class);

                    intent.putExtra(PropertyDescriptionPresenter.PROPERTY_POSITION, position);
                    intent.putExtra(PropertyDescriptionActivity.PROPERTY, propertyId);
                    intent.putExtra(PropertyDescriptionPresenter.PROPERTY_IMAGE, photoUrl);

                    context.startActivity(intent);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    if (((BallabaErrorResponse)entity).statusCode == 403){
                        // TODO: 07/06/2018 INSERT X and get guarantor
                    }else {
                        // TODO: 07/06/2018 ERROR FLOW
                    }
                }
            });

        }else {
            startActivity(new Intent(getActivity() , ScoringWelcomeActivity.class));
        }
    }

}
