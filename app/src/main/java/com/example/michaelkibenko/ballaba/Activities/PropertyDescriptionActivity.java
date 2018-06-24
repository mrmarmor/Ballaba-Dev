package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;

public class PropertyDescriptionActivity extends BaseActivity {

    public static final String PROPERTY = "Prop";
    private ActivityPropertyDescriptionBinding binder;
    private PropertyDescriptionPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = DataBindingUtil.setContentView(this, R.layout.activity_property_description);
        presenter = new PropertyDescriptionPresenter(this, binder);
        binder.setPresenter(presenter);

    }


}
