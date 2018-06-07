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

        initViewPager();
    }

    public static AddPropertyPresenter getInstance(AppCompatActivity activity, ActivityAddPropertyBinding binding){
        if (instance == null)
            instance = new AddPropertyPresenter(activity, binding);

        return instance;
    }

    private void initViewPager(){
        addPropertyPagerAdapter = new AddPropertyPagerAdapter(binder, activity.getSupportFragmentManager());
        binder.addPropertyViewPager.setAdapter(addPropertyPagerAdapter);
        binder.addPropertyViewPager.setCurrentItem(0);
        binder.addPropertyViewPager.setOffscreenPageLimit(0);
    }

    /*public void sendDataToActivity(String image, int position){
        //activity.getSupportFragmentManager().findFragmentById(R.id.b).getLayoutInflater().inflate();
        AddPropEditPhotoFrag.newInstance(binder).setImage(image);
        onNextViewPagerItem(position);
    }*/

    public void setViewPagerItem(final int position){
        /*TODO if user go from first item to second item and returns to the first, the pagerAdapter method setCurrentItem does not move him
          TODO to second. i think this is because he was already there and the adapter already set this item.
          TODO the solution is: launching second item after a delay with a thread as below.*/
        binder.addPropertyViewPager.setAdapter(addPropertyPagerAdapter);
        binder.addPropertyViewPager.setOffscreenPageLimit(2);
        binder.addPropertyViewPager.post(new Runnable() {
            @Override
            public void run() {
                binder.addPropertyViewPager.setCurrentItem(position);
                activity.invalidateOptionsMenu();
            }
        });
    }
   /* public void getChipsFromFragment(@Nullable HashMap<String, ArrayList<String>> fragmentData, int position){
        //TODO data.putAll(fragmentData);
        Log.d(TAG, "elements: "+data.size());

        binder.addPropertyViewPager.setCurrentItem(position + 1);
        activity.invalidateOptionsMenu();
    }*/

}