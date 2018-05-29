package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageInfoFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageInterestedFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageMeetingsFragment;

/**
 * Created by User on 23/05/2018.
 */

public class PropertyManagementAdapter extends FragmentStatePagerAdapter
        implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private final String TAG = PropertyManagementAdapter.class.getSimpleName();

    private Context context;
    //private FragmentManager fm;
    private Fragment[] fragments;
    private RtlViewPager propertyManagementViewPager;
    private int propertyId;
    private TabLayout tabLayout;

    public PropertyManagementAdapter(Context context, RtlViewPager propertyManagementViewPager
            , FragmentManager fm, TabLayout tabLayout, int propertyId) {
        super(fm);
        //this.fm = fm;
        this.context = context;
        this.propertyManagementViewPager = propertyManagementViewPager;
        this.tabLayout = tabLayout;
        this.propertyId = propertyId;

        setFragments(propertyId);
        initTabsClickListener();
    }

    private void setFragments(int id){
        fragments = new Fragment[]{
                PropertyManageInfoFragment.newInstance(id), PropertyManageFragment.newInstance(id)
                , new PropertyManageInterestedFragment(), PropertyManageMeetingsFragment.newInstance()};
    }

    private void initTabsClickListener() {
        tabLayout.addOnTabSelectedListener(this);
        propertyManagementViewPager.addOnPageChangeListener(this);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 4; //NUM_PAGES
    }

    // to update viewPager when user clicks a tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.select();
        propertyManagementViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    // to update tabs when user swipes the viewPager
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tabLayout.setScrollPosition(propertyManagementViewPager.getCurrentItem(), 0, true);
    }
    @Override
    public void onPageSelected(int position) {}
    @Override
    public void onPageScrollStateChanged(int state) {}
}
