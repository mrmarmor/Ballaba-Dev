package com.example.michaelkibenko.ballaba.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Fragments.Filter.AttachmentsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.DateOfEntranceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.PriceFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.RoomsFragment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.SizeFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SelectCityPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainActivity extends BaseActivity implements
        //PropertiesRecyclerFragment.OnFragmentInteractionListener,
        BallabaFragmentListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainLayoutBinding binder;
    public MainPresenter presenter;
    private HashMap<String, String> filterResults = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main_layout);

        //properties = PropertiesManager.getInstance(this).getProperties();
        presenter = new MainPresenter(this, binder, getSupportFragmentManager());
        binder.setPresenter(presenter);
        //initPropertiesRecyclerFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.mainActivityPropertiesViewPager.getCurrentItem() == 0) {
            BallabaDialogBuilder areUSureDialog = new BallabaDialogBuilder(this);

            areUSureDialog.setButtons("Exit", "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            areUSureDialog.setIcon(R.drawable.check_blue_24);
            areUSureDialog.setContent("Exit", "Are You sure you want to exit?", null).show();

        } else {
            presenter.onClickToGoogleMap();
        }
    }

//    public void onClickFilterDone(View view){
//        for (String results:filterResults.values())
//            Log.d(TAG, results+"\n");
//        Toast.makeText(this, "filters number: "+filterResults.size(), Toast.LENGTH_LONG).show();
//    }
//    public void onClickFilterCanceled(View view){
//        Toast.makeText(this, "canceled", Toast.LENGTH_LONG).show();
//    }

    //Here is all data from fragments: properties and filters
    @Override
    public void onFragmentInteraction(HashMap<String, String> results) {
        Object[] resultsKeys = results.keySet().toArray();
        for (Object res : resultsKeys)
            filterResults.put(res.toString(), results.get(res));
        //filterResults.put(res[1].toString(), results.get(res[1]));
        Log.d(TAG, filterResults.get(resultsKeys[0])+":"+resultsKeys[0]+":"+filterResults.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO add result to board
        if (requestCode == MainPresenter.REQ_CODE_SELECT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> cities = data.getStringArrayListExtra(SelectCityPresenter.SELECTED_CITIES_KEY);
                ArrayList<String> allPropertiesLocations =
                        BallabaSearchPropertiesManager.getInstance(this).getResultsByLocation();
                for (String city : cities) {
                    if (allPropertiesLocations.contains(city))
                        Log.d(TAG, city);

                    LatLng cityLatLng = BallabaLocationManager.getInstance(this).locationGeoCoder(city);
                    if (cityLatLng != null) {
                        String cityLatLngStr = cityLatLng.latitude + "," + cityLatLng.longitude;
                        Log.d(TAG, cityLatLngStr + "\n");
                    }
                }

                //TODO Moshe check it
//                presenter.filterStateUIChanger(MainPresenter.FilterState.NO_FILTER);

                /*BallabaSearchPropertiesManager.getInstance(this).getPropertiesByLatLng(cityLatLngStr
                        , new BallabaResponseListener() {
                            @Override
                            public void resolve(BallabaBaseEntity entity) {
                                Log.d(TAG, "entity: "+entity);
                            }

                            @Override
                            public void reject(BallabaBaseEntity entity) {
                                Log.e(TAG, "entity: "+entity);
                            }
                        }, true);

                Toast.makeText(this, data.getStringExtra(SelectCityPresenter.SELECTED_CITY_KEY), Toast.LENGTH_LONG).show();
                */
            }
        }
    }

}
