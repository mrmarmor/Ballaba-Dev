package com.example.michaelkibenko.ballaba.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.SearchActivity;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchPlaceFragment extends Fragment {
    private AutoCompleteTextView actvSearchPlace;

    public SearchPlaceFragment() {}
    public static SearchPlaceFragment newInstance() {
        Bundle args = new Bundle();
        SearchPlaceFragment fragment = new SearchPlaceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_place, container, false);

        actvSearchPlace = v.findViewById(R.id.searchPlaceFragment_autoCompleteTextView);

        //TODO if you want actvSearchPlace display device current address:
        //setListAdapterToDeviceAddress(listView);

        final GooglePlacesAcAdapter dataAdapter = new GooglePlacesAcAdapter(
                getActivity(), android.R.layout.simple_list_item_1);
        actvSearchPlace.setAdapter(dataAdapter);

        actvSearchPlace.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {actvSearchPlace.setAdapter(dataAdapter); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        actvSearchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvSearchPlace.setText(((TextView)view).getText());
            }
        });

        return v;
    }

    public void setListAdapterToDeviceAddress(ListView listView){
        ArrayAdapter myAddressAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice/*R.layout.one_item*//*R.layout.fragment_publish_job_location*/);
        myAddressAdapter.add(getDeviceAddress(getActivity()));
        listView.setAdapter(myAddressAdapter);
    }

    public String getDeviceAddress(Context context){
        LocationManager lm;
        Location location = null;
        Geocoder geo = null;

        List<Address> addresses = null;
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //get the last known latitude longitude from network
        getPermissionsToGps(getView());


        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return null;
*/
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

    private final String TAG = SearchPlaceFragment.class.getSimpleName();
    private final int GPS_PERMISSION_REQ_CODE = 1;
    public void getPermissionsToGps(View v){
        Log.d(TAG, "registerReadSmsReceiver");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_PERMISSION_REQ_CODE);

        }else{
            Log.d(TAG, "gps permission had already been given");
        }
    }

}

class GooglePlacesAcAdapter extends ArrayAdapter<String> implements Filterable {
    private final String PLACES_API_BASE = EndpointsHolder.GOOGLE_PLACES_API
            , TYPE_AUTOCOMPLETE = "/autocomplete", OUT_JSON = "/json"
            , API_KEY = getMatadataFromManifest("com.google.android.geo.API_KEY");
    private final String TAG = GooglePlacesAcAdapter.class.getSimpleName();

    private ArrayList<String> resultList;
    private Context context = null;
    public GooglePlacesAcAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public int getCount() {
        if(resultList != null)
            return resultList.size();
        else
            return 0;
    }

    @Override
    public String getItem(int index) {
        if (resultList.size() > 0)
            return resultList.get(index);
        else
            return null;
    }

    public ArrayList<String> autoComplete(String input) {
        ArrayList<String> resultList = null;
        ArrayList<String> descriptionList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:IL");//TODO set locale for another countries
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection)url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("tag google places", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("tag google places", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray jsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList(jsonArray.length());
            descriptionList = new ArrayList(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                resultList.add(jsonArray.getJSONObject(i).toString());
                descriptionList.add(jsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("tag google places", "Cannot process JSON results", e);
        }

        return descriptionList;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = autoComplete(constraint.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private String getMatadataFromManifest(String key){
        String mApiKey = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            mApiKey = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        return mApiKey;
    }
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

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchPlaceFragment.
     *//*
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

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}*/
