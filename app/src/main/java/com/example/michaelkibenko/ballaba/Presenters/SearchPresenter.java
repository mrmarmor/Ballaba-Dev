package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;

import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class SearchPresenter extends BasePresenter {

    private final SearchActivityLayoutBinding binder;
    private MapPresenter mapPresenter;
    private Context context;
    public SearchPresenter(Context context, SearchActivityLayoutBinding binder){
        this.binder = binder;
        this.context = context;
        mapPresenter = new MapPresenter(this.context, this.binder);
        mapPresenter.openMapFragment();
    }
}
