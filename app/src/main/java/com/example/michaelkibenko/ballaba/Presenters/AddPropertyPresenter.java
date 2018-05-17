package com.example.michaelkibenko.ballaba.Presenters;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPagerAdapter;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropertyPresenter {
    private static String TAG = AddPropertyPresenter.class.getSimpleName();

    private AppCompatActivity activity;
    private static AddPropertyPresenter instance;
    private ActivityAddPropertyBinding binder;
    //private ViewGroup root;
    //private BottomSheetDialog bottomSheetDialog;
    private AddPropertyPagerAdapter addPropertyPagerAdapter;
    //private HashMap<String, String> data = new HashMap<>();

    public AddPropertyPresenter(){}
    public AddPropertyPresenter(AppCompatActivity activity, ActivityAddPropertyBinding binding) {
        this.activity = activity;
        this.binder = binding;

        addPropertyPagerAdapter = new AddPropertyPagerAdapter(binder, activity.getSupportFragmentManager());
        binder.addPropertyViewPager.setAdapter(addPropertyPagerAdapter);

        //TODO testing
        binding.addPropertyViewPager.setCurrentItem(0);
        //TODO end of testing

        binder.addPropertyViewPager.setOffscreenPageLimit(0);
    }

    public static AddPropertyPresenter getInstance(AppCompatActivity activity, ActivityAddPropertyBinding binding){
        if (instance == null)
            instance = new AddPropertyPresenter(activity, binding);

        return instance;
    }

    /*public void sendDataToActivity(String image, int position){
        //activity.getSupportFragmentManager().findFragmentById(R.id.b).getLayoutInflater().inflate();
        AddPropEditPhotoFrag.newInstance(binder).setImage(image);
        onNextViewPagerItem(position);
    }*/

    public void onNextViewPagerItem(int position){
        binder.addPropertyViewPager.setCurrentItem(position + 1, false);
        activity.invalidateOptionsMenu();
    }
   /* public void getChipsFromFragment(@Nullable HashMap<String, ArrayList<String>> fragmentData, int position){
        //TODO data.putAll(fragmentData);
        Log.d(TAG, "elements: "+data.size());

        binder.addPropertyViewPager.setCurrentItem(position + 1);
        activity.invalidateOptionsMenu();
    }*/

}