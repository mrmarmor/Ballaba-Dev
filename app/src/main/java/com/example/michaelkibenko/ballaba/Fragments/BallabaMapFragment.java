package com.example.michaelkibenko.ballaba.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment.MAP_SAVE_CONTAINER_STATES.OFF;
import static com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment.MAP_SAVE_CONTAINER_STATES.ON;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class BallabaMapFragment extends Fragment implements OnMapReadyCallback, LocationListener , GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener ,GoogleMap.OnMarkerClickListener{

    @IntDef({ON, OFF})
    @interface MAP_SAVE_CONTAINER_STATES {
        int ON = 1;
        int OFF = 2;
    }


    private static final String TAG = BallabaMapFragment.class.getSimpleName();

    private GoogleMap googleMap;
    private MapView mMapView;
    private BallabaLocationManager locationManager;
    private Context context;
    private boolean changed;
    private LatLngBounds bounds;
    private BallabaLocationManager.OnGoogleMapListener mListener;
    private ConstraintLayout saveContainer;
    private Button saveSearchButton;
    private View saveSearchContainerAnchor;
    private ConstraintLayout rootView, transition, notChangeableRootView;
    private float markerSize;
    private Bitmap guaranteeMarkerIcon, exclusivityMarkerIcon;
    private HashMap<String,ArrayList<BallabaPropertyResult>> insideResHash;

    private @MAP_SAVE_CONTAINER_STATES int saveContainerState = MAP_SAVE_CONTAINER_STATES.OFF;

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
        rootView = (ConstraintLayout) v;
        notChangeableRootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_map, container, false);
        transition = (ConstraintLayout) inflater.inflate(R.layout.map_fragment_for_transitions, container, false);
        mMapView = (MapView)v.findViewById(R.id.mapView);
        saveContainer = (ConstraintLayout) v.findViewById(R.id.saveMapSearchContainer);
        saveSearchButton = (Button) v.findViewById(R.id.saveMapSearch_save_BTN);
        saveSearchContainerAnchor = (View) v.findViewById(R.id.saveMapSearchContainerBottom_anchor);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        markerSize = getResources().getDimension(R.dimen.mapMarkerSize);

        BitmapDrawable bitmapdrawGU=(BitmapDrawable)getResources().getDrawable(R.drawable.guarenty);
        Bitmap gu=bitmapdrawGU.getBitmap();
        guaranteeMarkerIcon = Bitmap.createScaledBitmap(gu, (int)markerSize, (int)markerSize, false);

        BitmapDrawable bitmapdrawEX=(BitmapDrawable)getResources().getDrawable(R.drawable.exclusive);
        Bitmap ex=bitmapdrawEX.getBitmap();
        exclusivityMarkerIcon = Bitmap.createScaledBitmap(ex, (int)markerSize, (int)markerSize, false);


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
        insideResHash = new HashMap<>();

        //TODO context == MainActivity. next 6 lines throws exception because it is not instance of
        //TODO BallabaLocationManager.OnGoogleMapListener. needed to be fixed.
        /*if (context instanceof BallabaLocationManager.OnGoogleMapListener) {
            mListener = (BallabaLocationManager.OnGoogleMapListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGoogleMapListener");
        }*/
        //TODO end of TODO

        /*DataBindingUtil.inflate(
                inflater, R.layout.fragment_properties_recycler, null, false);
        presenter = new SearchPropertiesPresenter(getActivity(), binder, mParam);
*/

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
        this.googleMap.setOnMarkerClickListener(this);

        if (mListener != null) {
            mListener.OnGoogleMap(googleMap);
        }

        initInsideResultsHash(BallabaSearchPropertiesManager.getInstance(context).getResults());
        updatePropertiesMarkers(insideResHash);
    }

    public void initInsideResultsHash(ArrayList<BallabaPropertyResult> arr){
        insideResHash.clear();
        for (BallabaPropertyResult target : arr) {
            if(insideResHash.containsKey(target.formattedAddress)){
                insideResHash.get(target.formattedAddress).add(target);
            }else{
                ArrayList<BallabaPropertyResult> potable = new ArrayList<BallabaPropertyResult>();
                potable.add(target);
                insideResHash.put(target.formattedAddress, potable);
            }
        }
    }

    public GoogleMap getGoogleMapObject(){
        return this.googleMap;
    }

    //location
    @Override
    public void onLocationChanged(Location location) {
        if(!changed) {
            changed = true;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
        changeMapSaveState(MAP_SAVE_CONTAINER_STATES.OFF);
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
        changeMapSaveState(MAP_SAVE_CONTAINER_STATES.ON);
        //TODO here will be the get properties request
    }

    //TODO if you want map to focus on autocompletetextview input place do next method
    /*@Override
    public void onItemSelected(GoogleMap googleMap, LatLng location) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }*/
    //map camera end

    //marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(context, (String)marker.getTag(), Toast.LENGTH_LONG).show();
        return true;
    }
    //marker

    private void changeMapSaveState(@MAP_SAVE_CONTAINER_STATES int newState){
        if(this.saveContainerState != newState) {
            this.saveContainerState = newState;
            onMapStateChanged();
        }
    }

    private void onMapStateChanged(){
        ConstraintSet set = new ConstraintSet();
        if(this.saveContainerState == MAP_SAVE_CONTAINER_STATES.ON){
            set.clone(transition);
            saveSearchContainerAnchor.setBackgroundResource(R.color.colorPrimary);
        }
        if(this.saveContainerState == MAP_SAVE_CONTAINER_STATES.OFF){
            set.clone(notChangeableRootView);
            saveSearchContainerAnchor.setBackgroundResource(android.R.color.transparent);
        }
        TransitionManager.beginDelayedTransition(rootView);
        set.applyTo(rootView);
    }

    private void updatePropertiesMarkers(HashMap<String,ArrayList<BallabaPropertyResult>> insideResHash){
        clearGoogleMap();
        Set<String> resultsKeySet = insideResHash.keySet();
        for (String address : resultsKeySet) {
            ArrayList<BallabaPropertyResult> arrayList = insideResHash.get(address);
            BallabaPropertyResult prop = arrayList.get(0);
            Marker marker = googleMap.addMarker(getMarker(prop.isGuarantee, prop.latLng, arrayList.size()));
            marker.setTag(address);
        }
    }

    private MarkerOptions getMarker(boolean isGuarantee, LatLng position, int resultsNum){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(isGuarantee?guaranteeMarkerIcon:exclusivityMarkerIcon));
        return markerOptions;
    }

    private void clearGoogleMap(){
        if(googleMap != null){
            googleMap.clear();
        }
    }
}
