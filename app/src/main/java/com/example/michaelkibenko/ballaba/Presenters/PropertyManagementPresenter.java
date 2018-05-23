package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.michaelkibenko.ballaba.Adapters.PropertyManagementAdapter;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyManagementBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;

/**
 * Created by User on 23/05/2018.
 */

public class PropertyManagementPresenter extends BasePresenter {
    private final String TAG = PropertyManagementPresenter.class.getSimpleName();

    private static PropertyManagementPresenter instance;
    private Context context;
    private ActivityPropertyManagementBinding binder;

    public PropertyManagementPresenter(Context context, ActivityPropertyManagementBinding binder){
        this.context = context;
        this.binder = binder;

        PropertyManagementAdapter adapter = new PropertyManagementAdapter(context, binder
                , ((AppCompatActivity)context).getSupportFragmentManager());
        binder.propertyManagementViewPager.setAdapter(adapter);
    }

    public static PropertyManagementPresenter getInstance(Context context, ActivityPropertyManagementBinding binder) {
        if(instance == null){
            instance = new PropertyManagementPresenter(context, binder);
        }
        return instance;
    }
}
