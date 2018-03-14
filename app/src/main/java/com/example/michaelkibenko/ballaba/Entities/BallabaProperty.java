package com.example.michaelkibenko.ballaba.Entities;

import android.content.Context;
import android.location.LocationManager;

import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;

import java.io.Serializable;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by User on 13/03/2018.
 */

public class BallabaProperty extends BallabaBaseEntity {
    private static BallabaProperty instance;
    private Context context;
    private String id, address, price;

    public static BallabaProperty getInstance(Context context) {
        if(instance == null){
            instance = new BallabaProperty(context);
        }
        return instance;
    }

    private BallabaProperty(Context context){
        this.context = context;
    }

    public BallabaProperty(String id, String address, String price) {
        this.id = id;
        this.address = address;
        this.price = price;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String address() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String price() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
