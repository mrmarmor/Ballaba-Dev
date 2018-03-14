package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by User on 14/03/2018.
 */

public class SearchPropertiesPresenter {
    private Context context;
    private PropertiesRecyclerFragment fragment;
    private SearchActivityLayoutBinding binder;
    private PropertiesRecyclerAdapter rvAdapter;
    private RecyclerView rvProperties;
    private List<BallabaProperty> properties;

    public SearchPropertiesPresenter(Context context, SearchActivityLayoutBinding binder, String a) {
        this.context = context;
        this.fragment = PropertiesRecyclerFragment.newInstance(binder, a);
        this.binder = binder;

        initRecycler();
    }

    private void initRecycler() {
        properties = new ArrayList<BallabaProperty>();
        //TODO moving binder across fragments when a specific widget is in the child fragment and
        //TODO not in the parent activity cause binder not be able to see the specific widget.
        //TODO that is why binder.getRoot() returns null. use findViewById instead!!!
        //rvProperties = (RecyclerView)binder.getRoot().findViewById(R.id.properties_recycler_RV);

        View rootView = (FrameLayout)fragment.getLayoutInflater().inflate(
                R.layout.fragment_properties_recycler, null, false);
        rvProperties = (RecyclerView)rootView.findViewById(R.id.properties_recycler_RV);
        rvAdapter = new PropertiesRecyclerAdapter(context, rvProperties, properties, new BallabaUser());
        rvProperties.setLayoutManager(new LinearLayoutManager(context));
        rvProperties.setAdapter(rvAdapter);
    }
}
