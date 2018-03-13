package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Fragments.SearchPlaceFragment;
import com.example.michaelkibenko.ballaba.Presenters.MapPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

/**
 * Created by User on 13/03/2018.
 */

public class SearchViewPagerAdapter extends FragmentPagerAdapter {
    //public SearchViewPagerAdapter(){}
    private Context context;
    private SearchActivityLayoutBinding binder;

    public SearchViewPagerAdapter(Context context, SearchActivityLayoutBinding binder, FragmentManager fm) {
        super(fm);

        this.context = context;
        this.binder = binder;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return BallabaMapFragment.newInstance();
        } else {
            return PropertiesRecyclerFragment.newInstance("a", "b");
        }
    }

    @Override
    public int getCount() {
        return 2; //NUM_PAGES
    }
}
