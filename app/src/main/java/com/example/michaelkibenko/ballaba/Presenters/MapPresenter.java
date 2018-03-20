package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;

import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.databinding.MainScreenLayoutBinding;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class MapPresenter {

    private Context context;
    private BallabaMapFragment mapFragment;
    private MainScreenLayoutBinding binding;

    public MapPresenter(Context context, MainScreenLayoutBinding binder) {
        this.context = context;
        mapFragment = BallabaMapFragment.newInstance();
        this.binding = binder;
        openMapFragment();
    }

    public void openMapFragment(){
        /*FragmentManager fragmentManager = ((MainActivity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.googleMap.getId(), mapFragment);
        fragmentTransaction.commit();*/
    }
}
