package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Utils.GeneralUtils;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter.GooglePlacesFilter.CITIES;
import static com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter.GooglePlacesFilter.REGION;

/**
 * Created by User on 14/03/2018.
 */

public class GooglePlacesAdapter extends ArrayAdapter<String> implements Filterable {
    @StringDef({CITIES, REGION})
    public @interface GooglePlacesFilter {
        String CITIES = "&types=(cities)";
        String REGION = "&components=locality:";
    }

    //TODO if i want a deep search within a specific city:
    //https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyBF0dZnsnmBZ0Yuvyns6CX0bZwIK3jSGYA&components=country:IL&input=haifa+stadium
    private final String PLACES_API_BASE = EndpointsHolder.GOOGLE_PLACES_API
            , TYPE_AUTOCOMPLETE = "/autocomplete", OUT_JSON = "/json";
    private final String TAG = GooglePlacesAdapter.class.getSimpleName();

    private String apiKey ="";
    private ArrayList<String> resultList;
    private Context context = null;
    private @GooglePlacesFilter String gpFilter;

    public GooglePlacesAdapter(Context context, int textViewResourceId, @Nullable @GooglePlacesFilter String filter) {
        super(context, textViewResourceId);
        this.context = context;
        this.gpFilter = filter;

        apiKey = GeneralUtils.getMatadataFromManifest(context, "com.google.android.geo.API_KEY");
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
        if (resultList.size() > index)
            return resultList.get(index);
        else
            return "";
    }

    private ArrayList<String> autoComplete(final String INPUT, final @Nullable @GooglePlacesFilter String FILTER) {
        //ArrayList<String> resultList = null;
            ArrayList<String> descriptionList = null;

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + apiKey);
            sb.append(FILTER);
            sb.append("&components=country:IL");//TODO set locale for another countries
            try {
                sb.append("&input=" + URLEncoder.encode(INPUT, "utf8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            StringBuilder jsonResults = ConnectionsManager.getInstance(context).apiRequest(sb);
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
                Log.e(TAG, "Cannot process JSON results", e);
            }
            return descriptionList;
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = autoComplete(constraint.toString(), gpFilter);
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
}
