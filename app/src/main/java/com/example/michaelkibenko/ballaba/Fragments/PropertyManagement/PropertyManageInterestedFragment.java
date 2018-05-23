package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;

public class PropertyManageInterestedFragment extends Fragment {
    private final String TAG = PropertyManageInterestedFragment.class.getSimpleName();

    public PropertyManageInterestedFragment() {}

    public static PropertyManageFragment newInstance() {
        return new PropertyManageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_property_manage_interested, container, false);
    }

}
