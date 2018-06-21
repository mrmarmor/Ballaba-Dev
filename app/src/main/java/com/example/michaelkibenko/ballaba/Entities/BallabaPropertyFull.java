package com.example.michaelkibenko.ballaba.Entities;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 13/03/2018.
 */

public class BallabaPropertyFull extends BallabaBaseEntity implements Serializable {
    private BallabaPropertyFull instance;
    private Context context;

    public String id, roomsNumber, price, size, formattedAddress, rentPeriod, numberOfPayments, lat, lng,
                  city, street, street_number, entry, floor, max_floor, no_of_parking, parking_price,
                  description, payment_date, bathrooms, toilets, entry_date, status, country,
                  zip_code, level_1_area, level_2_area, google_place_id, created_at, updated_at;
    public boolean furniture, electronics, show, priority, is_saved, isGuarantee;
    public ArrayList<HashMap<String, String>> landlords, payments, paymentMethods, addons, photos, openDoorDates;
    public ArrayList<String> attachments;
    public ArrayList<PropertyDescriptionComment> comments;
    /*public Landlord[] landlords;
    public Attachment[] attachments;
    public Payments[] payments;
    public PaymentMethods[] paymentMethods;
    public Addon[] addons;
    public Photo[] photos;
    public OpenDoorDate[] openDoorDates;
    public Comment[] comments;*/

    public BallabaPropertyFull getInstance(Context context) {
        if(instance == null){
            instance = new BallabaPropertyFull(context);
        }
        return instance;
    }

    public BallabaPropertyFull(){}
    public BallabaPropertyFull(String id){ this.id = id; }
    private BallabaPropertyFull(Context context){
        this.context = context;
    }

    public BallabaPropertyFull(String id, String roomsNumber, String price, String size, String formattedAddress, String rentPeriod,
                               String numberOfPayments, String lat, String lng, String city, String street, String street_number, String entry,
                               String floor, String max_floor, String no_of_parking, String parking_price, String description, String payment_date,
                               String bathrooms, String toilets, String entry_date, String status, String country, String zip_code, String level_1_area,
                               String level_2_area, String google_place_id, String created_at, String updated_at, boolean furniture, boolean electronics,
                               boolean show, boolean priority, boolean is_saved, boolean isGuarantee, ArrayList<HashMap<String, String>> landlords,
                               ArrayList<String> attachments, ArrayList<HashMap<String, String>> payments, ArrayList<HashMap<String, String>> paymentMethods,
                               ArrayList<HashMap<String, String>> addons, ArrayList<HashMap<String, String>> photos, ArrayList<HashMap<String, String>> openDoorDates,
                               ArrayList<PropertyDescriptionComment> comments) {
        this.id = id;
        this.roomsNumber = roomsNumber;
        this.price = price;
        this.size = size;
        this.formattedAddress = formattedAddress;
        this.rentPeriod = rentPeriod;
        this.numberOfPayments = numberOfPayments;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.street = street;
        this.street_number = street_number;
        this.entry = entry;
        this.floor = floor;
        this.max_floor = max_floor;
        this.no_of_parking = no_of_parking;
        this.parking_price = parking_price;
        this.description = description;
        this.payment_date = payment_date;
        this.bathrooms = bathrooms;
        this.toilets = toilets;
        this.entry_date = entry_date;
        this.status = status;
        this.country = country;
        this.zip_code = zip_code;
        this.level_1_area = level_1_area;
        this.level_2_area = level_2_area;
        this.google_place_id = google_place_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.furniture = furniture;
        this.electronics = electronics;
        this.show = show;
        this.priority = priority;
        this.is_saved = is_saved;
        this.isGuarantee = isGuarantee;
        this.landlords = landlords;
        this.attachments = attachments;
        this.payments = payments;
        this.paymentMethods = paymentMethods;
        this.addons = addons;
        this.photos = photos;
        this.openDoorDates = openDoorDates;
        this.comments = comments;
    }

    public class Landlord {
        private String id, first_name, last_name, city, profile_image;
        public Landlord(String id, String first_name, String last_name, String city, String profile_image) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.city = city;
            this.profile_image = profile_image;
        }
    }

    public class Attachment {
        private String id, property_id, attachment_type;
        public Attachment(String id, String property_id, String attachment_type) {
            this.id = id;
            this.property_id = property_id;
            this.attachment_type = attachment_type;
        }
    }

    class Payments {
        private String id, property_id, payment_type, price, currency;
        private boolean is_included;
        public Payments(String id, String property_id, String payment_type, String price, String currency, boolean is_included) {
            this.id = id;
            this.property_id = property_id;
            this.payment_type = payment_type;
            this.price = price;
            this.currency = currency;
            this.is_included = is_included;
        }
    }

    class PaymentMethods {
        private String id, property_id, payment_method;
        public PaymentMethods(String id, String property_id, String payment_method) {
            this.id = id;
            this.property_id = property_id;
            this.payment_method = payment_method;
        }
    }

    class Addon {
        private String id, property_id, addon_type, addon_photo;
        public Addon(String id, String property_id, String addon_type, String addon_photo) {
            this.id = id;
            this.property_id = property_id;
            this.addon_type = addon_type;
            this.addon_photo = addon_photo;
        }
    }

    class Photo {
        private String tags, photo_url, sort_order;
        public Photo(String tags, String photo_url, String sort_order) {
            this.tags = tags;
            this.photo_url = photo_url;
            this.sort_order = sort_order;
        }
    }

    class OpenDoorDate {
        private String start_time, end_time;
        public OpenDoorDate(String start_time, String end_time) {
            this.start_time = start_time;
            this.end_time = end_time;
        }
    }

    class Comment {
        private String created_at, display;
        private User user;
        private HashMap<String, String> positive, negative;
        private Reply reply;

        public Comment(String created_at, String display, User user, HashMap<String, String> positive, HashMap<String, String> negative, Reply reply) {
            this.created_at = created_at;
            this.display = display;
            this.user = user;
            this.positive = positive;
            this.negative = negative;
            this.reply = reply;
        }

        class User {
            private String id, first_name, last_name;
            public User(String id, String first_name, String last_name) {
                this.id = id;
                this.first_name = first_name;
                this.last_name = last_name;
            }
        }

        class Reply {
            private String created_at, content;
            private User user;
            public Reply(String created_at, String content, User user) {
                this.created_at = created_at;
                this.content = content;
                this.user = user;
            }
        }
    }
}
