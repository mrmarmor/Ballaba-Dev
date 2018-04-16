package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;

import java.util.ArrayList;

public class PropertyDescriptionActivity extends BaseActivity {

    public static final String PROPERTY = "Prop";

    private ActivityPropertyDescriptionBinding binder;
    public PropertyDescriptionPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = DataBindingUtil.setContentView(this, R.layout.activity_property_description);
        presenter = new PropertyDescriptionPresenter(this, binder);
        binder.setPresenter(presenter);

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

        //TODO states
        //binder.propertyDescriptionMapFragmentFullContainer.setVisibility(View.GONE);
        //binder.propertyDescriptionMapFragmentContainer.setVisibility(View.VISIBLE);
    }*/
}
