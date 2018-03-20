package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaSelectedCityListener;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivitySelectCityBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 20/03/2018.
 */

public class SelectCityPresenter extends BasePresenter implements
        BallabaLocationManager.OnGoogleMapListener, BallabaSelectedCityListener {
    private final String TAG = SelectCityPresenter.class.getSimpleName(),
            PLACES_API_BASE = EndpointsHolder.GOOGLE_PLACES_API,
            TYPE_TEXT_SEARCH = "/textsearch", OUT_JSON = "/json?query=";

    private Context context;
    private ActivitySelectCityBinding binder;

    private AutoCompleteTextView actvSelectCity;

    private BallabaLocationManager.OnGoogleMapListener mListener;
    private GoogleMap googleMap;

    //public SelectCityPresenter(){}
    public SelectCityPresenter(Context context, ActivitySelectCityBinding binder){
        this.binder = binder;
        this.context = context;

        //initGoogleMapListener();
        initAutoCompleteTextView();
    }

    /*private void initGoogleMapListener(){
        if (context instanceof BallabaLocationManager.OnGoogleMapListener) {
            mListener = (BallabaLocationManager.OnGoogleMapListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGoogleMapListener");
        }
    }*/

    private void initAutoCompleteTextView(){
        //TODO if you want actvSearchPlace display device current address:
        //setListAdapterToDeviceAddress(listView);

        actvSelectCity = binder.selectCityAutoCompleteTextView;

        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(
                context, android.R.layout.simple_list_item_1);
        actvSelectCity.setAdapter(dataAdapter);

        actvSelectCity.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {actvSelectCity.setAdapter(dataAdapter); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        actvSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvSelectCity.setText(((TextView)view).getText());
                LatLng selectedPlace = BallabaLocationManager.getInstance(context)
                        .locationGeoCoder(((TextView)view).getText().toString());

                //BallabaMapFragment.newInstance().onItemSelected(googleMap, selectedPlace);

                ////TODO TESTING! These line should appear in Done button to close this activity/presenter and return back to MainActivity
                ((Activity)context).getIntent().putExtra("DUMMY!!!", selectedPlace);
                ((Activity)context).setResult(Activity.RESULT_OK, ((Activity)context).getIntent());
                ((Activity) context).finish();
                ////
            }
        });
    }

    @Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, googleMap.getMaxZoomLevel()+"");
        this.googleMap = googleMap;
    }

    @Override
    public void onItemSelected(GoogleMap googleMap, LatLng location) {
        this.googleMap = googleMap;
        //this.location = location;
    }
}
