package com.example.michaelkibenko.ballaba.Presenters;

import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPagerAdapter;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropPresenter {
    private static String TAG = AddPropPresenter.class.getSimpleName();

    private AppCompatActivity activity;
    private static AddPropPresenter instance;
    private ActivityAddPropertyBinding binder;
    private ViewGroup root;
    private BottomSheetDialog bottomSheetDialog;
    private AddPropertyPagerAdapter addPropertyPagerAdapter;
    private HashMap<String, String> data = new HashMap<>();

    public AddPropPresenter(){}
    public AddPropPresenter(AppCompatActivity activity, ActivityAddPropertyBinding binding) {
        this.activity = activity;
        this.binder = binding;

        addPropertyPagerAdapter = new AddPropertyPagerAdapter(binder, activity.getSupportFragmentManager());
        binder.addPropertyViewPager.setAdapter(addPropertyPagerAdapter);
    }

    public static AddPropPresenter getInstance(){
        if (instance == null)
            instance = new AddPropPresenter();

        return instance;
    }

    public void getDataFromFragment(HashMap<String, String> fragmentData, int position){
        data.putAll(fragmentData);
        Log.d(TAG, "elements: "+data.size());

        binder.addPropertyViewPager.setCurrentItem(position + 1);
        activity.invalidateOptionsMenu();
    }

}
