package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Manifest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by michaelkibenko on 08/03/2018.
 */

public class BallabaLocationManager {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; //10 meters
    private static final long MIN_TIME_BW_UPDATES = 0; //one minute
    private static final String TAG = BallabaLocationManager.class.getSimpleName();
    private static BallabaLocationManager instance;
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
        locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
    }

    public void getLocation(LocationListener locationListener) {
        try {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
//                    0, locationListener);
           locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
            //locationListener.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }catch (NullPointerException | SecurityException ex){
            ex.printStackTrace();
        }
    }

    public void getLastKnownLocation(LocationListener locationListener){
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationListener.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }else{
            Log.e(TAG, "Not permitted task");
        }
    }

    public LatLng locationGeoCoder(String strAddress){
        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());

            } else {
                return null;
            }

        } catch (IOException e){
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
    public String locationGeoCoder(LatLng latLng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            //TODO optional values:
            //String city = addresses.get(0).getAddressLine(1);
            //String country = addresses.get(0).getAddressLine(2);

            return address;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public interface OnGoogleMapListener {
        void OnGoogleMap(GoogleMap googleMap);
    }
}
