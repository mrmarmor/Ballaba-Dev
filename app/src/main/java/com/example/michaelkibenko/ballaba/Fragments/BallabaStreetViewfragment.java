package com.example.michaelkibenko.ballaba.Fragments;

import android.os.Bundle;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class BallabaStreetViewfragment extends SupportStreetViewPanoramaFragment implements OnStreetViewPanoramaReadyCallback{
    private static final String LATITUDE_KEY = "latitude", LONGITUDE_KEY = "longitude"
            , TAG = BallabaStreetViewfragment.class.getSimpleName();

    private LatLng latLng;
    private StreetViewPanorama streetViewPanorama;
    private BallabaResponseListener responseListener;

    public static BallabaStreetViewfragment newInstance(LatLng latLng) {
        Bundle args = new Bundle();
        args.putDouble(LATITUDE_KEY, latLng.latitude);
        args.putDouble(LONGITUDE_KEY, latLng.longitude);
        BallabaStreetViewfragment fragment = new BallabaStreetViewfragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        double latitude = getArguments().getDouble(LATITUDE_KEY);
        double longitude = getArguments().getDouble(LONGITUDE_KEY);
        latLng = new LatLng(latitude, longitude);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        this.streetViewPanorama = streetViewPanorama;
        this.streetViewPanorama.setPosition(latLng);
        this.streetViewPanorama.setStreetNamesEnabled(true);

        this.streetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                    if(responseListener != null){
                        responseListener.resolve(new BallabaOkResponse());
                    }
                } else {
                    //TODO configure it
                }
            }
        });
    }

    public void setResponseListener(BallabaResponseListener responseListener){
        this.responseListener = responseListener;
    }

    public void setLocation(LatLng latLng){
        Log.d(TAG, latLng+"");
        streetViewPanorama.setPosition(latLng);
        //StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera()
        //streetViewPanorama.animateTo();

    }
}
