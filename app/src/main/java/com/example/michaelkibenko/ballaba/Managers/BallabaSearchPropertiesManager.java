package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class BallabaSearchPropertiesManager {

    private static final String TAG = BallabaSearchPropertiesManager.class.getSimpleName();
    private static BallabaSearchPropertiesManager instance;
    private Context context;
    private ArrayList<BallabaPropertyResult> results;

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

    public void getRandomProperties(final BallabaResponseListener callback, boolean isLazy){
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

    public void getPropertiesByLatLng(final LatLng latLng, final BallabaResponseListener callback, boolean isLazy){
        String latLngStr = latLng.latitude + "," + latLng.longitude;
        ConnectionsManager.getInstance(context).getPropertyByLatLng(latLngStr, new BallabaResponseListener() {
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

    public ArrayList<BallabaPropertyResult> parsePropertyResults(String result){
        try{
            ArrayList<BallabaPropertyResult> returned = new ArrayList<>();
            JSONArray object = new JSONArray(result);

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
                ArrayList<String> photos = new ArrayList<>();
                if (photosJsonArray != null) {
                    int len = photosJsonArray.length();
                    for (int g=0;g<len;g++){
                        photos.add(photosJsonArray.get(g).toString());
                    }
                }
                boolean isSaved = res.getBoolean("is_saved");

                BallabaPropertyResult propertyResult = new BallabaPropertyResult(id, rooms, price, size,
                        formattedAddress, rentPeriod, numberOfPayments, photos, isSaved);

                returned.add(propertyResult);
            }

            Log.d(TAG, returned.get(0).id);
            return returned;

        }catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }
}
