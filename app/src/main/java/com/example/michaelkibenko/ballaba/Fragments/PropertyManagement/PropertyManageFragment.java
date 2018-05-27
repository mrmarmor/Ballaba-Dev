package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;

public class PropertyManageFragment extends Fragment {
    private final String TAG = PropertyManageFragment.class.getSimpleName();

    public PropertyManageFragment() {}

    public static PropertyManageFragment newInstance() {
        return new PropertyManageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO make server request and get data + progress dialog
        View v = inflater.inflate(R.layout.fragment_property_manage, container, false);
        v.findViewById(R.id.propertyManagement_price).findViewById(R.id.propertyDescriptionPrice_toVirtualTour_button)
                .setVisibility(View.GONE);

        return v;
    }


}
