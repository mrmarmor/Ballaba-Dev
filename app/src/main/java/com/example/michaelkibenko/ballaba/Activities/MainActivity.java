package com.example.michaelkibenko.ballaba.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;

import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SelectCityPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainActivity extends BaseActivity {
        //PropertiesRecyclerFragment.OnFragmentInteractionListener,
        //BallabaFragmentListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainLayoutBinding binder;
    public MainPresenter presenter;
    //private HashMap<String, String> filterResults = new HashMap<>();
    //private List<BallabaPropertyFull> properties = new ArrayList<>();
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
    /*@Override
    public void onFragmentInteraction(HashMap<String, String> results) {
        Object[] resultsKeys = results.keySet().toArray();
        for (Object res : resultsKeys)
            filterResults.put(res.toString(), results.get(res));
        //filterResults.put(res[1].toString(), results.get(res[1]));
        Log.d(TAG, filterResults.get(resultsKeys[0])+":"+resultsKeys[0]+":"+filterResults.size());
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO add result to board
        if (requestCode == MainPresenter.REQ_CODE_SELECT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> cities = data.getStringArrayListExtra(SelectCityPresenter.SELECTED_CITIES_KEY);
//                ArrayList<String> allPropertiesLocations =
////                        BallabaSearchPropertiesManager.getInstance(this).getResultsByLocation();
////                for (String city : cities) {
////                    if (allPropertiesLocations.contains(city))
////                        Log.d(TAG, city);
////
////                    LatLng cityLatLng = BallabaLocationManager.getInstance(this).locationGeoCoder(city);
////                    if (cityLatLng != null) {
////                        String cityLatLngStr = cityLatLng.latitude + "," + cityLatLng.longitude;
////                        Log.d(TAG, cityLatLngStr + "\n");
////                    }
////                }
                presenter.onSearchFlowComplete(cities);
            }
        }
    }

}
