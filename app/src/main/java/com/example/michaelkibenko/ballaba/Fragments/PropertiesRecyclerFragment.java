package com.example.michaelkibenko.ballaba.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertiesRecyclerBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PropertiesRecyclerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = PropertiesRecyclerFragment.class.getSimpleName();
    private final int SWIPE_TO_REFRESH_DISPLAY_DURATION = 1000;//1 second
    private static final String PROPERTIES_KEY = "properties key";
    private final int REO_CODE_LOCATION_PERMISSION = 1;

    private int firstProperty, nextProperty;

    private PropertiesRecyclerFragment fragment;
    private Context context;
    private View view;
    private BallabaResponseListener listener;
    private boolean isGPSPermissionGranted, isLocationApproved;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private PropertiesRecyclerAdapter rvAdapter;
    //private RecyclerView rvProperties;
    private ArrayList<BallabaPropertyResult> properties;

    //private PropertyItemPresenter presenter;
    private FragmentPropertiesRecyclerBinding binder;
    //private LayoutInflater inflater;

    private LatLng latLng;

    public PropertiesRecyclerFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static PropertiesRecyclerFragment newInstance() {
        //binder = mBinder;
        PropertiesRecyclerFragment fragment = new PropertiesRecyclerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        //refreshItems();
        //getProperties();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_properties_recycler, null, false);
        initRecycler(view);
        checkForLocation();
        //getProperties();

        return binder.getRoot();
    }

    private void checkForLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            BallabaLocationManager.getInstance(context).getLastKnownLocation(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "Location: " + location);
                    if (location != null) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        isLocationApproved = true;
                    }
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
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    , REO_CODE_LOCATION_PERMISSION);
        }
    }

    public void refreshPropertiesRecycler() {
        properties = BallabaSearchPropertiesManager.getInstance(context).getResults();
        rvAdapter.updateList(properties);
    }

    private void initRecycler(View view) {
        properties = BallabaSearchPropertiesManager.getInstance(context).getResults();
        Log.d(TAG, "properties: " + properties);

        //TODO moving binder across fragments when a specific widget is in the child fragment and
        //TODO not in the parent activity cause binder not be able to see the specific widget.
        //TODO that is why binder.getRoot() returns null. use findViewById instead!!!
        //rvProperties = (RecyclerView)binder.getRoot().findViewById(R.id.properties_recycler_RV);

        //rvProperties = (RecyclerView)view.findViewById(R.id.properties_recycler_RV);
        //GridLayoutManager manager = new GridLayoutManager(getContext(), 2);//TODO to display 2 properties in a row
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rvAdapter = new PropertiesRecyclerAdapter(context, getFragmentManager(), properties, false);
        binder.propertiesRecyclerRV.setLayoutManager(manager);
        binder.propertiesRecyclerRV.setAdapter(rvAdapter);

        binder.propertiesRecyclerSwipeToRefresh.setOnRefreshListener(this);

        binder.propertiesRecyclerRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (((MainActivity) context).presenter.filterState != MainPresenter.FilterState.NO_FILTER) {
                    ((MainActivity) context).presenter.filterStateUIChanger(MainPresenter.FilterState.NO_FILTER);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void getProperties() {
        listener = new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                properties = BallabaSearchPropertiesManager.getInstance(context)
                        .parsePropertyResults(((BallabaOkResponse) entity).body);

                Log.d(TAG, "properties: " + properties.get(0).formattedAddress + ":" + properties.get(1).formattedAddress);
                BallabaSearchPropertiesManager.getInstance(context).appendProperties(properties, false);

                rvAdapter.updateList(properties);

            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                ((BaseActivity) getActivity()).getDefaultSnackBar(binder.getRoot(), getResources().getString(R.string.error_network_internal), true);
            }
        };

        if (isLocationApproved) {
            BallabaSearchPropertiesManager.getInstance(context).getPropertiesByLatLng(latLng, listener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REO_CODE_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "GPS granted");
            isGPSPermissionGranted = true;
            getProperties();//to show properties by latLng

        } else if (requestCode == REO_CODE_LOCATION_PERMISSION) {
            Log.d(TAG, "GPS not granted");
            BallabaSearchPropertiesManager.getInstance(context).getRandomProperties(listener);
        }
    }

    public void sortProperties(@MainPresenter.SORT_TYPE final int type) {
        try {
            Collections.sort(properties, new Comparator<BallabaPropertyResult>() {
                @Override
                public int compare(final BallabaPropertyResult FIRST, final BallabaPropertyResult NEXT) {
                    switch (type) {
                        case MainPresenter.SORT_TYPE.RELEVANT:
                            //TODO what is relevant??? maybe distance?
                            firstProperty = Integer.parseInt(FIRST.price);
                            nextProperty = Integer.parseInt(NEXT.price);
                            break;

                        case MainPresenter.SORT_TYPE.PRICE:
                            firstProperty = Integer.parseInt(FIRST.price);
                            nextProperty = Integer.parseInt(NEXT.price);
                            break;

                        case MainPresenter.SORT_TYPE.SIZE:
                            firstProperty = Integer.parseInt(FIRST.size);
                            nextProperty = Integer.parseInt(NEXT.size);
                            break;

                        case MainPresenter.SORT_TYPE.NUMBER_OF_ROOMS://rooms can be a fraction
                            return Double.parseDouble(FIRST.roomsNumber) > Double.parseDouble(NEXT.roomsNumber) ? -1 : 1;
                    }

                    //TODO here we can add option to reverse sort by switching numbers
                    return firstProperty > nextProperty ? 1 : -1;
                }
            });
            rvAdapter.updateList(properties);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //hide swipeToRefresh progressIcon after 1 second
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (binder.propertiesRecyclerSwipeToRefresh.isRefreshing())
                    binder.propertiesRecyclerSwipeToRefresh.setRefreshing(false);
            }
        }, SWIPE_TO_REFRESH_DISPLAY_DURATION);

        refreshItems();
    }

    public void onRefreshAnimation(boolean is) {
        binder.propertiesRecyclerSwipeToRefresh.setRefreshing(is);
    }

    //TODO to display 1 or 2 properties in a row
    /*private void arrangeLayoutManagerToDisplaySingleAndDuplicateItems(final GridLayoutManager layoutManager){
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 0) {
                    return 1;
                }

                return 2;
            }
        });
    }*/

    private void lazyLoading() {
        final BallabaSearchPropertiesManager propertiesManager = BallabaSearchPropertiesManager.getInstance(context);

        BallabaSearchPropertiesManager.getInstance(context).getLazyLoadingResults(false, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ArrayList<BallabaPropertyResult> properties =
                        propertiesManager.parsePropertyResults(((BallabaOkResponse) entity).body);
                propertiesManager.appendProperties(properties, true);

                rvAdapter.updateList(propertiesManager.getResults(), true);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, "error fetching offset properties");
            }
        });
    }

    private void refreshItems() {
        onRefreshAnimation(true);
        final BallabaSearchPropertiesManager propertiesManager = BallabaSearchPropertiesManager.getInstance(context);

        BallabaSearchPropertiesManager.getInstance(context).getLazyLoadingResults(true, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                onRefreshAnimation(false);
                ArrayList<BallabaPropertyResult> properties =
                        propertiesManager.parsePropertyResults(((BallabaOkResponse) entity).body);
                propertiesManager.appendProperties(properties, false);

                rvAdapter.updateList(propertiesManager.getResults(), false);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, "error fetching offset properties");
                onRefreshAnimation(false);
            }
        });
    }


}
