package com.example.michaelkibenko.ballaba.Fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class BallabaMapFragment extends Fragment implements OnMapReadyCallback, LocationListener , GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener{

    private static final String TAG = BallabaMapFragment.class.getSimpleName();

    private GoogleMap googleMap;
    private MapView mMapView;
    private BallabaLocationManager locationManager;
    private Context context;
    private boolean changed;
    private LatLngBounds bounds;

    public BallabaMapFragment(){}

    public static BallabaMapFragment newInstance() {
        Bundle args = new Bundle();

        BallabaMapFragment fragment = new BallabaMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView)v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        //TODO optional in the future
        //googleMap = mMapView.getMap();
        /*double latitude = 17.385044;
        double longitude = 78.486671;

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("title");

        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        locationManager = BallabaLocationManager.getInstance(this.context);
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        locationManager.getLocation(this);
        this.googleMap.setOnCameraMoveStartedListener(this);
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setOnCameraMoveCanceledListener(this);
        this.googleMap.setOnCameraIdleListener(this);
    }

    //location
    @Override
    public void onLocationChanged(Location location) {
        if(!changed) {
            changed = true;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this.context, "GPS disabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String s) {

    }
    //locationEnd

    //map camera
    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraIdle() {
        bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        //TODO here will be the get properties request
    }
    //map camera end

}
