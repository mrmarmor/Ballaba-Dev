package com.example.michaelkibenko.ballaba.Entities;

import android.net.Uri;

import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by User on 13/05/2018.
 */

public class BallabaPropertyPhoto extends BallabaBaseEntity{
    private int id;
    private Uri photo;
    private ArrayList<String> tags;

    public BallabaPropertyPhoto(int id, Uri photo, ArrayList<String> tags) {
        this.id = id;
        this.photo = photo;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

}
