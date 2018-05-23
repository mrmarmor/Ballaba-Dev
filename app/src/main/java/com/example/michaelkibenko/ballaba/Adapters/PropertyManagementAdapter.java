package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

public class PropertyManagementAdapter extends FragmentStatePagerAdapter implements Button.OnClickListener{
    private final String TAG = PropertyManagementAdapter.class.getSimpleName();

    private Context context;
    private FragmentManager fm;
    private Fragment[] fragments = new Fragment[]{PropertyManageInfoFragment.newInstance()
        , PropertyManageFragment.newInstance(), PropertyManageInterestedFragment.newInstance()
        , PropertyManageMeetingsFragment.newInstance()};
    private ActivityPropertyManagementBinding binder;

    final private @IdRes Integer[] TABS = {R.id.propertyManagement_tabs_info_button,
            R.id.propertyManagement_tabs_manage_button, R.id.propertyManagement_tabs_interested_button,
            R.id.propertyManagement_tabs_meetings_button};

    public PropertyManagementAdapter(Context context, ActivityPropertyManagementBinding binder, FragmentManager fm) {
        super(fm);

        this.context = context;
        this.binder = binder;
        fragments = new Fragment[]{};

        initButtonsClickListener();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 4; //NUM_PAGES
    }

    public void onPropertyManagementButtonsStateChange(final int currentButtonPosition){
        for (int button = 0; button < TABS.length; button++){
            final Button currentButton = binder.propertyManagementRoot.findViewById(TABS[button]);

            if (button == currentButtonPosition){
                currentButton.setTextColor(context.getResources().getColor(
                        android.R.color.white, context.getTheme()));
            } else {
                currentButton.setTextColor(context.getResources().getColor(
                        R.color.filter_textColor_aquaBlue, context.getTheme()));
            }
        }

    }

    private void initButtonsClickListener() {
        for (@IdRes int buttonId : TABS) {
            final Button button = binder.propertyManagementRoot.findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        binder.propertyManagementViewPager.setCurrentItem(position);
        onPropertyManagementButtonsStateChange(position);
    }

}
