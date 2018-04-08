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
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.ViewPagerFilterAdapter;
import com.example.michaelkibenko.ballaba.Adapters.ViewPagerPropertiesAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaSelectedCityListener;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.NUMBER_OF_ROOMS;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.PRICE;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.RELEVANT;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.SIZE;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainPresenter extends BasePresenter implements ConstraintLayout.OnFocusChangeListener
        , PropertiesRecyclerFragment.OnFragmentInteractionListener {

    @IntDef({RELEVANT, PRICE, SIZE, NUMBER_OF_ROOMS})
    public @interface SORT_TYPE {
        int RELEVANT = 1;
        int PRICE = 2;
        int SIZE = 3;
        int NUMBER_OF_ROOMS = 4;
    }

    private final String TAG = MainPresenter.class.getSimpleName();
    public static final int REQ_CODE_GPS_PERMISSION = 1, REQ_CODE_SELECT_CITY = 2;

    public @interface FilterState { int NO_FILTER = 1; int MIDDLE_FILTER = 2; int FULL_FILTER = 3; }

    private Context context;
    private FragmentManager fm;
    private ViewPager filterViewPager;
    private PagerAdapter propertiesPagerAdapter;
    private ViewPagerFilterAdapter filterPagerAdapter;
    private ActivityMainLayoutBinding binder;
    private BallabaSelectedCityListener listener;
    public Button.OnClickListener clickListener;
    private PropertiesRecyclerFragment.OnFragmentInteractionListener mListener;
    private PropertiesRecyclerFragment propertiesFragment;
    private LayoutInflater inflater;
    private ConstraintLayout filterTransition, noFilterTransition;
    //private GoogleMap googleMap;
    private float middleFilterHeight;
    public @FilterState int filterState;
    public FilterResultEntity filterResult;

    public MainPresenter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm){
        this.binder = binder;
        this.context = context;
        this.fm = fm;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup parent = ((MainActivity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        this.noFilterTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_main_layout, parent, false).findViewById(R.id.mainActivity_rootLayout);
        this.filterTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_main_layout_filter_transition, parent, false).findViewById(R.id.mainActivity_rootLayout);
        propertiesFragment = PropertiesRecyclerFragment.newInstance(null);
        middleFilterHeight = context.getResources().getDimension(R.dimen.mainScreen_filter_middle_height);
        this.filterState = FilterState.NO_FILTER;
        filterResult = new FilterResultEntity();
        initDrawer();
        initViewPagerProperties();
        //TODO to be added only when after user selected city
        initFilter();
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

    private void initViewPagerProperties(){
        propertiesPagerAdapter = new ViewPagerPropertiesAdapter(context, binder, fm, propertiesFragment);
        binder.mainActivityPropertiesViewPager.setAdapter(propertiesPagerAdapter);
    }

    private void initFilter(){
        filterPagerAdapter = new ViewPagerFilterAdapter(context, binder, fm);
        filterViewPager = binder.mainActivityFilterIncluded.mainActivityFilterViewPager;
        filterViewPager.setAdapter(filterPagerAdapter);
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setOnFocusChangeListener(this);//new View.OnFocusChangeListener() {
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.requestFocus();
        //binder.mainActivityFilterRoot.clearFocus();
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setFocusableInTouchMode(true);
        filterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1 && filterState != FilterState.FULL_FILTER){
                    filterStateUIChanger(FilterState.FULL_FILTER);
                }else if(filterState != FilterState.MIDDLE_FILTER){
                    filterStateUIChanger(FilterState.MIDDLE_FILTER);
                }
                filterPagerAdapter.onFilterButtonsStateChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        binder.mainActivityFilterIncluded.mainActivityFilterXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO clear the search object here
                filterStateUIChanger(FilterState.NO_FILTER);
            }
        });


        binder.mainActivityFilterIncluded.mainActivityFilterVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save the search object here and start request
                filterStateUIChanger(FilterState.NO_FILTER);
            }
        });

        filterViewPager.setCurrentItem(4);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "hiding: " + binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility() + "");
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.GONE);
        Log.d(TAG, "hiding: " + binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility() + "");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.mainActivity_filter_root) {
            if (hasFocus){
                binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "hiding: " + binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility() + "");
                binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.GONE);
                Log.d(TAG, "hiding: " + binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility() + "");
            }
        }
    }
 /*   public void hideFilterBar(){
        Log.d(TAG, "hiding: " + binder.mainActivityFilterRoot.getVisibility() + "");
        binder.mainActivityFilterRoot.setVisibility(View.GONE);
        Log.d(TAG, "hiding: " + binder.mainActivityFilterRoot.getVisibility() + "");

    }*/

    public void onClickToGoogleMap(){
        final int TO_GOOGLE_MAP = 0, BACK_TO_MAIN_SCREEN = 1;
        switch (binder.mainActivityPropertiesViewPager.getCurrentItem()){
            case TO_GOOGLE_MAP:
                binder.mainActivityPropertiesViewPager.setCurrentItem(1, false);
                binder.mainActivityToGoogleMapImageButton.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.enabled, context.getTheme()));
                binder.mainActivitySearchBar.setVisibility(View.GONE);
                binder.mainActivitySortButtonsLinearLayout.setVisibility(View.GONE);
                break;

            case BACK_TO_MAIN_SCREEN:
                binder.mainActivityPropertiesViewPager.setCurrentItem(0, false);
                binder.mainActivityToGoogleMapImageButton.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.disabled, context.getTheme()));
                binder.mainActivitySearchBar.setVisibility(View.VISIBLE);
                binder.mainActivitySortButtonsLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    public void onClickToSelectCity(){
        Intent intentSelectCity = new Intent(context, SelectCitySubActivity.class);
        ((Activity)context).startActivityForResult(intentSelectCity, REQ_CODE_SELECT_CITY);
    }

    public void onClickDrawer(){
        binder.mainActivityDrawerLayout.openDrawer(Gravity.START);
    }

    public void onClickSortByRelevant(){
        Toast.makeText(context, "relevant clicked", Toast.LENGTH_SHORT).show();
        propertiesFragment.sortProperties(RELEVANT);
    }
    public void onClickSortByPrice(){
        Toast.makeText(context, "price clicked", Toast.LENGTH_SHORT).show();
        propertiesFragment.sortProperties(PRICE);
    }
    public void onClickSortBySize(){
        Toast.makeText(context, "size clicked", Toast.LENGTH_SHORT).show();
        propertiesFragment.sortProperties(SIZE);
    }
    public void onClickSortByRooms(){
        Toast.makeText(context, "rooms clicked", Toast.LENGTH_SHORT).show();
        propertiesFragment.sortProperties(NUMBER_OF_ROOMS);
    }

    public void onClickToFilter(){
        if(binder.mainActivityFilterIncluded.getRoot().getTag() != null) {
            if (Integer.parseInt((String)binder.mainActivityFilterIncluded.getRoot().getTag()) == FilterState.FULL_FILTER) {
                filterStateUIChanger(FilterState.FULL_FILTER);
            } else {
                filterStateUIChanger(FilterState.MIDDLE_FILTER);
            }
        }else {
            filterStateUIChanger(FilterState.MIDDLE_FILTER);
        }

    }

    public void filterStateUIChanger(@FilterState int state){
        if(state != filterState){
            //this is the previous state
            binder.mainActivityFilterIncluded.getRoot().setTag(this.filterState+"");
            this.filterState = state;
            onFilterUIChanged(state);
        }
    }

    private void onFilterUIChanged(int state){
        ConstraintSet set = new ConstraintSet();
        if (state == FilterState.NO_FILTER){
            set.clone(noFilterTransition);
            set.constrainHeight(R.id.mainActivity_filter_included, binder.mainActivityFilterIncluded.getRoot().getHeight());
            binder.propertiesRecyclerFloatingButton.setVisibility(View.VISIBLE);
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(android.R.color.transparent, context.getTheme()));
        } else if (state == FilterState.MIDDLE_FILTER){
            set.clone(filterTransition);
            set.constrainHeight(R.id.mainActivity_filter_included, (int)middleFilterHeight);
            binder.propertiesRecyclerFloatingButton.setVisibility(View.GONE);
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }else if(state == FilterState.FULL_FILTER){
            set.clone(filterTransition);
            set.constrainHeight(R.id.mainActivity_filter_included, ConstraintLayout.LayoutParams.MATCH_PARENT);
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }
        TransitionManager.beginDelayedTransition(binder.mainActivityRootLayout);
        set.applyTo(binder.mainActivityRootLayout);
    }

    public Button.OnClickListener getClickListener(){
        return clickListener;
    }

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
        //TODO checkPermissions
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

