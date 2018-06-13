package com.example.michaelkibenko.ballaba.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.hrskrs.instadotlib.InstaDotView;

import java.util.ArrayList;

public class PropertiesPhotosPagerAdapter extends FragmentStatePagerAdapter{

    private ArrayList<PropertyImageFragment> items;

    public PropertiesPhotosPagerAdapter(FragmentManager fm, ArrayList<PropertyImageFragment> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }
}
