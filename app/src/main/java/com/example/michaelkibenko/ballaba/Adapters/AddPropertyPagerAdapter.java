package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAddonsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAssetFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropPaymentsFrag;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropertyPagerAdapter extends FragmentStatePagerAdapter /*implements
        View.OnClickListener*//*, ViewPager.OnPageChangeListener*/{
    private final String TAG = FilterPagerAdapter.class.getSimpleName();
    //private Context context;
    private ActivityAddPropertyBinding binder;
    private FragmentManager fm;

    public AddPropertyPagerAdapter(ActivityAddPropertyBinding binder, FragmentManager fm) {
        super(fm);

        this.binder = binder;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: default:
                return AddPropLandlordFrag.newInstance(binder);

            case 1:
                return AddPropAssetFrag.newInstance(binder);

            case 2:
                return AddPropAddonsFrag.newInstance(binder);

            case 3:
                return AddPropPaymentsFrag.newInstance(binder);

        }
    }

    @Override
    public int getCount() {
        return 4; //NUM_PAGES
    }

    /*private void initButtonsClickListener() {
        for (@IdRes int buttonId : BUTTONS) {
            final Button button = rootLayout.findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }*/

    /*@Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        binder.addPropertyViewPager.setCurrentItem(position);
    }*/

   /* private void initButtonsClickListener() {
        for (@IdRes int buttonId : BUTTONS) {
            final Button button = rootLayout.findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }*/

  /*  @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        binder.mainActivityFilterIncluded.mainActivityFilterViewPager.setCurrentItem(position);
        onFilterButtonsStateChange(position);
    }*/

    /*@Override
    public void onPageSelected(int i) {
        Log.d("tag", i+":1");

        int nextPageFactor = previousPageIndex < i? 1 : -1;
        binder.mainActivityFilterIncluded.mainActivityFilterViewPager
                .setCurrentItem(binder.mainActivityFilterIncluded.mainActivityFilterViewPager.getCurrentItem() + nextPageFactor);

        //Use isMovingForward variable anywhere now
        previousPageIndex = i;
    }*/

}