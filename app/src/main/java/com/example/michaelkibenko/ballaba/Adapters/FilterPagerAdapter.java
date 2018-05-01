package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Entities.FilterDimensions;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.Filter.AttachmentsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.DateOfEntranceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.PriceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.RoomsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.SizeFragment;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.ArrayList;

/**
 * Created by User on 01/04/2018.
 */

public class FilterPagerAdapter extends FragmentStatePagerAdapter implements View.OnClickListener{
    private final String TAG = FilterPagerAdapter.class.getSimpleName();
    private Context context;
    private ActivityMainLayoutBinding binder;
    private FragmentManager fm;
    private ConstraintLayout rootLayout;
    public PriceFragment priceFragment;
    public RoomsFragment roomsFragment;
    public SizeFragment sizeFragment;
    public AttachmentsFragment attachmnetsFragment;
    public DateOfEntranceFragment dateOfEntranceFragment;
    private FilterDimensions filterDimensions;
    private int previousPageIndex = 0;
    //private int previousPage;


    final private @IdRes Integer[] BUTTONS = {R.id.mainActivity_filter_dateOfEntrance_button,
            R.id.mainActivity_filter_attachments_button, R.id.mainActivity_filter_size_button,
            R.id.mainActivity_filter_rooms_button, R.id.mainActivity_filter_price_button};

    final private @IdRes Integer[] DIVIDERS = {R.id.mainActivity_filterButtons_divider_dateOfEntrance,
            R.id.mainActivity_filterButtons_divider_attachments, R.id.mainActivity_filterButtons_divider_size,
            R.id.mainActivity_filterButtons_divider_rooms, R.id.mainActivity_filterButtons_divider_price};

    public FilterPagerAdapter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm, FilterDimensions filterDimensions) {
        super(fm);
        this.context = context;
        this.binder = binder;
        this.fm = fm;
        this.filterDimensions = filterDimensions;
        ArrayList<PropertyAttachmentAddonEntity> items = PropertyAttachmentsAddonsHolder.getInstance().getAttachments();

        priceFragment = PriceFragment.newInstance(this.filterDimensions.getMin_price(), this.filterDimensions.getMax_price());
        roomsFragment = RoomsFragment.newInstance(this.filterDimensions.getMin_rooms(), this.filterDimensions.getMax_rooms());
        sizeFragment = SizeFragment.newInstance(this.filterDimensions.getMin_size(), this.filterDimensions.getMax_size());
        attachmnetsFragment = AttachmentsFragment.newInstance(items);
        dateOfEntranceFragment = DateOfEntranceFragment.newInstance();
        //binder.mainActivityFilterRoot.mainActivityFilterPriceButton
        rootLayout = binder.mainActivityFilterIncluded.mainActivityFilterRoot;
        binder.mainActivityFilterIncluded.mainActivityFilterViewPager.setCurrentItem(0);
        initButtonsClickListener();
    }

    //TODO 1. UI states to white color
    //TODO 2. layout direction
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return dateOfEntranceFragment;

            case 1:
                return attachmnetsFragment;

            case 2:
                return sizeFragment;

            case 3:
                return roomsFragment;

            case 4: default:
                return priceFragment;
        }
    }

    @Override
    public int getCount() {
        return 5; //NUM_PAGES
    }

    public void onFilterButtonsStateChange(final int currentButtonPosition){

        for (int button = 0; button < BUTTONS.length; button++){
            final Button currentButton = rootLayout.findViewById(BUTTONS[button]);
            final View currentDivider = rootLayout.findViewById(DIVIDERS[button]);

            if (button == currentButtonPosition){
                currentButton.setTextColor(context.getResources().getColor(
                        android.R.color.white, context.getTheme()));
                currentDivider.setVisibility(View.VISIBLE);
            } else {
                currentButton.setTextColor(context.getResources().getColor(
                        R.color.filter_textColor_aquaBlue, context.getTheme()));
                currentDivider.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void initButtonsClickListener() {
        for (@IdRes int buttonId : BUTTONS) {
            final Button button = rootLayout.findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        binder.mainActivityFilterIncluded.mainActivityFilterViewPager.setCurrentItem(position);
        onFilterButtonsStateChange(position);
    }

    public void updateFilterDimensions(FilterDimensions filterDimensions){
        sizeFragment.updateRangeBar(filterDimensions.getMin_size(), filterDimensions.getMax_size());
        priceFragment.updateRangeBar(filterDimensions.getMin_price(), filterDimensions.getMax_price());
        roomsFragment.updateRangeBar(filterDimensions.getMin_rooms(), filterDimensions.getMax_rooms());
    }

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
