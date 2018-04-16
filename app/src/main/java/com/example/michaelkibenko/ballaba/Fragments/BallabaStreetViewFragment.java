package com.example.michaelkibenko.ballaba.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class BallabaStreetViewFragment extends SupportStreetViewPanoramaFragment implements OnStreetViewPanoramaReadyCallback{
    public static final String LATITUDE_KEY = "latitude", LONGITUDE_KEY = "longitude"
            , TAG = BallabaStreetViewFragment.class.getSimpleName();

    private LatLng latLng;
    private StreetViewPanorama panorama;
    private BallabaResponseListener responseListener;

    public static BallabaStreetViewFragment newInstance(final LatLng latLng) {
        Bundle args = new Bundle();
        args.putDouble(LATITUDE_KEY, latLng.latitude);
        args.putDouble(LONGITUDE_KEY, latLng.longitude);
        BallabaStreetViewFragment svFragment = new BallabaStreetViewFragment();
        svFragment.setArguments(args);

        return svFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (getArguments() != null) {
            double latitude = getArguments().getDouble(LATITUDE_KEY);
            double longitude = getArguments().getDouble(LONGITUDE_KEY);
            latLng = new LatLng(latitude, longitude);
        }

        getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        if (latLng != null) {
            panorama = streetViewPanorama;
            panorama.setPosition(latLng);
            panorama.setStreetNamesEnabled(true);

            panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                @Override
                public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                    if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                        if (responseListener != null) {
                            responseListener.resolve(new BallabaOkResponse());
                        }
                    } else {
                        //TODO configure it
                        Toast.makeText(getActivity(), "No Street View Available For This Location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void setResponseListener(BallabaResponseListener responseListener){
        this.responseListener = responseListener;
    }

    public void setLocation(LatLng latLng){
        Log.d(TAG, latLng+"");
        panorama.setPosition(latLng);
        //StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera()
        //streetViewPanorama.animateTo();

    }

}
