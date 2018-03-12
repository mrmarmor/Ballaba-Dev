package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.michaelkibenko.ballaba.Presenters.SearchPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class SearchActivity extends FragmentActivity{

    private SearchActivityLayoutBinding binder;
    private SearchPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.search_activity_layout);
        presenter = new SearchPresenter(this, binder);
    }
}
