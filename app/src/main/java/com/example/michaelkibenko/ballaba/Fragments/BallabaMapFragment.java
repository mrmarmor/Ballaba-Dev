package com.example.michaelkibenko.ballaba.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.EditViewportSubActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.SavedAreaActivity;
import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoardActivity;
import com.example.michaelkibenko.ballaba.Adapters.MapPropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SavedAreaPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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
import static com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment.PROPERTY_RECYCLER_VIEW_STATES.HIDE;
import static com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment.PROPERTY_RECYCLER_VIEW_STATES.MORE_ITEMS;
import static com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment.PROPERTY_RECYCLER_VIEW_STATES.ONE_ITEM;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class BallabaMapFragment extends DialogFragment implements OnMapReadyCallback, LocationListener , GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener ,GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener{

    private LayoutInflater inflater;
    private ViewGroup container;
    private int mapZoom;

    @IntDef({ON, OFF})
    @interface MAP_SAVE_CONTAINER_STATES {
        int ON = 1;
        int OFF = 2;
    }

    @IntDef({ONE_ITEM, MORE_ITEMS, HIDE})
    @interface PROPERTY_RECYCLER_VIEW_STATES {
        int ONE_ITEM = 3;
        int MORE_ITEMS = 4;
        int HIDE = 5;
    }

    public static final String TAG = BallabaMapFragment.class.getSimpleName()
            , PROPERTY_MAP_EXTRA = "property map extra";

    private static GoogleMap googleMap;
    private MapView mMapView;
    private BallabaLocationManager locationManager;
    private boolean changed, disableUpdating;
    private LatLngBounds bounds;
    private BallabaLocationManager.OnGoogleMapListener mListener;
    private ConstraintLayout saveContainer;
    private Button saveSearchButton;
    private View saveSearchContainerAnchor;
    private ConstraintLayout rootView, searchBarTransition, notChangeableRootView, propertiesRecyclerViewTransition;
    private float markerSize;
    private Bitmap guaranteeMarkerIcon, exclusivityMarkerIcon;
    private HashMap<String,ArrayList<BallabaPropertyResult>> insideResHash;
    private RecyclerView propertiesRV;
    private MapPropertiesRecyclerAdapter propetiesReciclerAdapter;
    private boolean isShowProperty;
    private float oneItemHeight, moreItemsHeight;
    private FrameLayout pnlFlash;
    private LatLng location;
    private Context context;
    private boolean isFromPropertyDescription;
    private BallabaResponseListener responseListener;
    private AlertDialog saveViewPort;

    private @MAP_SAVE_CONTAINER_STATES int saveContainerState = MAP_SAVE_CONTAINER_STATES.OFF;
    private @PROPERTY_RECYCLER_VIEW_STATES int propertyRecyclerViewState = PROPERTY_RECYCLER_VIEW_STATES.ONE_ITEM;

    public BallabaMapFragment(){}

    public static BallabaMapFragment newInstance() {
        Bundle args = new Bundle();

        BallabaMapFragment fragment = new BallabaMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        rootView = (ConstraintLayout) v;
        notChangeableRootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_map, container, false);
        if(context instanceof MainActivity) {
            searchBarTransition = (ConstraintLayout) inflater.inflate(R.layout.map_fragment_for_transitions, container, false);
        }else if(context instanceof StreetAndMapBoardActivity || context instanceof EditViewportSubActivity){
            searchBarTransition = (ConstraintLayout) inflater.inflate(R.layout.map_fragment_for_transition_no_search, container, false);
        }
        propertiesRecyclerViewTransition = (ConstraintLayout) inflater.inflate(R.layout.map_fragment_items_recycler_view_transition, container, false);
        mMapView = (MapView)v.findViewById(R.id.mapView);
        saveContainer = (ConstraintLayout) v.findViewById(R.id.saveMapSearchContainer);
        saveSearchButton = (Button) v.findViewById(R.id.saveMapSearch_save_BTN);
        saveSearchContainerAnchor = (View) v.findViewById(R.id.saveMapSearchContainerBottom_anchor);
        propertiesRV = (RecyclerView) v.findViewById(R.id.mapFragment_properties_RV);
        propetiesReciclerAdapter = new MapPropertiesRecyclerAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        propertiesRV.setLayoutManager(linearLayoutManager);
        propertiesRV.setAdapter(propetiesReciclerAdapter);
        pnlFlash = (FrameLayout) v.findViewById(R.id.pnlFlash);

        saveSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaveViewPortDialog();
            }
        });

        v.findViewById(R.id.map_fragment_getPropertiesBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPropertiesByViewPort();
            }
        });

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        Resources res = context.getResources();
        markerSize = res.getDimension(R.dimen.mapMarkerSize);

        oneItemHeight = res.getDimension(R.dimen.map_fragment_properties_recycler_view_one_item_height);
        moreItemsHeight = getResources().getDimension(R.dimen.map_fragment_properties_recycler_view_more_items_height);

        BitmapDrawable bitmapdrawGU=(BitmapDrawable)res.getDrawable(R.drawable.guarenty);
        Bitmap gu = bitmapdrawGU.getBitmap();
        guaranteeMarkerIcon = Bitmap.createScaledBitmap(gu, (int)markerSize, (int)markerSize, false);

        BitmapDrawable bitmapdrawEX=(BitmapDrawable)res.getDrawable(R.drawable.exclusive);
        Bitmap ex = bitmapdrawEX.getBitmap();
        exclusivityMarkerIcon = Bitmap.createScaledBitmap(ex, (int)markerSize, (int)markerSize, false);

        return v;
    }

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        if(mContext instanceof PropertyDescriptionActivity){
            isFromPropertyDescription = true;
        }
        this.context = mContext;
        locationManager = BallabaLocationManager.getInstance(this.context);
        insideResHash = new HashMap<>();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        //we need context instance of to do not change the location on property description
        if(context instanceof MainActivity && !disableUpdating) {
//            onLocationChanged(locationManager.getLastKnownLocation());
            locationManager.getLocation(this);
        }

        if(!disableUpdating) {
            LatLng latLng = new LatLng(31.425155, 35.1929755);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(7));
        }

        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }else{
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }


        if (mListener != null) {
            mListener.OnGoogleMap(googleMap);
        }

        if(disableUpdating){
            onLocationChanged(null);
        }

        initInsideResultsHash(BallabaSearchPropertiesManager.getInstance(context).getResults());
        updatePropertiesMarkers(insideResHash);

        if(responseListener != null){
            responseListener.resolve(new BallabaOkResponse());
        }

    }

    public void setResponseListener(BallabaResponseListener responseListener){
        this.responseListener = responseListener;
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

    //location
    @Override
    public void onLocationChanged(Location location) {
        if(!changed  && location != null) {
            changed = true;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            getPropertiesByViewPort();
        }else if(!changed){
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(this.location));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(mapZoom));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isShowProperty) {
            hideSelectedAddress();
        }
        if(isFromPropertyDescription){
            //((PropertyDescriptionActivity)context).presenter.setMapFullScreen();
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
        if (context instanceof MainActivity) {
            if (((MainActivity) context).presenter.filterState != MainPresenter.FilterState.NO_FILTER) {
                ((MainActivity) context).presenter.filterStateUIChanger(MainPresenter.FilterState.NO_FILTER);
            }
        }
        changeMapSaveState(MAP_SAVE_CONTAINER_STATES.OFF);
        changePropertyRVState(HIDE);
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraIdle() {
        changeMapSaveState(MAP_SAVE_CONTAINER_STATES.ON);
        //TODO here will be the get properties request
    }

    //TODO if you want map to focus on autocompletetextview input place do next method
    /*@Override
    public void onItemSelected(GoogleMap googleMap, LatLng location) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }*/
    //map camera end

    //marker onclick
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(saveContainerState == MAP_SAVE_CONTAINER_STATES.ON){
            changeMapSaveState(MAP_SAVE_CONTAINER_STATES.OFF);
        }
        showSelectedAddress((String)marker.getTag());
        return true;
    }
    //marker end

    private void changeMapSaveState(@MAP_SAVE_CONTAINER_STATES int newState){
        if(this.saveContainerState != newState) {
            this.saveContainerState = newState;
            onMapStateChanged();
        }
    }

    private void onMapStateChanged(){
        ConstraintSet set = new ConstraintSet();
        if(this.saveContainerState == MAP_SAVE_CONTAINER_STATES.ON){
            set.clone(searchBarTransition);
            saveSearchContainerAnchor.setBackgroundResource(R.color.colorPrimary);
        }
        if(this.saveContainerState == MAP_SAVE_CONTAINER_STATES.OFF){
            set.clone(notChangeableRootView);
            saveSearchContainerAnchor.setBackgroundResource(android.R.color.transparent);
        }
        TransitionManager.beginDelayedTransition(rootView);
        set.applyTo(rootView);
    }

    private void changePropertyRVState(@PROPERTY_RECYCLER_VIEW_STATES int newState){
        if(propertyRecyclerViewState != newState){
            propertyRecyclerViewState = newState;
        }
        onPropertyRecyclerViewStateChanged();
    }

    private void onPropertyRecyclerViewStateChanged(){
        ConstraintSet set = new ConstraintSet();
        set.clone(propertiesRecyclerViewTransition);
        if(this.propertyRecyclerViewState == PROPERTY_RECYCLER_VIEW_STATES.ONE_ITEM){
            set.constrainHeight(propertiesRV.getId(), (int)oneItemHeight);
        }else if(this.propertyRecyclerViewState == PROPERTY_RECYCLER_VIEW_STATES.MORE_ITEMS){
            set.constrainHeight(propertiesRV.getId(), (int)moreItemsHeight);
        }else if(this.propertyRecyclerViewState == HIDE){
            set.clone(notChangeableRootView);
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
            startDropMarkerAnimation(marker);
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

    private void showSelectedAddress(String address){
        if(insideResHash.get(address).size()==1){
            changePropertyRVState(ONE_ITEM);
        }else if(insideResHash.get(address).size()>1){
            changePropertyRVState(MORE_ITEMS);
        }
        isShowProperty = true;
        propetiesReciclerAdapter.updateItems(insideResHash.get(address));
        propetiesReciclerAdapter.notifyDataSetChanged();
    }

    private void hideSelectedAddress(){
        if(isShowProperty){
            changePropertyRVState(HIDE);
            propetiesReciclerAdapter.updateItems(new ArrayList<BallabaPropertyResult>());
            propetiesReciclerAdapter.notifyDataSetChanged();
            isShowProperty = false;
        }
    }

    private void getPropertiesByViewPort(){
        bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        BallabaSearchPropertiesManager.getInstance(context).getPropertiesByViewPort(bounds, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                initInsideResultsHash(BallabaSearchPropertiesManager.getInstance(context).getResults());
                updatePropertiesMarkers(insideResHash);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                if(((BallabaErrorResponse)entity).statusCode == 404){
                    changeMapSaveState(OFF);
                    ((BaseActivity)context).getDefaultSnackBar(rootView, context.getResources().getString(R.string.no_results), false)
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onShown(Snackbar sb) {
                                    super.onShown(sb);
                                }

                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    changeMapSaveState(ON);
                                    super.onDismissed(transientBottomBar, event);
                                }
                            })
                            .show();
//                    Toast.makeText(context, "No results", Toast.LENGTH_LONG).show();
                }else{
                    changeMapSaveState(OFF);
                    ((BaseActivity)context).getDefaultSnackBar(rootView, context.getResources().getString(R.string.error_network_internal), false)
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onShown(Snackbar sb) {
                                    super.onShown(sb);
                                }

                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    changeMapSaveState(ON);
                                    super.onDismissed(transientBottomBar, event);
                                }
                            })
                            .show();
//                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startDropMarkerAnimation(final Marker marker) {
        final LatLng target = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = this.googleMap.getProjection();
        Point targetPoint = proj.toScreenLocation(target);
        final long duration = (long) (200 + (targetPoint.y * 0.6));
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.y = 0;
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearOutSlowInInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later == 60 frames per second
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void snapShotAnimation(){
        pnlFlash.setVisibility(View.VISIBLE);

        final AlphaAnimation fadeIn = new AlphaAnimation(.1f, .9f);
        final AlphaAnimation fadeOut = new AlphaAnimation(.9f, .1f);
        fadeIn.setDuration(200);
        fadeOut.setDuration(200);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new MediaActionSound().play(MediaActionSound.SHUTTER_CLICK);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                pnlFlash.startAnimation(fadeOut);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation anim) {
                pnlFlash.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });

        pnlFlash.startAnimation(fadeIn);
    }

    public void setLocation(LatLng latLng, final int mapZoom){
        //locationManager.getLocation(null);
        //this.mListener = null;
        disableUpdating = true;
        this.location = latLng;
        this.mapZoom = mapZoom;
//        if(googleMap != null){
//            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
//            CameraUpdate zoom = CameraUpdateFactory.zoomTo(mapZoom);
//            googleMap.moveCamera(center);
//            googleMap.animateCamera(zoom);
//
//
//            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        } else {
//            Log.e(TAG, "google map is null");
//        }
    }

    private void openSaveViewPortDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_AppCompat_Dialog_Alert);
        View v = inflater.inflate(R.layout.save_view_port_dialog_layout, container, false);
        builder.setView(v);
        final EditText nameEditText = v.findViewById(R.id.save_view_port_dialog_save_name_edit_text);
        Button saveButton = v.findViewById(R.id.save_view_port_dialog_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapShotAnimation();
                saveViewPort.dismiss();
                String name = nameEditText.getText().toString();
                if(name.isEmpty()){
                    name = "No Name";
                }
                ConnectionsManager.getInstance(context).saveViewPort(name
                        , googleMap.getProjection().getVisibleRegion().latLngBounds
                        , googleMap.getCameraPosition().zoom
                        , new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                        changeMapSaveState(MAP_SAVE_CONTAINER_STATES.OFF);
                        ((BaseActivity)context).getDefaultSnackBar(getView(), "אזור זה נשמר בהצלחה", false)
                                .addCallback(new Snackbar.Callback(){
                            @Override
                            public void onShown(Snackbar sb) {
                                super.onShown(sb);
                            }

                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                changeMapSaveState(MAP_SAVE_CONTAINER_STATES.ON);
                            }
                        }).show();
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        ((BaseActivity)context).getDefaultSnackBar(getView(), "השמירה נכשלה נסה שנית מאוחר יותר", false).show();
                    }
                });
            }
        });
        saveViewPort = builder.create();
        
        saveViewPort.show();

    }

}
