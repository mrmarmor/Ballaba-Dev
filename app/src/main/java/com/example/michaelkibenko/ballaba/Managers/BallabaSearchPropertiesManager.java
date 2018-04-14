package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.FilterDimensions;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.AFTER_20;
import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.FIRST_20;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class BallabaSearchPropertiesManager {
    @IntDef({FIRST_20, AFTER_20})
    public @interface LAZY_LOADING_OFFSET_STATES {
        int FIRST_20 = 0;
        int AFTER_20 = 1;
    }

    private static final String TAG = BallabaSearchPropertiesManager.class.getSimpleName();
    private static BallabaSearchPropertiesManager instance;
    private Context context;
    private ArrayList<BallabaPropertyResult> results;
    private FilterDimensions filterDimensions;
    private String currentSearchEndpoint;


    public static BallabaSearchPropertiesManager getInstance(Context context) {
        if(instance == null){
            instance = new BallabaSearchPropertiesManager(context);
        }

        return instance;
    }

    private BallabaSearchPropertiesManager(Context context) {
        this.context = context;
        results = new ArrayList<>();
    }

    public ArrayList<BallabaPropertyResult> getResults() {
        return results;
    }
    public ArrayList<String> getResultsByLocation() {
        ArrayList<String> locations = new ArrayList<>();
        for (BallabaPropertyResult result : results)
            locations.add(result.formattedAddress);

        return locations;

    }

    public int getResultsCount(){
        return results.size();
    }

    public void appendProperties(ArrayList<BallabaPropertyResult> res, boolean isLazyLoading){
        if(isLazyLoading){
            results.addAll(res);
        }else {
            results = res;
        }
    }

    public void getRandomProperties(final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getRandomProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);

                    if(results != null){
                        appendProperties(results, true);
                    }else {
                        Log.e(TAG, "results is null, Json parse error");
                    }

                    callback.resolve(entity);
                }else {
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        });
    }

    public void getPropertiesByLatLng(final LatLng latLng, final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByLatLng(latLng,new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);

                    if(results != null){
                        appendProperties(results, true);
                    }else {
                        Log.e(TAG, "results is null, Json parse error");
                    }

                    callback.resolve(entity);
                }else {
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        });
    }

    public void getPropertiesByViewPort(LatLngBounds bounds, final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByViewPort(bounds, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);
                    BallabaSearchPropertiesManager.this.results = results;
                    callback.resolve(entity);
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        });
    }

    public void getPropertiesByAddressAndFilter(ArrayList<String> cities, FilterResultEntity filterResult, BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByAddress(cities, filterResult, callback);
    }

    public void getLazyLoadingResults(BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).lazyLoading(callback);
    }

    public String getCurrentSearchEndpoint() {
        return currentSearchEndpoint;
    }

    public void setCurrentSearchEndpoint(String currentSearchEndpoint) {
        this.currentSearchEndpoint = currentSearchEndpoint;
    }

    public BallabaPropertyResult getPropertyById(@Nullable ArrayList<BallabaPropertyResult> target, String id){
        if(target == null){
            target = getResults();
        }

        for (BallabaPropertyResult prop : target) {
            if(prop.id.equals(id)){
                return prop;
            }
        }

        return null;
    }

    public FilterDimensions parseFilterDimens(String result){
        try{
            JSONObject jsonObject = new JSONObject(result);
            String max_price = jsonObject.getString("max_price");
            String min_price = jsonObject.getString("min_price");
            String max_size = jsonObject.getString("max_size");
            String min_size = jsonObject.getString("min_size");
            String max_rooms = jsonObject.getString("max_rooms");
            String min_rooms = jsonObject.getString("min_rooms");

            FilterDimensions filterDimensions = new FilterDimensions(max_price, min_price, max_size, min_size, max_rooms, min_rooms);
            this.filterDimensions = filterDimensions;
            return filterDimensions;
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<BallabaPropertyResult> parsePropertyResults(String result){
        try{
            ArrayList<BallabaPropertyResult> returned = new ArrayList<>();
            JSONObject preObject = new JSONObject(result);
            JSONArray object = preObject.getJSONArray("properties");

            for (int i = 0; i < object.length(); i++) {
                JSONObject res = object.getJSONObject(i);
                String id = res.getString("id");
                String rooms = res.getString("rooms");
                String price = res.getString("price");
                String size = res.getString("size");
                String formattedAddress = res.getString("formatted_address");
                String rentPeriod = res.getString("rent_period");
                String numberOfPayments = res.getString("no_of_payments");
                JSONArray photosJsonArray = res.getJSONArray("photos");
                ArrayList<String> photos  = new ArrayList<>();
                if (photosJsonArray != null) {
                    int len = photosJsonArray.length();
                    for (int g=0;g<len;g++){
                        photos.add(photosJsonArray.getJSONObject(g).getString("photo_url"));
                    }
                }
                boolean isSaved = res.getBoolean("is_saved");

                LatLng latLng = new LatLng(Double.parseDouble(res.getString("lat")), Double.parseDouble(res.getString("lng")));

                boolean isGuarantee = res.getBoolean("is_guaranteed");

                //TODO change lat lng object

                BallabaPropertyResult propertyResult = new BallabaPropertyResult(id, rooms, price, size,
                        formattedAddress, rentPeriod, numberOfPayments, photos, isSaved, latLng, isGuarantee);

                returned.add(propertyResult);
            }
            return returned;

        }catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }
}
