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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.michaelkibenko.ballaba.Activities.MainScreenActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.SearchViewPagerAdapter;
import com.example.michaelkibenko.ballaba.Common.ClassesCommunicationListener;
import com.example.michaelkibenko.ballaba.databinding.MainScreenLayoutBinding;

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

    private Context context;
    private FragmentManager fm;
    private PagerAdapter pagerAdapter;
    private MainScreenLayoutBinding binder;
    private ClassesCommunicationListener listener;
    //private GoogleMap googleMap;

    public MainPresenter() {}
    public MainPresenter(Context context, MainScreenLayoutBinding binder, FragmentManager fm){
        this.binder = binder;
        this.context = context;
        this.fm = fm;

        //initAutoCompleteTextView();
        initDrawer();
        initViewPager();

        //TODO as default the recyclerView will be shown. for now, map will be
        //mapPresenter = new MapPresenter(this.context, this.binder);
        //mapPresenter.openMapFragment();
    }

    private void initDrawer(){
        binder.mainScreenNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        binder.mainScreenDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

    }

    private void initViewPager(){
        pagerAdapter = new SearchViewPagerAdapter(context, binder, fm);
        binder.mainScreenViewPager.setAdapter(pagerAdapter);
    }

    public void onClickToSelectCity(){
        Intent intentSelectCity = new Intent(context, SelectCitySubActivity.class);
        ((Activity)context).startActivityForResult(intentSelectCity, REQ_CODE_SELECT_CITY);
    }

    public void onClickToGoogleMap(){
        binder.mainScreenViewPager.setCurrentItem(
                binder.mainScreenViewPager.getCurrentItem()==0? 1:0, false);
    }

    /*@Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, googleMap.getMaxZoomLevel()+"");
        this.googleMap = googleMap;

    }*/

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
            ActivityCompat.requestPermissions((MainScreenActivity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_CODE_GPS_PERMISSION);
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



/*public class SearchPlaceFragment extends PlaceAutocompleteFragment {

    private EditText editSearch;

    private View zzaRh;
    private View zzaRi;
    private EditText zzaRj;
    private LatLngBounds zzaRk;
    private AutocompleteFilter zzaRl;
    private PlaceSelectionListener zzaRm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View var4 = inflater.inflate(R.layout.fragment_search_place, container, false);

        editSearch = (EditText) var4.findViewById(R.id.searchPlaceFragment_editText);
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zzzG();
            }
        });

        return var4;
    }


    public void onDestroyView() {
        this.zzaRh = null;
        this.zzaRi = null;
        this.editSearch = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        this.zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        this.zzaRl = filter;
    }

    public void setText(CharSequence text) {
        this.editSearch.setText(text);
        //this.zzzF();
    }

    public void setHint(CharSequence hint) {
        this.editSearch.setHint(hint);
        this.zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;
    }

    private void zzzF() {
        boolean var1 = !this.editSearch.getText().toString().isEmpty();
        //this.zzaRi.setVisibility(var1?0:8);
    }

    private void zzzG() {
        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).setBoundsBias(this.zzaRk).setFilter(this.zzaRl).zzeq(this.editSearch.getText().toString()).zzig(1).build(this.getActivity());
            this.startActivityForResult(var2, 1);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if (var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(this.getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                Place var4 = PlaceAutocomplete.getPlace(this.getActivity(), data);
                if (this.zzaRm != null) {
                    this.zzaRm.onPlaceSelected(var4);
                }

                this.setText(var4.getName().toString());
            } else if (resultCode == 2) {
                Status var5 = PlaceAutocomplete.getStatus(this.getActivity(), data);
                if (this.zzaRm != null) {
                    this.zzaRm.onError(var5);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }







    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchPlaceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchPlaceFragment newInstance(String param1, String param2) {
        SearchPlaceFragment fragment = new SearchPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}*/

