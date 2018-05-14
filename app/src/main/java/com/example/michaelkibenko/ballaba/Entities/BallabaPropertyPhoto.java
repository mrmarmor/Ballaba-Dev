package com.example.michaelkibenko.ballaba.Entities;

import android.net.Uri;

import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 13/05/2018.
 */

public class BallabaPropertyPhoto extends BallabaBaseEntity implements Serializable{
    private int id;
    private boolean hasSent;
    private Uri photo;
    private ArrayList<PropertyAttachmentAddonEntity> tags = new ArrayList<>();

    public BallabaPropertyPhoto(int id){
        this.id = id;
    }
    public BallabaPropertyPhoto(Uri photo){
        this.photo = photo;
    }

    /*public BallabaPropertyPhoto(int id, Uri photo, ArrayList<String> tags) {
        this.id = id;
        this.photo = photo;
        this.tags = tags;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasSent() {
        return hasSent;
    }

    public void setHasSent(boolean hasSent) {
        this.hasSent = hasSent;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getTags() {
        return tags;
    }

    public void setTags(ArrayList<PropertyAttachmentAddonEntity> tags) {
        this.tags = tags;
    }

    public void addTag(PropertyAttachmentAddonEntity tag){
        this.tags.add(tag);
    }
    public void removeTag(PropertyAttachmentAddonEntity tag){
        this.tags.remove(tag);
    }

}
