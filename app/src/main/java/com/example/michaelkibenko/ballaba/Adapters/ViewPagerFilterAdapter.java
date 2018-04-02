package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

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
    private final String TAG = ViewPagerFilterAdapter.class.getSimpleName();
    private Context context;
    private ActivityMainLayoutBinding binder;
    private FragmentManager fm;
    private ConstraintLayout rootLayout;
    //private int previousPage;

    private @IdRes Integer[] buttons = {R.id.mainActivity_filter_price_button,
        R.id.mainActivity_filter_rooms_button, R.id.mainActivity_filter_size_button,
        R.id.mainActivity_filter_attachments_button, R.id.mainActivity_filter_dateOfEntrance_button};
    private @IdRes Integer[] views = {R.id.mainActivity_filterButtons_divider_price,
        R.id.mainActivity_filterButtons_divider_rooms, R.id.mainActivity_filterButtons_divider_size,
        R.id.mainActivity_filterButtons_divider_attachments, R.id.mainActivity_filterButtons_divider_dateOfEntrance};

    public ViewPagerFilterAdapter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm) {
        super(fm);

        this.context = context;
        this.binder = binder;
        this.fm = fm;
        //binder.mainActivityFilterRoot.mainActivityFilterPriceButton
        rootLayout = binder.mainActivityFilterIncluded.mainActivityFilterRoot;
        initButtonsClickListener();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, position+":"+
                binder.mainActivityFilterIncluded.mainActivityFilterViewPager.getCurrentItem());
        onFilterButtonsStateChange(binder.mainActivityFilterIncluded.mainActivityFilterViewPager.getCurrentItem());
        //previousPage = position;

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

    private void onFilterButtonsStateChange(final int currentButtonPosition){

        for (int button = 0; button < buttons.length; button++){
            final Button currentButton = rootLayout.findViewById(buttons[button]);
            final View currentDivider = rootLayout.findViewById(views[button]);

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
        for (@IdRes int buttonId : buttons) {

            final Button button = rootLayout.findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Log.d("tag", position+":"+
                            binder.mainActivityFilterIncluded.mainActivityFilterViewPager.getCurrentItem());
                    binder.mainActivityFilterIncluded.mainActivityFilterViewPager.setCurrentItem(position);

                    //getItem(Integer.parseInt(v.getTag().toString()));
                    //onFilterButtonsStateChange(i);
                }
            });
        }
    }
}
