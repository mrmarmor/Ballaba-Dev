package com.example.michaelkibenko.ballaba.Presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.example.michaelkibenko.ballaba.Activities.SearchActivity;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class MapPresenter {

    private Context context;
    private BallabaMapFragment mapFragment;
    private SearchActivityLayoutBinding binding;

    public MapPresenter(Context context, SearchActivityLayoutBinding binder) {
        this.context = context;
        mapFragment = BallabaMapFragment.newInstance();
        this.binding = binder;
    openMapFragment();
}

    private void openMapFragment(){
        FragmentManager fragmentManager = ((SearchActivity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.mapFragmentContainer.getId(), mapFragment);
        fragmentTransaction.commit();
    }
}
