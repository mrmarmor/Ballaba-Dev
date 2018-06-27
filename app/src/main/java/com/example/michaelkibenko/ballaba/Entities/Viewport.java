package com.example.michaelkibenko.ballaba.Entities;

import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by User on 14/05/2018.
 */

public class Viewport extends BallabaBaseEntity{
    public String id, title;
    public LatLngBounds bounds;
    public byte[] mapImage;
    public int zoom;

    public Viewport(String id, String title, LatLngBounds bounds, byte[] mapImage, int zoom) {
        this.id = id;
        this.title = title;
        this.bounds = bounds;
        this.mapImage = mapImage;
        this.zoom = zoom;
    }
}
