package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.SplashActivity;
import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.SearchPropertiesPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PropertiesRecyclerFragment extends Fragment {
    private final String TAG = PropertiesRecyclerFragment.class.getSimpleName();
    private static final String PROPERTIES_KEY = "properties key";
    private final int REO_CODE_LOCATION_PERMISSION = 1;

    // TODO: Rename and change types of parameters
    //private List<BallabaProperty> mParam;
    private View view;
    private BallabaResponseListener listener;
    private boolean isGPSPermissionGranted;
    private PropertiesRecyclerAdapter rvAdapter;
    private RecyclerView rvProperties;
    private ArrayList<BallabaPropertyResult> properties;

    private SearchPropertiesPresenter presenter;
    private static ActivityMainLayoutBinding binder;
    private LayoutInflater inflater;

    private OnFragmentInteractionListener mListener;

    public PropertiesRecyclerFragment() {}

    // TODO: Rename and change types and number of parameters
    public static PropertiesRecyclerFragment newInstance(ActivityMainLayoutBinding mBinder, String param) {
        binder = mBinder;
        PropertiesRecyclerFragment fragment = new PropertiesRecyclerFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROPERTIES_KEY, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binder = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_properties_recycler);

        //binder.setPresenter(presenter);

        if (getArguments() != null) {
            //get arguments here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_properties_recycler, container, false);
        this.inflater = inflater;
        //DataBindingUtil.inflate(
        //        inflater, R.layout.fragment_properties_recycler, null, false);
        //presenter = new SearchPropertiesPresenter(getActivity(), binder, mParam);

        initRecycler(view);
        getProperties();

        return view;//binder.getRoot();
    }

    private void initRecycler(View view) {
        properties = BallabaSearchPropertiesManager.getInstance(getContext()).getResults();
        Log.d(TAG, "properties: " + properties);
                //PropertiesManager.getInstance(getContext()).getProperties();//new ArrayList<BallabaProperty>();
        //TODO moving binder across fragments when a specific widget is in the child fragment and
        //TODO not in the parent activity cause binder not be able to see the specific widget.
        //TODO that is why binder.getRoot() returns null. use findViewById instead!!!
        //rvProperties = (RecyclerView)binder.getRoot().findViewById(R.id.properties_recycler_RV);

        rvProperties = (RecyclerView)view.findViewById(R.id.properties_recycler_RV);
        //GridLayoutManager manager = new GridLayoutManager(getContext(), 2);//TODO to display 2 properties in a row
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAdapter = new PropertiesRecyclerAdapter(getContext(), properties);
        rvProperties.setLayoutManager(manager);
        rvProperties.setAdapter(rvAdapter);
    }

    private void getProperties(){
        listener = new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                properties = BallabaSearchPropertiesManager.getInstance(getContext())
                        .parsePropertyResults(((BallabaOkResponse)entity).body);

                Log.d(TAG, "properties: " + properties.get(0).formattedAddress+":"+properties.get(1).formattedAddress);
                BallabaSearchPropertiesManager.getInstance(getContext()).appendProperties(
                        properties, false);

                //TODO notifyDataSetChanged doesn't refresh properties. that is why i set adapter again
                rvAdapter = new PropertiesRecyclerAdapter(getContext(), properties);
                rvProperties.setAdapter(rvAdapter);
                //rvAdapter.notifyDataSetChanged();

            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                ((BaseActivity)getActivity()).getDefaultSnackBar(binder.getRoot(), getResources().getString(R.string.error_network_internal), true);
            }
        };

        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            BallabaLocationManager.getInstance(getContext()).getLocation(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "Location: " + location);
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        BallabaSearchPropertiesManager.getInstance(getContext()).getPropertiesByLatLng(latLng, listener, true);
                    } /*else {
                        BallabaSearchPropertiesManager.getInstance(SplashActivity.this).getRandomProperties(listener, true);
                    }*/
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            });

        }else {
            requestPermissions(new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }
                , REO_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REO_CODE_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "GPS granted");
            isGPSPermissionGranted = true;
            getProperties();

        } else if (requestCode == REO_CODE_LOCATION_PERMISSION){
            Log.d(TAG, "GPS not granted");
            BallabaSearchPropertiesManager.getInstance(getContext()).getRandomProperties(listener, true);
        }
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

        /*DataBindingUtil.inflate(
                inflater, R.layout.fragment_properties_recycler, null, false);
        presenter = new SearchPropertiesPresenter(getActivity(), binder, mParam);
*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
