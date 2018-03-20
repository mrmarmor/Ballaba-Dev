package com.example.michaelkibenko.ballaba.Common;

import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 15/03/2018.
 */

public interface BallabaSelectedCityListener {
    //this method help moving location of selected place from mapFragment to activity
    public void onItemSelected(GoogleMap googleMap, LatLng location);
}
