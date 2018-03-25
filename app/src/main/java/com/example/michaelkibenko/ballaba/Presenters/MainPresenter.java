package com.example.michaelkibenko.ballaba.Presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.SearchViewPagerAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaSelectedCityListener;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainPresenter extends BasePresenter {
    private final String TAG = MainPresenter.class.getSimpleName();
    //private final String PLACES_API_BASE = EndpointsHolder.GOOGLE_PLACES_API
    //        , TYPE_TEXT_SEARCH = "/textsearch", OUT_JSON = "/json?query=";
    public static final int REQ_CODE_GPS_PERMISSION = 1, REQ_CODE_SELECT_CITY = 2;

    public @interface SearchState { int FILTERED = 1; int NOT_FILTERED = 2; }

    private Context context;
    private FragmentManager fm;
    private PagerAdapter pagerAdapter;
    private ActivityMainLayoutBinding binder;
    private BallabaSelectedCityListener listener;
    //private GoogleMap googleMap;

    public MainPresenter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm){
        this.binder = binder;
        this.context = context;
        this.fm = fm;

        initDrawer();
        initViewPager();
    }

    private void initDrawer(){
        binder.mainActivityNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        binder.mainActivityDrawerLayout.closeDrawers();

                        //TODO here i need to add switch between menu items

                        return true;
                    }
                });

    }

    private void initViewPager(){
        pagerAdapter = new SearchViewPagerAdapter(context, binder, fm);
        binder.mainActivityViewPager.setAdapter(pagerAdapter);
    }

    public void onClickToSelectCity(){
        Intent intentSelectCity = new Intent(context, SelectCitySubActivity.class);
        ((Activity)context).startActivityForResult(intentSelectCity, REQ_CODE_SELECT_CITY);
    }

    public void onClickToGoogleMap(){
        binder.mainActivityViewPager.setCurrentItem(
                binder.mainActivityViewPager.getCurrentItem() == 0? 1:0, false);
    }

    public void onClickDrawer(){
        binder.mainActivityDrawerLayout.openDrawer(Gravity.START);
    }

    public void onClickSortByRelevant(){
        Toast.makeText(context, "relevant clicked", Toast.LENGTH_SHORT).show();
    }
    public void onClickSortByPrice(){
        Toast.makeText(context, "price clicked", Toast.LENGTH_SHORT).show();
    }
    public void onClickSortBySize(){
        Toast.makeText(context, "size clicked", Toast.LENGTH_SHORT).show();
    }
    public void onClickSortByRooms(){
        Toast.makeText(context, "rooms clicked", Toast.LENGTH_SHORT).show();
    }

    //TODO it could be united with next method
    //TODO also, i can delete SearchState because there is only 1 state change - from not filtered to filtered
    public void SearchBarStateUIChanger(String city, @SearchState int state){
        onSearchBarUIChanged(city, state);
    }

    private void onSearchBarUIChanged(String city, int state){
        if (state == SearchState.FILTERED){
            binder.mainActivitySearchBar.setBackgroundColor(context.getResources()
                .getColor(R.color.colorPrimary, context.getTheme()));
            binder.mainActivitySortButtonsLinearLayout.setVisibility(View.VISIBLE);
            binder.mainActivitySearchButton.setText(city);
            binder.propertiesRecyclerFloatingButton.setVisibility(View.VISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binder.mainActivityRootLayout);
            constraintSet.connect(R.id.mainActivity_viewPager, ConstraintSet.TOP, R.id.mainActivity_sortButtons_linearLayout, ConstraintSet.BOTTOM,0);
            //constraintSet.connect(R.id.mainActivity_horizontalScrollView, ConstraintSet.RIGHT, R.id.mainActivity_drawerLayout, ConstraintSet.RIGHT,0);
            constraintSet.applyTo(binder.mainActivityRootLayout);

        } else {
            binder.mainActivitySearchBar.setBackgroundColor(context.getResources()
                    .getColor(android.R.color.white, context.getTheme()));
            binder.mainActivitySortButtonsLinearLayout.setVisibility(View.GONE);
            binder.mainActivitySearchButton.setText("");
            binder.propertiesRecyclerFloatingButton.setVisibility(View.GONE);
        }
    }

    /*private void setViewportByName(String name){
        LatLngBounds bounds;
        String apiKey = GeneralUtils.getMatadataFromManifest(context, "com.google.android.geo.API_KEY");

        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_TEXT_SEARCH + OUT_JSON);
        try {
            sb.append(URLEncoder.encode(name, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&key=" + apiKey);
        sb.append("&components=country:IL");//TODO set locale for another countries

       *//* ConnectionsManager.getInstance(getActivity()).apiRequest(sb, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d(TAG, "request result: " + ((BallabaOkResponse)entity).body);

            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, "request result: " + entity);

            }
        });*//*
        *//*ConnectionsManager.UrlTask.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder result = ConnectionsManager.getInstance(getActivity()).apiRequest(sb);
                Log.d(TAG, "request result: " + result);
            }
        });*//*

    }*/

    public void setListAdapterToDeviceAddress(ListView listView){
        ArrayAdapter myAddressAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_single_choice/*R.layout.one_item*//*R.layout.fragment_publish_job_location*/);
        myAddressAdapter.add(getDeviceAddress(context));
        listView.setAdapter(myAddressAdapter);
    }

    public String getDeviceAddress(Context context){
        LocationManager lm;
        Location location = null;
        Geocoder geo = null;

        List<Address> addresses = null;
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        getPermissionsToGps();
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        geo = new Geocoder(context.getApplicationContext(), Locale.getDefault());

        try {
            if (location != null && geo != null)
                addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getLocality();
    }


    public void getPermissionsToGps(){
        Log.d(TAG, "registerReadSmsReceiver");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((MainActivity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_CODE_GPS_PERMISSION);
        }else{
            Log.d(TAG, "gps permission had already been given");
        }
    }

    //TODO add onRequestPermissionsResult:
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do something with user device location
                } else {
                    //denied
                }
            }
        }
    }*/

}

