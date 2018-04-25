package com.example.michaelkibenko.ballaba.Entities;


import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class BallabaPropertyResult extends BallabaBaseEntity implements Serializable{
    public String id;
    public String roomsNumber;
    public String price;
    public String size;
    public String formattedAddress;
    public ArrayList<String> photos;
    public boolean isSaved;
    public LatLng latLng;
    public boolean isGuarantee;

    public BallabaPropertyResult(){}
    public BallabaPropertyResult(String id, String roomsNumber, String price, String size, String formattedAddress, ArrayList<String> photos, boolean isSaved, LatLng latLng, boolean isGuarantee) {
        this.id = id;
        this.roomsNumber = roomsNumber;
        this.price = price;
        this.size = size;
        this.formattedAddress = formattedAddress;
        this.photos = photos;
        this.isSaved = isSaved;
        this.latLng = latLng;
        this.isGuarantee = isGuarantee;
    }
}
