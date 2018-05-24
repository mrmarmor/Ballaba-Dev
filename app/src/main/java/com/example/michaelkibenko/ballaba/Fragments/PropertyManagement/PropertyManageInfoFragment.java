package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;

public class PropertyManageInfoFragment extends Fragment {
    private final String TAG = PropertyManageInfoFragment.class.getSimpleName();

    public PropertyManageInfoFragment() {}

    public static PropertyManageInfoFragment newInstance(/*String min, String max*/) {
        PropertyManageInfoFragment fragment = new PropertyManageInfoFragment();
        /*Bundle args = new Bundle();
        args.putString(FILTER_PRICE_MIN, min);
        args.putString(FILTER_PRICE_MAX, max);
        fragment.setArguments(args);*/
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchDataFromServer();

        //TODO make server request and get data + progress dialog
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_property_manage_info, container, false);

        return v;
    }

    private void fetchDataFromServer(){

    }

}
