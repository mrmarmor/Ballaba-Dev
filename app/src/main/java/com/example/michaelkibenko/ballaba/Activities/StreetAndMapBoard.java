package com.example.michaelkibenko.ballaba.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.BallabaStreetViewFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;

public class StreetAndMapBoard extends BaseActivity {
    private final String TAG = StreetAndMapBoard.class.getSimpleName();

    private BallabaMapFragment mapFragment;
    private LatLng propertyLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_and_map_board);

        propertyLatLng = convertStringToLatlng(getIntent().getStringExtra(PropertyDescriptionPresenter.PROPERTY_LATLNG_EXTRA));

        if (propertyLatLng != null){
            fragmentsSwitch();
        } else {
            Toast.makeText(this, "Error: Property location is undefined.", Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.streetAndMapBoard_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fragmentsSwitch(){
        String fragment = getIntent().getStringExtra(PropertyDescriptionPresenter.FRAGMENT_NAME);
        if (fragment.equals(BallabaStreetViewFragment.TAG))
            initStreetView();
        else if (fragment.equals(BallabaMapFragment.TAG))
            initGoogleMap();
    }

    private void initStreetView(){
        final BallabaStreetViewFragment svFragment = BallabaStreetViewFragment.newInstance(propertyLatLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.streetAndMapBoard, svFragment);
        transaction.commit();

        svFragment.setResponseListener(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if (propertyLatLng != null)
                    svFragment.setLocation(propertyLatLng);
            }
            @Override
            public void reject(BallabaBaseEntity entity) {}
        });
    }

    public void initGoogleMap(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mapFragment = BallabaMapFragment.newInstance();
        transaction.replace(R.id.streetAndMapBoard, mapFragment);
        transaction.commit();

        mapFragment.setResponseListener(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if (propertyLatLng != null)
                    mapFragment.setLocation(propertyLatLng);
            }
            @Override
            public void reject(BallabaBaseEntity entity) {}
        });

    }

    private LatLng convertStringToLatlng(String string){
        if (string != null && string.contains(",")) {
            String[] strings = string.split(",");
            return new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
        }
        return null;
    }

}
