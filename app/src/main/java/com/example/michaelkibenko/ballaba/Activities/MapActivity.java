package com.example.michaelkibenko.ballaba.Activities;

import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.michaelkibenko.ballaba.Presenters.MapPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.MapLayoutBinding;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class MapActivity extends FragmentActivity {

    private MapLayoutBinding binding;
    private MapPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.map_layout);
        presenter = new MapPresenter(this, binding);
    }
}
