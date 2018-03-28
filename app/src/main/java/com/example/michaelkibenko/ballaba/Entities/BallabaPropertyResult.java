package com.example.michaelkibenko.ballaba.Entities;


import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class BallabaPropertyResult extends BallabaBaseEntity {
    public String id;
    public String roomsNumber;
    public String price;
    public String size;
    public String formattedAddress;
    public String rentPeriod;
    public String numberOfPayments;
    public ArrayList<String> photos;
    public boolean isSaved;
    public boolean isGuaranteed;

    public BallabaPropertyResult(String id, String roomsNumber, String price, String size, String formattedAddress, String rentPeriod, String numberOfPayments, ArrayList<String> photos, boolean isSaved, boolean isGuaranteed) {
        this.id = id;
        this.roomsNumber = roomsNumber;
        this.price = price;
        this.size = size;
        this.formattedAddress = formattedAddress;
        this.rentPeriod = rentPeriod;
        this.numberOfPayments = numberOfPayments;
        this.photos = photos;
        this.isSaved = isSaved;
        this.isGuaranteed = isGuaranteed;
    }

}
