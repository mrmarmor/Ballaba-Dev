package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.michaelkibenko.ballaba.Adapters.SavedAreasRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.Viewport;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.ViewportsManager;
import com.example.michaelkibenko.ballaba.Presenters.SavedAreaPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivitySavedAreaBinding;

import java.util.ArrayList;

/**
 * Created by User on 14/05/2018.
 */

public class SavedAreaActivity extends BaseActivityWithActionBar {
    private ActivitySavedAreaBinding binder;
    private SavedAreaPresenter presenter;
    private ProgressDialog pd;
    private String selectedAreaId;

    /*private SavedAreaListener savedAreaListener;

    public SavedAreaActivity(){}
    public SavedAreaActivity(SavedAreaListener listener){
        savedAreaListener = listener;
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_saved_area);

        pd = new ProgressDialog(this);
        pd.setTitle("loading...");
        pd.show();

        getViewports();
    }

    private void getViewports(){
        ConnectionsManager.getInstance(this).getViewports(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ArrayList<Viewport> viewports = ViewportsManager.getInstance(
                        SavedAreaActivity.this).parseViewportsResults(
                        ((BallabaOkResponse)entity).body);

                //if you want to store viewports on a singleton.
                //ViewportsManager.getInstance(SavedAreaActivity.this).setViewports(viewports);

                pd.dismiss();

                presenter = new SavedAreaPresenter(SavedAreaActivity.this, binder, viewports);
                binder.setPresenter(presenter);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                presenter = new SavedAreaPresenter(SavedAreaActivity.this, binder, null);
                binder.setPresenter(presenter);
            }
        });
    }

    private @MAP_STATES int mapState;
    public @interface MAP_STATES {
        int ON = 1;
        int OFF = 2;
    }
    public void setActivityState(@MAP_STATES int state){
        this.mapState = state;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SavedAreaPresenter.REQ_CODE_EDIT_VIEWPORT){
            //TODO here we need to update list according to what user has been edited(title, viewport)
        }
    }

    //Here we add a back button at the right corner of the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void onClickSearch(View view*//*SavedAreaListener savedAreaListener*//*){
        finish();
        savedAreaListener.onClickSavedArea(selectedAreaId);
    }

    public interface SavedAreaListener {
        void onClickSavedArea(String selectedAreaId);
    }*/

    @Override
    public void onBackPressed() {
        if (mapState == MAP_STATES.ON) {
            Fragment mapFragment = BallabaMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction().remove(mapFragment)
                    .commit();
            setActivityState(MAP_STATES.OFF);

        } else {
            super.onBackPressed();
        }
    }
}
