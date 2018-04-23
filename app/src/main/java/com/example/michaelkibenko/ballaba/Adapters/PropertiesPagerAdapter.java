package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageButton;

import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
//import com.example.michaelkibenko.ballaba.Fragments.SearchPlaceFragment;
import com.example.michaelkibenko.ballaba.Presenters.MapPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private ActivityMainLayoutBinding binder;
    private PropertiesRecyclerFragment propertiesRecyclerFragment;
    private BallabaMapFragment mapFragment;

    public PropertiesPagerAdapter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm, PropertiesRecyclerFragment propertiesRecyclerFragment) {
        super(fm);

        this.context = context;
        this.binder = binder;
        this.propertiesRecyclerFragment = propertiesRecyclerFragment;
        this.mapFragment = BallabaMapFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return mapFragment;
        } else {
            return propertiesRecyclerFragment;
        }
    }

    @Override
    public int getCount() {
        return 2; //NUM_PAGES
    }

    public PropertiesRecyclerFragment getPropertiesRecyclerFragment() {
        return propertiesRecyclerFragment;
    }

    public BallabaMapFragment getMapFragment() {
        return mapFragment;
    }
}
