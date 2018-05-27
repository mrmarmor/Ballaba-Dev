package com.example.michaelkibenko.ballaba.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.gson.Gson;

/**
 * Created by User on 06/03/2018.
 */

public class BallabaUser extends BallabaBaseEntity {
    private String id, phone, email, first_name, last_name, city, address, apt_no, birth_date, about
            , tenant_score, landlord_score, guarantor_score, date_created, date_updated, session_token
            , device_id, global_token, fcm_token, profile_image;

    private StringUtils heb = StringUtils.getInstance(true, null);
   /* private static StringUtils stringInstance(Context context){
        if (instance == null)
            instance = StringUtils.getInstance(true, context);

        return instance;
    }*/

    public BallabaUser(){}

    //TODO MAKE THIS CLASS BECOME A SINGLETON

    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String city, String address, String apt_no, String birth_date, String about, String tenant_score, String landlord_score, String guarantor_score, String date_created, String date_updated, String session_token, String fcm_token, String global_token, String profile_image) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.address = address;
        this.apt_no = apt_no;
        this.about = about;
        this.birth_date = birth_date;
        this.tenant_score = tenant_score;
        this.landlord_score = landlord_score;
        this.guarantor_score = guarantor_score;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.session_token = session_token;
        this.fcm_token = fcm_token;
        this.global_token = global_token;
        this.profile_image = profile_image;
    }

    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String city, String address, String apt_no, String birth_date, String about, String session_token, String fcm_token, String global_token, String profile_image) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.about = about;
        this.city = city;
        this.address = address;
        this.apt_no = apt_no;
        this.profile_image = profile_image;
        this.global_token = global_token;
        this.session_token = session_token;
        this.fcm_token = fcm_token;
    }

    private String trimNull(String s){
        if (s == null || s.equals("null"))
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
    }//TODO could be an email in hebrew?

    public String getFirst_name() {
        return (first_name.codePointAt(0) > 1487 && first_name.codePointAt(0) < 1515)? //==formatted hebrew
                trimNull(first_name) : heb.formattedHebrew(trimNull(first_name));
    }

    public String getLast_name() {
        return (last_name.codePointAt(0) > 1487 && last_name.codePointAt(0) < 1515)? //==formatted hebrew
                trimNull(last_name) : heb.formattedHebrew(trimNull(last_name));
    }

    public String getCity() {
        return (city.codePointAt(0) > 1487 && city.codePointAt(0) < 1515)? //==formatted hebrew
                trimNull(city) : heb.formattedHebrew(trimNull(city));
    }

    public String getAddress() {
        return (address.codePointAt(0) > 1487 && address.codePointAt(0) < 1515)? //==formatted hebrew
                trimNull(address) : heb.formattedHebrew(trimNull(address));
    }

    public String getApt_no() {
        return trimNull(apt_no);
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

    /*public String getFcm_token() {
        return trimNull(fcm_token);
    }*/

    public String getBirth_date() {
        return birth_date;
    }

    public String getAbout() {
        return (about.codePointAt(0) > 1487 && about.codePointAt(0) < 1515)? //==formatted hebrew
                trimNull(about) : heb.formattedHebrew(trimNull(about));
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public String getProfile_image() {
        return profile_image;
    }
}