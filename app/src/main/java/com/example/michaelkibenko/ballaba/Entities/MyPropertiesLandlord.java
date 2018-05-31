package com.example.michaelkibenko.ballaba.Entities;

public class MyPropertiesLandlord {

    private String address;
    private int id , rooms , size;
    private String[] photos;


    public MyPropertiesLandlord(String address, int id, int rooms, int size, String[] photos) {
        this.address = address;
        this.id = id;
        this.rooms = rooms;
        this.size = size;
        this.photos = photos;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public int getRooms() {
        return rooms;
    }

    public int getSize() {
        return size;
    }

    public String[] getPhotos() {
        return photos;
    }

}
