package com.example.michaelkibenko.ballaba.Entities;

import java.util.ArrayList;
import java.util.Date;

public class FilterResultEntity extends BallabaBaseEntity {

    private int fromPrice;
    private int toPrice;
    private float fromRooms;
    private float toRooms;
    private int fromSize;
    private int toSize;
    private ArrayList<String> attachments_ids;
    private Boolean isElectronics = null, isFurnished = null;
    private Date enterDate;
    private boolean isFlexible = true;

    public FilterResultEntity() {
        this.attachments_ids = new ArrayList<>();
    }

    public int getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(int fromPrice) {
        this.fromPrice = fromPrice;
    }

    public int getToPrice() {
        return toPrice;
    }

    public void setToPrice(int toPrice) {
        this.toPrice = toPrice;
    }

    public float getFromRooms() {
        return fromRooms;
    }

    public void setFromRooms(float fromRooms) {
        this.fromRooms = fromRooms;
    }

    public float getToRooms() {
        return toRooms;
    }

    public void setToRooms(float toRooms) {
        this.toRooms = toRooms;
    }

    public int getFromSize() {
        return fromSize;
    }

    public void setFromSize(int fromSize) {
        this.fromSize = fromSize;
    }

    public int getToSize() {
        return toSize;
    }

    public void setToSize(int toSize) {
        this.toSize = toSize;
    }

    public ArrayList<String> getAttachments_ids() {
        return attachments_ids;
    }

    public void appendAttachmentId(String id) {
        if(!this.attachments_ids.contains(id)) {
            this.attachments_ids.add(id);
        }
    }

    public void deleteAttachmentId(String id) {
        if(this.attachments_ids.contains(id)) {
            this.attachments_ids.remove(id);
        }
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public boolean isFlexible() {
        return isFlexible;
    }

    public void setFlexible(boolean flexible) {
        isFlexible = flexible;
    }

    public Boolean isElectronics() {
        return isElectronics;
    }

    public void setElectronics(Boolean electronics) {
        isElectronics = electronics;
    }

    public Boolean isFurnished() {
        return isFurnished;
    }

    public void setFurnished(Boolean furnished) {
        isFurnished = furnished;
    }
}
