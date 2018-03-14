package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Fragments.SearchPlaceFragment;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.Presenters.SearchPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class SearchActivity extends FragmentActivity implements PropertiesRecyclerFragment.OnFragmentInteractionListener{
    private final String TAG = SearchActivity.class.getSimpleName();

    private SearchActivityLayoutBinding binder;
    private SearchPresenter presenter;
    private List<BallabaProperty> properties = new ArrayList<>();//Collections.emptyList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.search_activity_layout);

        /////TESTING
        //properties.add(new BallabaProperty("0001", "רחוב סומסום", "100000000000$"));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0001", "רחוב סומסום", "100000000000$"));
        /////END OF TESTING

        properties = PropertiesManager.getInstance(this).getProperties();
        presenter = new SearchPresenter(this, binder, getSupportFragmentManager());
        binder.setPresenter(presenter);
        //initSearchFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.searchViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binder.searchViewPager.setCurrentItem(binder.searchViewPager.getCurrentItem() - 1);
        }
    }

    private void initSearchFragment(){
        // Retrieve the PlaceAutocompleteFragment.
        SearchPlaceFragment autocompleteFragment = (SearchPlaceFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_autoCompleteTV_fragment);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_autoCompleteTV_fragment, autocompleteFragment)
                .commit();
// Register a listener to receive callbacks when a place has been selected or an error has
// occurred.
        /*autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, "Place: " + place.getName());
                Log.d(TAG, "Place Selected: " + place.getName() + "  " + place.getLatLng());

                double workLatitude = place.getLatLng().latitude;
                double workLongitude = place.getLatLng().longitude;

                //Over we can get the address, rating, price level,etc.

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "data from fragment: "+uri.getEncodedUserInfo());
    }
}
