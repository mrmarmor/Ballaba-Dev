package com.example.michaelkibenko.ballaba.Presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.ContinueAddPropertyActivity;
import com.example.michaelkibenko.ballaba.Activities.MyPropertiesBaseActivity;
import com.example.michaelkibenko.ballaba.Activities.ProfileActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.SavedAreaActivity;
import com.example.michaelkibenko.ballaba.Activities.SavedPropertiesActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.FilterPagerAdapter;
import com.example.michaelkibenko.ballaba.Adapters.PropertiesPagerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Entities.FilterDimensions;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.OnBoardingFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.michaelkibenko.ballaba.Activities.MainActivity.SEARCH_BY_LOCATION_REQUEST_CODE;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.FilterState.FULL_FILTER;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.FilterState.MIDDLE_FILTER;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.FilterState.NO_FILTER;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.NUMBER_OF_ROOMS;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.PRICE;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.RELEVANT;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.SORT_TYPE.SIZE;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.ScreenState.AFTER_SEARCH;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.ScreenState.BEFORE_SEARCH;
import static com.example.michaelkibenko.ballaba.Presenters.MainPresenter.ScreenState.MAP;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainPresenter extends BasePresenter implements ConstraintLayout.OnFocusChangeListener
        /* , PropertiesRecyclerFragment.OnFragmentInteractionListener*/ {

    @IntDef({RELEVANT, PRICE, SIZE, NUMBER_OF_ROOMS})
    public @interface SORT_TYPE {
        int RELEVANT = 1;
        int PRICE = 2;
        int SIZE = 3;
        int NUMBER_OF_ROOMS = 4;
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2345;

    private final String TAG = MainPresenter.class.getSimpleName();
    public static final int REQ_CODE_SAVED_AREA = 1, REQ_CODE_SELECT_CITY = 2;

    @IntDef({NO_FILTER, MIDDLE_FILTER, FULL_FILTER})
    public @interface FilterState {
        int NO_FILTER = 1;
        int MIDDLE_FILTER = 2;
        int FULL_FILTER = 3;
    }

    @IntDef({BEFORE_SEARCH, AFTER_SEARCH, MAP})
    public @interface ScreenState {
        int BEFORE_SEARCH = 555;
        int AFTER_SEARCH = 666;
        int MAP = 888;
    }

    private Activity context;
    private FragmentManager fm;
    private ViewPager filterViewPager;
    private PropertiesPagerAdapter propertiesPagerAdapter;
    private FilterPagerAdapter filterPagerAdapter;
    private ActivityMainLayoutBinding binder;
    public Button.OnClickListener clickListener;
    private PropertiesRecyclerFragment propertiesFragment;
    private LayoutInflater inflater;
    private ConstraintLayout filterTransition, noFilterTransition, searchStateTransition, maplayoutTransition;
    private float middleFilterHeight;
    public @FilterState
    int filterState;
    public FilterResultEntity filterResult;
    private FilterDimensions filterDimensions;
    private ArrayList<String> citiesResults;
    private @ScreenState
    int screenState = ScreenState.BEFORE_SEARCH;
    private @ScreenState
    int beforeScreenState;

    public MainPresenter() {
    }

    public MainPresenter(Activity context, ActivityMainLayoutBinding binder, FragmentManager fm) {
        this.binder = binder;
        this.context = context;
        this.fm = fm;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup parent = context.getWindow().getDecorView().findViewById(android.R.id.content);
        this.noFilterTransition = inflater.inflate(R.layout.activity_main_layout, parent, false).findViewById(R.id.mainActivity_rootLayout);
        this.filterTransition = inflater.inflate(R.layout.activity_main_layout_filter_transition, parent, false).findViewById(R.id.mainActivity_rootLayout);
        this.searchStateTransition = inflater.inflate(R.layout.activity_main_layout_search_state_layout, parent, false).findViewById(R.id.mainActivity_rootLayout);
        this.maplayoutTransition = inflater.inflate(R.layout.activity_main_map_state_layout, parent, false).findViewById(R.id.mainActivity_rootLayout);
        propertiesFragment = PropertiesRecyclerFragment.newInstance();
        middleFilterHeight = context.getResources().getDimension(R.dimen.mainScreen_filter_middle_height);
        this.filterState = NO_FILTER;
        filterResult = new FilterResultEntity();
        citiesResults = new ArrayList<>();
        initDrawer();
        initViewPagerProperties();
        //TODO to be added only when after user selected city
        initFilter();
        changeScreenState(BEFORE_SEARCH);

        initNavBar();
    }

    private void initDrawer() {
        binder.mainActivityNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        binder.mainActivityDrawerLayout.closeDrawers();

                        //TODO here i need to add switch between menu items
                        switchScreenByMenuItem(menuItem);

                        return true;
                    }
                });
    }

    private void initViewPagerProperties() {
        propertiesPagerAdapter = new PropertiesPagerAdapter(context, binder, fm, propertiesFragment);
        binder.mainActivityPropertiesViewPager.setAdapter(propertiesPagerAdapter);
        binder.mainActivityPropertiesViewPager.setOffscreenPageLimit(2);
    }

    private void initNavBar() {
        BallabaUser ballabaUser = BallabaUserManager.getInstance().getUser();
        LinearLayout navBarHeader = (LinearLayout) binder.mainActivityNavigationView.getHeaderView(0);
        if (ballabaUser != null) {
            if (ballabaUser.getProfile_image() != null && !ballabaUser.getProfile_image().equals("null")) {
                CircleImageView circleImageView = navBarHeader.findViewById(R.id.imageAvatar_navigationView);
                Glide.with(context).load(ballabaUser.getProfile_image()).into(circleImageView);
            }
            if (ballabaUser.getFirst_name() != null && !ballabaUser.getFirst_name().equals("null") &&
                    ballabaUser.getLast_name() != null && !ballabaUser.getLast_name().equals("null")) {
                ((TextView) navBarHeader.findViewById(R.id.name_navigationView)).setText(ballabaUser.getFirst_name() + " " + ballabaUser.getLast_name());
            }
            ((TextView) navBarHeader.findViewById(R.id.phoneNumber_navigationView)).setText(ballabaUser.getPhone());
        }
    }

    private void initFilter() {
        //TODO initFilter just after response
        filterPagerAdapter = new FilterPagerAdapter(context, binder, fm, new FilterDimensions("100000", "0", "500", "10", "50", "1"));
        filterViewPager = binder.mainActivityFilterIncluded.mainActivityFilterViewPager;
        filterViewPager.setAdapter(filterPagerAdapter);
        filterViewPager.setOffscreenPageLimit(5);
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setOnFocusChangeListener(this);//new View.OnFocusChangeListener() {
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.requestFocus();
        binder.mainActivityFilterIncluded.mainActivityFilterRoot.setFocusableInTouchMode(true);
        filterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    filterStateUIChanger(FULL_FILTER);
                } else if (position == 0) {
                    filterStateUIChanger(FULL_FILTER);
                } else if (filterState != MIDDLE_FILTER) {
                    if (binder.mainActivityFilterIncluded.getRoot().getTag() != null) {
                        filterStateUIChanger(MIDDLE_FILTER);
                    }
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
                filterStateUIChanger(NO_FILTER);
            }
        });


        binder.mainActivityFilterIncluded.mainActivityFilterVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save the search object here and start request
                filterStateUIChanger(NO_FILTER);
                getPropertiesByAddressAndFilter(citiesResults);
            }
        });

        filterViewPager.setCurrentItem(4);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.mainActivity_filter_root) {
            if (hasFocus) {
                binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.VISIBLE);
            } else {
                binder.mainActivityFilterIncluded.mainActivityFilterRoot.setVisibility(View.GONE);
            }
        }
    }

    public void onClickToGoogleMap() {
        final int TO_GOOGLE_MAP = 0, BACK_TO_MAIN_SCREEN = 1;
        switch (binder.mainActivityPropertiesViewPager.getCurrentItem()) {
            case TO_GOOGLE_MAP:
                changeScreenState(ScreenState.MAP);
                binder.mainActivityPropertiesViewPager.setCurrentItem(1, false);
                binder.mainActivityToGoogleMapImageButton.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.enabled, context.getTheme()));
                binder.mainActivitySearchBar.setVisibility(View.GONE);
                binder.mainActivitySortButtonsLinearLayout.setVisibility(View.GONE);
                break;

            case BACK_TO_MAIN_SCREEN:
                changeScreenState(beforeScreenState);
                binder.mainActivityPropertiesViewPager.setCurrentItem(0, false);
                binder.mainActivityToGoogleMapImageButton.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.disabled, context.getTheme()));
                binder.mainActivitySearchBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClickToSelectCity() {
        Intent intentSelectCity = new Intent(context, SelectCitySubActivity.class);
        context.startActivityForResult(intentSelectCity, REQ_CODE_SELECT_CITY);
    }

    public void onClickDrawer() {
        binder.mainActivityDrawerLayout.openDrawer(Gravity.START);
    }

    public void onClickSortByRelevant() {
        propertiesFragment.sortProperties(RELEVANT);
    }

    public void onClickSortByPrice() {
        propertiesFragment.sortProperties(PRICE);
    }

    public void onClickSortBySize() {
        propertiesFragment.sortProperties(SIZE);
    }

    public void onClickSortByRooms() {
        propertiesFragment.sortProperties(NUMBER_OF_ROOMS);
    }

    public void onClickToFilter() {
        if (binder.mainActivityFilterIncluded.getRoot().getTag() != null) {
            if (Integer.parseInt((String) binder.mainActivityFilterIncluded.getRoot().getTag()) == FULL_FILTER) {
                filterStateUIChanger(FULL_FILTER);
            } else {
                filterStateUIChanger(MIDDLE_FILTER);
            }
        } else {
            filterStateUIChanger(MIDDLE_FILTER);
        }

    }

    public void filterStateUIChanger(@FilterState int state) {
        if (state != filterState) {
            //this is the previous state
            binder.mainActivityFilterIncluded.getRoot().setTag(this.filterState + "");
            this.filterState = state;
            onFilterUIChanged(state);
        }
    }

    public void onSearchFlowComplete(ArrayList<String> cities) {
        citiesResults = cities;
        if (!cities.isEmpty()) {
            binder.mainActivitySearchButton.setText(cities.get(0));
            getPropertiesByAddressAndFilter(cities);
            binder.mainActivityPropertiesViewPager.setCurrentItem(0);
        }
    }

    private void changeScreenState(@ScreenState int screenState) {
        if (this.screenState != screenState) {
            ConstraintSet set = new ConstraintSet();
            this.beforeScreenState = this.screenState;
            this.screenState = screenState;
            if (this.screenState == BEFORE_SEARCH) {
                binder.mainActivitySortButtonsLinearLayout.setVisibility(View.INVISIBLE);
                binder.openFilterButton.setVisibility(View.GONE);
                set.clone(noFilterTransition);
            } else if (this.screenState == AFTER_SEARCH) {
                binder.mainActivitySortButtonsLinearLayout.setVisibility(View.VISIBLE);
                binder.openFilterButton.setVisibility(View.VISIBLE);
                binder.mainActivitySearchBar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                set.clone(searchStateTransition);
            } else if (this.screenState == MAP) {
                this.binder.mainActivitySortButtonsLinearLayout.setVisibility(View.GONE);
                set.clone(maplayoutTransition);
            }
            TransitionManager.beginDelayedTransition(binder.mainActivityRootLayout);
            set.applyTo(binder.mainActivityRootLayout);
        } else {
            Log.e(TAG, "ScreenState is equals");
        }
    }

    private void switchScreenByMenuItem(MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_addProperty:
                intent = getIntent_addProperty();
                break;
            case R.id.nav_payments:

                break;
            case R.id.nav_myProperties:
                intent = new Intent(context, MyPropertiesBaseActivity.class);
                break;
            case R.id.nav_favorites:
                intent = new Intent(context, SavedPropertiesActivity.class);
                break;
            case R.id.nav_savedAreas:
                Intent goSavedAreas = new Intent(context, SavedAreaActivity.class);
                context.startActivityForResult(goSavedAreas, REQ_CODE_SAVED_AREA);
                break;
            case R.id.nav_changeName:

                break;
            case R.id.nav_conflicts:

                break;
            case R.id.nav_editProfile:
                intent = new Intent(context, ProfileActivity.class);
                break;
            case R.id.nav_settings:

                break;
            default:
        }

        if (intent != null) context.startActivity(intent);
    }

    private void getPropertiesByAddressAndFilter(ArrayList<String> cities) {
        propertiesFragment.onRefreshAnimation(true);
        BallabaSearchPropertiesManager.getInstance(context).getPropertiesByAddressAndFilter(cities, filterResult, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if (entity instanceof BallabaOkResponse) {
                    BallabaSearchPropertiesManager searchPropertiesManager = BallabaSearchPropertiesManager.getInstance(context);
                    ArrayList<BallabaPropertyResult> result = searchPropertiesManager.parsePropertyResults(((BallabaOkResponse) entity).body);
                    filterDimensions = searchPropertiesManager.parseFilterDimens(((BallabaOkResponse) entity).body);
                    searchPropertiesManager.appendProperties(result, false);
                    propertiesPagerAdapter.getPropertiesRecyclerFragment().refreshPropertiesRecycler();
                    updateFilterDimensions(filterDimensions);
                    changeScreenState(AFTER_SEARCH);
                    propertiesFragment.onRefreshAnimation(false);
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                //TODO server send message "null". when it will be ready, please use line below:
                //((BaseActivity) context).getDefaultSnackBar(binder.getRoot()
                //        , ((BallabaErrorResponse) entity).message, false).show();

                if (((BallabaErrorResponse) entity).statusCode == 404)
                    ((BaseActivity) context).getDefaultSnackBar(binder.getRoot()
                            , "לא נמצאו נכסים", false).show();
                propertiesFragment.onRefreshAnimation(false);
            }
        });
    }

    private void updateFilterDimensions(FilterDimensions filterDimensions) {
        filterPagerAdapter.updateFilterDimensions(filterDimensions);
    }

    private void onFilterUIChanged(int state) {
        ConstraintSet set = new ConstraintSet();
        if (state == NO_FILTER) {
            set.clone(searchStateTransition);
            set.constrainHeight(R.id.mainActivity_filter_included, binder.mainActivityFilterIncluded.getRoot().getHeight());
            binder.openFilterButton.setVisibility(View.VISIBLE);
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(android.R.color.transparent, context.getTheme()));
        } else if (state == MIDDLE_FILTER) {
            set.clone(filterTransition);
            set.constrainHeight(R.id.mainActivity_filter_included, (int) middleFilterHeight);
            binder.openFilterButton.setVisibility(View.GONE);
            binder.mainActivitySearchBar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        } else if (state == FULL_FILTER) {
            set.clone(filterTransition);
            binder.mainActivitySearchBar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
            set.constrainHeight(R.id.mainActivity_filter_included, ConstraintLayout.LayoutParams.MATCH_PARENT);
            binder.mainActivityBottomAnchor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }
        TransitionManager.beginDelayedTransition(binder.mainActivityRootLayout);
        set.applyTo(binder.mainActivityRootLayout);
    }

    private Intent getIntent_addProperty() {
        final int TWO_WEEKS = 14 * 24 * 60 * 60 * 1000;//TODO decide what will be expire date

        //TODO remove propertyId from sharedPrefs after user finished uploading property
        String propertyId = SharedPreferencesManager.getInstance(context).getString(
                SharedPreferencesKeysHolder.PROPERTY_ID, null);
        String uploadDateStr = SharedPreferencesManager.getInstance(context).getString(
                SharedPreferencesKeysHolder.PROPERTY_UPLOAD_DATE, null);

        Date expireDate = new Date(StringUtils.getInstance(true)
                .stringToTime(uploadDateStr) + TWO_WEEKS);
        Date now = new Date(Calendar.getInstance().getTimeInMillis());

        /*TODO TESTING*///propertyId = "1";/*TODO END OF TESTING*/
        if (propertyId == null //=> user had finished upload his property or had never uploaded any
                || (now.after(expireDate))) { //=> or user had not finished upload, but 14 days had passed
            //return new Intent(context, AddPropertyActivity.class);
            return new Intent(context, AddPropertyActivityNew.class);
        } else {// => user had started upload + had not finished yet + 14 days had not passed yet from starting uploading
            Intent intent = new Intent(context, ContinueAddPropertyActivity.class);
            intent.putExtra(PropertyDescriptionActivity.PROPERTY, propertyId);
            return intent;
        }
    }

    public void searchByLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            propertiesPagerAdapter.getPropertiesRecyclerFragment().onRefreshAnimation(true);
            BallabaLocationManager.getInstance(context).getLocation(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    BallabaSearchPropertiesManager.getInstance(context).getPropertiesByLatLng(latLng, new BallabaResponseListener() {
                        @Override
                        public void resolve(BallabaBaseEntity entity) {
                            propertiesPagerAdapter.getPropertiesRecyclerFragment().refreshPropertiesRecycler();
                            propertiesPagerAdapter.getPropertiesRecyclerFragment().onRefreshAnimation(false);

                        }

                        @Override
                        public void reject(BallabaBaseEntity entity) {
                            //TODO set error flow
                            propertiesPagerAdapter.getPropertiesRecyclerFragment().onRefreshAnimation(false);
                        }
                    });
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    SEARCH_BY_LOCATION_REQUEST_CODE);
        }
    }
}
