package com.example.michaelkibenko.ballaba.Entities;

import android.content.Context;

import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 06/03/2018.
 */

public class BallabaUser extends BallabaBaseEntity {
    private String id, phone, email, first_name, last_name, city, address, apt_no, birth_date, about, tenant_score, landlord_score, guarantor_score, date_created, date_updated, session_token, device_id, global_token, fcm_token, profile_image, meeting_time;

    private boolean isInterested, isMeeting , isScored;

    private static StringUtils instance;

    private static StringUtils stringInstance(Context context) {
        if (instance == null)
            instance = StringUtils.getInstance(true);

        return instance;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public BallabaUser() {
    }

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

    public BallabaUser(String id1, String phone1, String s, String name, String lastName, String id, String phone, String email, String first_name, String last_name, boolean isScored, String city, String address, String apt_no, String birth_date, String about, String session_token, String fcm_token, String global_token, String profile_image) {
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

    private String trimNull(String s) {
        if (s == null || s.equals("null"))
            return "";
        else
            return s;
    }

    public String getGlobal_token() {
        return trimNull(global_token);
    }

    public BallabaUser fromStringToBallabaUser(String userString) {
        return new Gson().fromJson(userString, BallabaUser.class);
    }

    public String getSessionToken() {
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
        return stringInstance(null).formattedHebrew(trimNull(first_name));
    }

    public String getLast_name() {
        return stringInstance(null).formattedHebrew(trimNull(last_name));
    }

    public String getCity() {
        return stringInstance(null).formattedHebrew(trimNull(city));
    }

    public String getAddress() {
        return stringInstance(null).formattedHebrew(trimNull(address));
    }

    public String getApt_no() {
        return trimNull(apt_no);
    }

    public boolean getIs_scored() { return isScored; }

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
        return trimNull(about);
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setIsInterested(boolean interested) {
        isInterested = interested;
    }

    public boolean isInterested() {
        return isInterested;
    }

    public void setIsMeeting(boolean meeting) {
        isMeeting = meeting;
    }

    public boolean isMeeting() {
        return isMeeting;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        // "2014-10-01T00:00:00.000Z"
        String newDate = null;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = parser.parse(meeting_time);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            newDate = StringUtils.getInstance(true).getFormattedDateString(instance, true);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.meeting_time = newDate;
    }

    public void setScored(boolean scored) {
        isScored = scored;
    }
}