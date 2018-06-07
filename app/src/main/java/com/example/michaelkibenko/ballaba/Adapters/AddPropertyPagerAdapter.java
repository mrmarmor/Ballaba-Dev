package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAddonsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAssetFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropMeetingsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropPaymentsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropertyPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = AddPropertyPagerAdapter.class.getSimpleName();

    private Fragment[] fragments;
    private AddPropEditPhotoFrag addPropEditPhotoFrag = new AddPropEditPhotoFrag();

    public AddPropertyPagerAdapter(ActivityAddPropertyBinding binder, FragmentManager fm) {
        super(fm);

        fragments = new Fragment[]{
                  AddPropLandlordFrag.newInstance().setMainBinder(binder), AddPropAssetFrag.newInstance().setMainBinder(binder)
                , AddPropAddonsFrag.newInstance(binder).setMainBinder(binder), AddPropPaymentsFrag.newInstance().setMainBinder(binder)
                , AddPropTakePhotoFrag.newInstance().setMainBinder(binder), addPropEditPhotoFrag.setMainBinder(binder)
                , AddPropMeetingsFrag.newInstance() /*AddPropPreviewFrag may be added here. we can take it from PropertyDescription*/};
    }

    //AddPropEditPhotoFrag needs also a photo, so i need to return it with a bundle.
    public void setData(Uri photo, String[] orientations){
        Bundle bundle = new Bundle();
        bundle.putString(AddPropEditPhotoFrag.FIRST_PHOTO , photo.toString());
        bundle.putStringArray(AddPropEditPhotoFrag.ORIENTATIONS, orientations);
        addPropEditPhotoFrag.setArguments(bundle);
        fragments[5] = addPropEditPhotoFrag;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] != null)
            return fragments[position];
        else
            return fragments[0];
    }

    @Override
    public int getCount() {
        return 7; //NUM_PAGES
    }

}
