package com.example.michaelkibenko.ballaba.Entities;

public class FilterDimensions extends BallabaBaseEntity {
    private String max_price;
    private String min_price;
    private String max_size;
    private String min_size;
    private String max_rooms;
    private String min_rooms;

    public FilterDimensions(String max_price, String min_price, String max_size, String min_size, String max_rooms, String min_rooms) {
        this.max_price = max_price;
        this.min_price = min_price;
        this.max_size = max_size;
        this.min_size = min_size;
        this.max_rooms = max_rooms;
        this.min_rooms = min_rooms;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_size() {
        return max_size;
    }

    public void setMax_size(String max_size) {
        this.max_size = max_size;
    }

    public String getMin_size() {
        return min_size;
    }

    public void setMin_size(String min_size) {
        this.min_size = min_size;
    }

    public String getMax_rooms() {
        return max_rooms;
    }

    public void setMax_rooms(String max_rooms) {
        this.max_rooms = max_rooms;
    }

    public String getMin_rooms() {
        return min_rooms;
    }

    public void setMin_rooms(String min_rooms) {
        this.min_rooms = min_rooms;
    }
}
