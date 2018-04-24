package com.example.michaelkibenko.ballaba.Entities;

import android.graphics.Bitmap;

import com.google.gson.Gson;

/**
 * Created by User on 06/03/2018.
 */

public class BallabaUser extends BallabaBaseEntity {
    private String id, phone, email, first_name, last_name, city, address, apt_no, gender, tenant_score, landlord_score, guarantor_score, date_created, date_updated, session_token
            , device_id, global_token;
    private Bitmap profile_image;

    public BallabaUser(){}

    //TODO MAKE THIS CLASS BECOME A SINGLETON

    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String city, String address, String apt_no, String gender, String tenant_score, String landlord_score, String guarantor_score, String date_created, String date_updated, String session_token, String device_id, String global_token, Bitmap profile_image) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.address = address;
        this.apt_no = apt_no;
        this.gender = gender;
        this.tenant_score = tenant_score;
        this.landlord_score = landlord_score;
        this.guarantor_score = guarantor_score;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.session_token = session_token;
        this.device_id = device_id;
        this.global_token = global_token;
        this.profile_image = profile_image;
    }

    private String trimNull(String s){
        if (s.equals("null"))
            return "";
        else
            return s;
    }

    public String getGlobal_token() {
        return trimNull(global_token);
    }

    public BallabaUser fromStringToBallabaUser(String userString){
        return new Gson().fromJson(userString, BallabaUser.class);
    }

    public String getSessionToken(){
        return trimNull(this.session_token);
    }

    public String getId() {
        return trimNull(id);
    }

    public String getPhone() {
        return trimNull(phone);
    }

    public String getEmail() {
        return trimNull(email);
    }

    public String getFirst_name() {
        return trimNull(first_name);
    }

    public String getLast_name() {
        return trimNull(last_name);
    }

    public String getCity() {
        return trimNull(city);
    }

    public String getAddress() {
        return trimNull(address);
    }

    public String getApt_no() {
        return trimNull(apt_no);
    }

    public String getGender() {
        return trimNull(gender);
    }

    public String getTenant_score() {
        return trimNull(tenant_score);
    }

    public String getLandlord_score() {
        return trimNull(landlord_score);
    }

    public String getGuarantor_score() {
        return trimNull(guarantor_score);
    }

    public String getDate_created() {
        return trimNull(date_created);
    }

    public String getDate_updated() {
        return trimNull(date_updated);
    }

    public String getDevice_id() {
        return trimNull(device_id);
    }

    public Bitmap getProfile_image() {
        return profile_image;
    }
}