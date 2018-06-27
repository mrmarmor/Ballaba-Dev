package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.Viewport;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14/05/2018.
 */

public class ViewportsManager {
    private final String TAG = ViewportsManager.class.getSimpleName();

    private static ViewportsManager instance;
    private Context context;
    private List<Viewport> viewports = new ArrayList<>();

    public static ViewportsManager getInstance(Context context) {
        if(instance == null){
            instance = new ViewportsManager(context);
        }
        return instance;
    }

    private ViewportsManager(Context context){
        this.context = context;
    }

    public List<Viewport> getViewports() {
        return viewports;
    }
    public void setViewports(List<Viewport> viewports){
        this.viewports = viewports;
    }

    // Viewports management tools methods
    public void addViewports(List<Viewport> viewports){
        this.viewports.addAll(viewports);
    }
    public void addViewport(Viewport viewport){
        viewports.add(viewport);
    }
    public void removeViewport(BallabaPropertyFull property){
        viewports.remove(property);
    }
    public void removeViewport(int position){
        viewports.remove(position);
    }
    public Viewport getViewport(int position){
        return viewports.get(position);
    }
    public Viewport getViewportById(String id){
        for (Viewport property : viewports)
            if (property.id.equals(id))
                return property;

        return null;
    }

    public ArrayList<Viewport> parseViewportsResults(String body){
        try{
            ArrayList<Viewport> results = new ArrayList<>();
            JSONArray jsonArr = new JSONArray(body);

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject res = jsonArr.getJSONObject(i);
                Integer id = res.getInt("id");
                String ne_lat = res.getString("ne_lat");
                String ne_lng = res.getString("ne_lng");
                String sw_lat = res.getString("sw_lat");
                String sw_lng = res.getString("sw_lng");
                String name = StringUtils.getInstance(true).formattedHebrew(res.getString("name"));
                byte[] map_image = Base64.decode(res.getString("image"), Base64.DEFAULT);

                LatLng ne = new LatLng(Double.parseDouble(ne_lat), Double.parseDouble(ne_lng));
                LatLng sw = new LatLng(Double.parseDouble(sw_lat), Double.parseDouble(sw_lng));
                LatLngBounds bounds = LatLngBounds.builder().include(ne).include(sw).build();

                Viewport viewport = new Viewport(id.toString(), name, bounds, map_image, 14);

                results.add(viewport);
            }
            return results;

        } catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }
}
