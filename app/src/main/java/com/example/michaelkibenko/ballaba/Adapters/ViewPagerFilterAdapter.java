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
                //animateScreen(false);
                return SizeFragment.getInstance();//fm.findFragmentById(R.id.sizeFragment_rootLayout);

            case 3:
                // animateScreen(true);
                return AttachmentsFragment.getInstance(context, binder);//fm.findFragmentById(R.id.attachmentsFragment_rootLayout);

            case 4:
                //animateScreen(false);
                return DateOfEntranceFragment.getInstance();//fm.findFragmentById(R.id.dateOfEntranceFragment_rootLayout);
        }
    }

    @Override
    public int getCount() {
        return 5; //NUM_PAGES
    }

    private void animateScreen(boolean hasFocus){
        Guideline guideline = binder.mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = hasFocus? 0 : -50;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = binder.mainActivityFilterRoot;
        ConstraintSet constraintSetFullHeight = new ConstraintSet();
        constraintSetFullHeight.clone(context, R.layout.filter_screen_full_height);
        constraintSetFullHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1

        /*ConstraintSet constraintSetHalfHeight = new ConstraintSet();
        constraintSetHalfHeight.clone(rootLayoutHalfHeight);
        ConstraintSet constraintSet = hasFocus? constraintSetFullHeight : constraintSetHalfHeight;
        ConstraintLayout constraintLayout = hasFocus? rootLayoutFullHeight : rootLayoutHalfHeight;
      */  //constraintSet.centerVertically(R.id.tv, 0);
        //constraintSetFullHeight.connect(R.id.fragmentAttachments_rootLayout, ConstraintSet.TOP, R.id.mainActivity_rootLayout, ConstraintSet.TOP,0);

        //constraintSet.setVerticalBias(R.id.tv, 1.5f);
        //constraintSet.setHorizontalBias(R.id.tv, 0.5f);

        AutoTransition transition = new AutoTransition();
        transition.setDuration(100);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());



        TransitionManager.beginDelayedTransition(constraintLayout, transition);
        constraintSetFullHeight.applyTo(constraintLayout);
    }

}
