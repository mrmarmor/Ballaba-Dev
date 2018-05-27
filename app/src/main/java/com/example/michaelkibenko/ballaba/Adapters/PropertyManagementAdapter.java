package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Property;
import android.view.View;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAddonsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAssetFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropMeetingsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropPaymentsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageInfoFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageInterestedFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageMeetingsFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyManagementBinding;

/**
 * Created by User on 23/05/2018.
 */

public class PropertyManagementAdapter extends FragmentStatePagerAdapter
        implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private final String TAG = PropertyManagementAdapter.class.getSimpleName();

    private Context context;
    private FragmentManager fm;
    //private String id;
    private Fragment[] fragments;
    private ActivityPropertyManagementBinding binder;

    public PropertyManagementAdapter(Context context, ActivityPropertyManagementBinding binder, FragmentManager fm, int id) {
        super(fm);

        this.context = context;
        this.binder = binder;
        //this.id = id;

        setFragments(id);
        initTabsClickListener();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 4; //NUM_PAGES
    }

    private void setFragments(int id){
        fragments = new Fragment[]{PropertyManageInfoFragment.newInstance(id)
                , PropertyManageFragment.newInstance(), PropertyManageInterestedFragment.newInstance()
                , PropertyManageMeetingsFragment.newInstance()};
    }

    private void initTabsClickListener() {
        TabLayout tabsRoot = binder.propertyManagementRoot.findViewById(R.id.propertyManagement_tabs_root);
        tabsRoot.setOnTabSelectedListener(this);
        binder.propertyManagementViewPager.addOnPageChangeListener(this);
    }

    // to update viewPager when user clicks a tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.select();
        binder.propertyManagementViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    // to update tabs when user swipes the viewPager
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        binder.propertyManagementTabsRoot.setScrollPosition(binder.propertyManagementViewPager.getCurrentItem(), 0, true);
    }
    @Override
    public void onPageSelected(int position) {}
    @Override
    public void onPageScrollStateChanged(int state) {}
}
