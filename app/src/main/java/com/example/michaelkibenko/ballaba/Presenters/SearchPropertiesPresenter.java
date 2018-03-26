package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertiesRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14/03/2018.
 */

public class SearchPropertiesPresenter extends BasePresenter implements SwipeRefreshLayout.OnRefreshListener{
    private final String TAG = SearchPropertiesPresenter.class.getSimpleName();

    private Context context;
    private PropertiesRecyclerFragment fragment;
    private ActivityMainLayoutBinding binder;
    private PropertiesRecyclerAdapter rvAdapter;
    private RecyclerView rvProperties;
    private List<BallabaPropertyResult> properties;

    public SearchPropertiesPresenter(Context context, /*ActivityMainLayoutBinding binder,*/ String params) {
        this.context = context;
        this.fragment = PropertiesRecyclerFragment.newInstance(params);
        //this.binder = binder;

        //initRecycler is done in PropertiesRecyclerFragment
        //initRecycler();
    }

    private void initRecycler() {
        properties = new ArrayList<BallabaPropertyResult>();
        //TODO moving binder across fragments when a specific widget is in the child fragment and
        //TODO not in the parent activity cause binder not be able to see the specific widget.
        //TODO that is why binder.getRoot() returns null. use findViewById instead!!!
        //rvProperties = (RecyclerView)binder.getRoot().findViewById(R.id.properties_recycler_RV);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        rvAdapter = new PropertiesRecyclerAdapter(context, properties);
        //binder.propertiesRecyclerRV.setLayoutManager(manager);
        //binder.propertiesRecyclerRV.setAdapter(rvAdapter);
        //binder.propertiesRecyclerRootSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        ((SwipeRefreshLayout)binder.getRoot().findViewById(R.id.properties_recycler_root_swipeRefresh)).setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        //myUpdateOperation();
                    }
                }
        );
    }
}
