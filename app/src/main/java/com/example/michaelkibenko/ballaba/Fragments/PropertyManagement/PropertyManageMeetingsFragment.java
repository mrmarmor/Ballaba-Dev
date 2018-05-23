package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyManageMeetingsFragment extends Fragment {
    private final String TAG = PropertyManageMeetingsFragment.class.getSimpleName();

    public PropertyManageMeetingsFragment() {}

    public static PropertyManageFragment newInstance() {
        return new PropertyManageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO make server request and get data + progress dialog
        return inflater.inflate(R.layout.fragment_property_manage_meetings, container, false);
    }

}
