package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class BallabaLocationManager implements LocationListener{

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; //10 meters
    private static final long MIN_TIME_BW_UPDATES = 1; //one minute
    private static BallabaLocationManager instance;
    private Location location;
    private LocationManager locationManager;
    private Context context;

    public static BallabaLocationManager getInstance(Context context) {
        if(instance == null){
            instance = new BallabaLocationManager(context);
        }
        return instance;
    }

    private BallabaLocationManager(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }catch (SecurityException ex){
            ex.printStackTrace();
            //TODO set error flow
        }
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }
}
