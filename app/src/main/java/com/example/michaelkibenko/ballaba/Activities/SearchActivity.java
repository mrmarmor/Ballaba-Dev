package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Adapters.SearchViewPagerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
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

public class SearchActivity extends FragmentActivity implements
        PropertiesRecyclerFragment.OnFragmentInteractionListener,
        BallabaLocationManager.OnGoogleMapListener{

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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_property);
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0001", "רחוב סומסום", "100000000000", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0002", "אבן גבירול 17", "2000000", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0003", "הזית 42", "1300000", bitmap));
        /////END OF TESTING

        properties = PropertiesManager.getInstance(this).getProperties();
        presenter = new SearchPresenter(this, binder, getSupportFragmentManager());
        binder.setPresenter(presenter);
        //initPropertiesRecyclerFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.searchViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binder.searchViewPager.setCurrentItem(binder.searchViewPager.getCurrentItem() - 1);
        }
    }

    private void initPropertiesRecyclerFragment(){
        /*PropertiesRecyclerFragment autocompleteFragment = (PropertiesRecyclerFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_autoCompleteTV_fragment);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_autoCompleteTV_fragment, autocompleteFragment)
                .commit();*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "data from fragment: "+uri.getEncodedUserInfo());
    }

    @Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, "data from fragment: "+googleMap);
        presenter.OnGoogleMap(googleMap);
    }
}
