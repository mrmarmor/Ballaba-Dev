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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.ViewPagerFilterAdapter;
import com.example.michaelkibenko.ballaba.Adapters.ViewPagerPropertiesAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaSelectedCityListener;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.io.IOException;
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

    public @interface SearchState { int FILTERED = 1; int NOT_FILTERED = 2; }

    private Context context;
    private FragmentManager fm;
    private ViewPager filterViewPager;
    private PagerAdapter propertiesPagerAdapter, filterPagerAdapter;
    private ActivityMainLayoutBinding binder;
    private BallabaSelectedCityListener listener;
    public Button.OnClickListener clickListener;
    private PropertiesRecyclerFragment.OnFragmentInteractionListener mListener;
    private PropertiesRecyclerFragment propertiesFragment;
    //private GoogleMap googleMap;

    public MainPresenter(Context context, ActivityMainLayoutBinding binder, FragmentManager fm){
        this.binder = binder;
        this.context = context;
        this.fm = fm;

        propertiesFragment = PropertiesRecyclerFragment.newInstance(null);
        initDrawer();
        initViewPagerProperties();
        //TODO to be added only when after user selected city
        initViewPagerFilter();
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

    private void initViewPagerFilter(){
        filterPagerAdapter = new ViewPagerFilterAdapter(context, binder, fm);
        filterViewPager = binder.mainActivityFilterIncluded.mainActivityFilterViewPager;
        filterViewPager.setAdapter(filterPagerAdapter);

        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setOnFocusChangeListener(this);//new View.OnFocusChangeListener() {

        /*if (context instanceof PropertiesRecyclerFragment.OnFragmentInteractionListener) {
            mListener = (PropertiesRecyclerFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
 /*           @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.mainActivity_filter_included && !hasFocus) {
                    Log.d(TAG, "hiding: "+binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility()+"");
                    binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.GONE);
                    Log.d(TAG, "hiding: "+binder.mainActivityFilterIncluded.mainActivityFilterRoot.getVisibility()+"");

                }
            }
        });*/
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.requestFocus();
        //binder.mainActivityFilterRoot.clearFocus();
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setFocusableInTouchMode(true);
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
        Toast.makeText(context, "filter clicked", Toast.LENGTH_SHORT).show();
        UiUtils.instance(true, context).setFilterBarVisibility(true);

    }

    public void onClickFilterButton(/*int position*/){
        Toast.makeText(context, "filter clicked", Toast.LENGTH_SHORT).show();
        filterViewPager.setCurrentItem(1/*position*/);
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
            //app:layout_constraintBottom_toTopOf="@+id/mainActivity_viewPager" />
            constraintSet.connect(R.id.mainActivity_properties_viewPager, ConstraintSet.TOP, R.id.mainActivity_sortButtons_linearLayout, ConstraintSet.BOTTOM,0);
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

