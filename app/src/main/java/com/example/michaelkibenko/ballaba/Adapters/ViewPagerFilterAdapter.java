package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.AttachmentsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.DateOfEntranceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.PriceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.RoomsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.SizeFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

/**
 * Created by User on 01/04/2018.
 */

public class ViewPagerFilterAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private ActivityMainLayoutBinding binder;
    private FragmentManager fm;

    public ViewPagerFilterAdapter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm) {
        super(fm);

        this.context = context;
        this.binder = binder;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: default:
                return PriceFragment.getInstance();//fm.findFragmentById(R.id.priceFragment_rootLayout);

            case 1:
                return RoomsFragment.getInstance();//fm.findFragmentById(R.id.roomsFragment_rootLayout);

            case 2:
                return SizeFragment.getInstance();//fm.findFragmentById(R.id.sizeFragment_rootLayout);

            case 3:
                return AttachmentsFragment.getInstance(context, binder);//fm.findFragmentById(R.id.attachmentsFragment_rootLayout);

            case 4:
                return DateOfEntranceFragment.getInstance();//fm.findFragmentById(R.id.dateOfEntranceFragment_rootLayout);
        }
    }

    @Override
    public int getCount() {
        return 5; //NUM_PAGES
    }
}
