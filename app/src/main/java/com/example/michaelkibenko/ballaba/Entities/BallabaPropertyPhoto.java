package com.example.michaelkibenko.ballaba.Entities;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by User on 13/05/2018.
 */

public class BallabaPropertyPhoto extends BallabaBaseEntity implements Serializable{

    private int id = -1;
    private boolean hasSent;
    private Uri photo;
    private HashSet<PropertyAttachmentAddonEntity> tags = new HashSet<>();
    private byte[] bytes;

    /*public BallabaPropertyPhoto(int id, boolean hasSent, Uri photo, HashSet<PropertyAttachmentAddonEntity> tags){
        this.id = id;
        this.hasSent = hasSent;
        this.photo = photo;
        this.tags = tags;
    }*/
    public BallabaPropertyPhoto(int id){
        this.id = id;
    }
    public BallabaPropertyPhoto(Uri photo){
        this.photo = photo;
    }

    public BallabaPropertyPhoto(byte[] bytes){
        this.bytes = bytes;
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

    public HashSet<PropertyAttachmentAddonEntity> getTags() {
        return tags;
    }

    public void setTags(HashSet<PropertyAttachmentAddonEntity> tags) {
        this.tags = tags;
    }

    public void addTag(PropertyAttachmentAddonEntity tag){
        this.tags.add(tag);
    }
    public void removeTag(PropertyAttachmentAddonEntity tag){
        this.tags.remove(tag);
    }

    public byte[] getBytes() {
        return bytes;
    }
}
