package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.MainScreenLayoutBinding;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainScreenActivity extends FragmentActivity implements
        PropertiesRecyclerFragment.OnFragmentInteractionListener{

    private final String TAG = MainScreenActivity.class.getSimpleName();

    private MainScreenLayoutBinding binder;
    private MainPresenter presenter;
    private List<BallabaProperty> properties = new ArrayList<>();//Collections.emptyList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.main_screen_layout);

        /////TESTING
        //properties.add(new BallabaProperty("0001", "רחוב סומסום", "100000000000$"));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_property);
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0001", "רחוב סומסום", "10000", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0002", "אבן גבירול 17", "2000", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0003", "הזית 42", "1300", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0004", "רחוב הלם", "12000", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0005", "אבן גבירול 147", "2300", bitmap));
        PropertiesManager.getInstance(this).addProperty(new BallabaProperty("0006", "הזית 4", "4300", bitmap));
        /////END OF TESTING

        properties = PropertiesManager.getInstance(this).getProperties();
        presenter = new MainPresenter(this, binder, getSupportFragmentManager());
        binder.setPresenter(presenter);
        //initPropertiesRecyclerFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.mainScreenViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binder.mainScreenViewPager.setCurrentItem(binder.mainScreenViewPager.getCurrentItem() - 1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainPresenter.REQ_CODE_SELECT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                Toast.makeText(this, data.getStringExtra("DUMMY!!!"), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*@Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, "data from fragment: "+googleMap);
        presenter.OnGoogleMap(googleMap);
    }*/
}
