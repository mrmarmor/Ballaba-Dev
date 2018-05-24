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

public class PropertyManagementAdapter extends FragmentStatePagerAdapter implements TabLayout.OnTabSelectedListener{
    private final String TAG = PropertyManagementAdapter.class.getSimpleName();

    private Context context;
    private FragmentManager fm;
    private Fragment[] fragments = {PropertyManageInfoFragment.newInstance()
            , PropertyManageFragment.newInstance(), PropertyManageInterestedFragment.newInstance()
            , PropertyManageMeetingsFragment.newInstance()};
    private ActivityPropertyManagementBinding binder;

    final private @IdRes Integer[] TABS = {R.id.propertyManagement_tabs_info,
            R.id.propertyManagement_tabs_manage, R.id.propertyManagement_tabs_interested,
            R.id.propertyManagement_tabs_meetings};

    public PropertyManagementAdapter(Context context, ActivityPropertyManagementBinding binder, FragmentManager fm) {
        super(fm);

        this.context = context;
        this.binder = binder;
        //fragments = new Fragment[4]

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

    /*public void onPropertyManagementTabsStateChange(final int currentTabPosition){
        for (int tab = 0; tab < TABS.length; tab++){
            final TabItem currentTab = binder.propertyManagementRoot.findViewById(TABS[tab]);

            if (tab == currentTabPosition){
                currentTab.setTextColor(context.getResources().getColor(
                        android.R.color.white, context.getTheme()));
            } else {
                //currentTab.setTextColor(context.getResources().getColor(
                //        R.color.myProperty_tabs_textColor, context.getTheme()));
            }
        }

    }*/

    private void initTabsClickListener() {
        TabLayout tabsRoot = binder.propertyManagementRoot.findViewById(R.id.propertyManagement_tabs_root);
        tabsRoot.setOnTabSelectedListener(this);//getTabAt(0).setOnClickListener(this);
        //tabsRoot.findViewById(R.id.propertyManagement_tabs_manage).setOnClickListener(this);
        //tabsRoot.findViewById(R.id.propertyManagement_tabs_interested).setOnClickListener(this);
        //tabsRoot.findViewById(R.id.propertyManagement_tabs_meetings).setOnClickListener(this);
        /*for (@IdRes int tabId : TABS) {
            final TabItem tab = tabsRoot.findViewById(tabId);
            tab.setOnClickListener(this);
        }*/
    }

    /*@Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        binder.propertyManagementViewPager.setCurrentItem(position);
        //onPropertyManagementTabsStateChange(position);
    }*/

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.select();
        binder.propertyManagementViewPager.setCurrentItem(tab.getPosition());
       /* int position = Integer.parseInt(tab.getTag().toString());
        binder.propertyManagementViewPager.setCurrentItem(position);*/
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
