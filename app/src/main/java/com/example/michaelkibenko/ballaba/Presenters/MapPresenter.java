package com.example.michaelkibenko.ballaba.Presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.example.michaelkibenko.ballaba.Activities.MapActivity;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.MapLayoutBinding;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class MapPresenter {

    private MapLayoutBinding binding;
    private Context context;
    private BallabaMapFragment mapFragment;

    public MapPresenter(Context context, MapLayoutBinding binding) {
        this.binding = binding;
        this.context = context;
        mapFragment = BallabaMapFragment.newInstance();
        openMapFragment();
    }

    private void openMapFragment(){
        FragmentManager fragmentManager = ((MapActivity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.MapFragmentContainer.getId(), mapFragment);
        fragmentTransaction.commit();
    }
}
