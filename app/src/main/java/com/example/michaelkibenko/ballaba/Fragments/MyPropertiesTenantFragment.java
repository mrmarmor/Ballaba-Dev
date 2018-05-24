package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;

public class MyPropertiesTenantFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_properties_tenant_fragment , container , false);

        return v;
    }
}
