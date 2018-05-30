package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.EditViewportSubActivity;
import com.example.michaelkibenko.ballaba.Adapters.SavedAreasRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.Viewport;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivitySavedAreaBinding;

import java.util.ArrayList;

/**
 * Created by User on 14/05/2018.
 */

public class SavedAreaPresenter {
    private final String TAG = SavedAreaPresenter.class.getSimpleName();
    public static final int REQ_CODE_EDIT_VIEWPORT = 1;

    final private AppCompatActivity activity;
    private ActivitySavedAreaBinding binder;

    public SavedAreaPresenter(final AppCompatActivity activity, ActivitySavedAreaBinding binding, ArrayList<Viewport> viewports) {
        this.activity = activity;
        this.binder = binding;

        if (viewports == null || viewports.size() == 0){
            showPlaceHolder();
        } else {

        initRecyclerView(viewports);
        }

        binder.savedAreasButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditViewportSubActivity.class);
                activity.startActivityForResult(intent, REQ_CODE_EDIT_VIEWPORT);

                //openSaveViewPortDialog();


                /* ((SavedAreaActivity)activity).setActivityState(SavedAreaActivity.MAP_STATES.ON);

                Fragment mapFragment = BallabaMapFragment.newInstance();
                activity.getSupportFragmentManager()
                        .beginTransaction().replace(R.id.savedAreas_root, mapFragment)
                        .commit();*/


                //new MainPresenter().onClickToGoogleMap();

                //activity.setResult(RESULT_OK);
                //activity.finish();
            }
        });
    }

    public void showPlaceHolder(){//TODO remember to set visibility to gone when element is added to recyclerView
        binder.savedAreasPlaceHolder.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView(ArrayList<Viewport> viewports){
        SavedAreasRecyclerAdapter adapter = new SavedAreasRecyclerAdapter(activity, binder, viewports);
        binder.savedAreasRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binder.savedAreasRecyclerView.setAdapter(adapter);
    }

    private void openSaveViewPortDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog_Alert);
        View v = activity.getLayoutInflater().inflate(R.layout.fragment_map, null);
        builder.setView(v);

        builder.create().show();

    }

}
