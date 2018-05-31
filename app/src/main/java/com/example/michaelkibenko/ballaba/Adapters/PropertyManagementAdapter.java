package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
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
    private PropertyManageInterestedFragment propertyManageInterestedFragment = new PropertyManageInterestedFragment();
    private PropertyManageMeetingsFragment propertyManageMeetingsFragment = new PropertyManageMeetingsFragment();

    private Context context;
    private FragmentManager fm;
    private Fragment[] fragments = {PropertyManageInfoFragment.newInstance(1)
            , PropertyManageFragment.newInstance(1),propertyManageInterestedFragment
            , propertyManageMeetingsFragment};
    private RtlViewPager propertyManagementViewPager;
    private TabLayout tabLayout;


    public PropertyManagementAdapter(Context context, RtlViewPager propertyManagementViewPager, FragmentManager fm, TabLayout tabLayout) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.propertyManagementViewPager = propertyManagementViewPager;
        this.tabLayout = tabLayout;

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

    private void initTabsClickListener() {
        tabLayout.addOnTabSelectedListener(this);
        propertyManagementViewPager.addOnPageChangeListener(this);
    }

    // to update viewPager when user clicks a tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //tab.select();
        int position = tab.getPosition();
        propertyManagementViewPager.setCurrentItem(position);
        ((PropertyManagementActivity)context).onStateChange(position);
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    // to update tabs when user swipes the viewPager
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentItem = propertyManagementViewPager.getCurrentItem();
        tabLayout.setScrollPosition(currentItem, 0, true);
        onTabSelected(tabLayout.getTabAt(currentItem));
        //((PropertyManagementActivity)context).onStateChange(currentItem);
        Log.d("VIEWPAGER", "onPageScrolled: " + position);
    }



    @Override
    public void onPageSelected(int position) {
        Log.d("VIEWPAGER", "onPageSelected: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("VIEWPAGER", "onPageSelected: " + state);
    }

    public void checkAllInterested(boolean isCheck, boolean isInterestedTab) {
        if (isInterestedTab){
            propertyManageInterestedFragment.setAllChecked(isCheck);
        }else {
            propertyManageMeetingsFragment.setAllCheck(isCheck);
        }
    }

    public PropertyManageInterestedFragment getPropertyManageInterestedFragment() {
        return propertyManageInterestedFragment;
    }

    public PropertyManageMeetingsFragment getPropertyManageMeetingsFragment() {
        return propertyManageMeetingsFragment;
    }
}
