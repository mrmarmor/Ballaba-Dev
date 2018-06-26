package com.example.michaelkibenko.ballaba.Entities;

import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by User on 06/03/2018.
 */

public class BallabaUser extends BallabaBaseEntity {

    public String userCurrentPropertyObservedID; // testing

    private String id, phone, email, first_name, last_name, city, address, street_number, apt_no, id_number, birth_date, about, profession, marital_status, no_of_kids, tenant_score, landlord_score, guarantor_score, date_created, date_updated, session_token, device_id, global_token, fcm_token, profile_image, meeting_time, last_4_digits;

    private boolean isInterested, isMeeting , isScored, isLandlord, isCreditAvailable;

    private HashMap<String, String> social;

    public BallabaUser() {}

    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String city, String address
                     , String street_number, String apt_no, String id_number, String birth_date, String about, String profession
                     , String marital_status, String no_of_kids, boolean isScored, String tenant_score, String landlord_score
                     , String guarantor_score, String date_created, String date_updated, String session_token, String fcm_token
                     , String global_token, String profile_image, boolean isLandlord, boolean isCreditAvailable, String last4Digits
                     , HashMap<String, String> social) {

        this.id = id;
        this.phone = phone;
        this.profession = profession;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.address = address;
        this.apt_no = apt_no;
        this.street_number = street_number;
        this.about = about;
        this.profession = profession;
        this.marital_status = marital_status;
        this.no_of_kids = no_of_kids;
        this.isScored = isScored;
        this.id_number = id_number;
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
        this.isLandlord = isLandlord;
        this.isCreditAvailable = isCreditAvailable;
        this.last_4_digits = last4Digits;
        this.social = social;
    }

//    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String city, String address, String apt_no, String birth_date, String about,
//                       String session_token, String fcm_token, String global_token, String profile_image) {
//        this.id = id;
//        this.phone = phone;
//        this.email = email;
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.city = city;
//        this.address = address;
//        this.apt_no = apt_no;
//        this.birth_date = birth_date;
//        this.about = about;
//        this.session_token = session_token;
//        this.fcm_token = fcm_token;
//        this.global_token = global_token;
//        this.profile_image = profile_image;
//    }

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

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSessionToken() {
        return trimNull(this.session_token);
    }

    public String getId() {
        return trimNull(id);
    }

    public String getPhone() {
        if (phone != null && phone.endsWith("+"))
            return trimNull("+" + phone.replace("+", ""));
        else
            return trimNull(phone);
    }

    public String getEmail() {
        return trimNull(email);
    }//TODO could be an email in hebrew?

    public String getFirst_name() {
        return StringUtils.getInstance(true).formattedHebrew(trimNull(first_name));
    }

    public String getLast_name() {
        return StringUtils.getInstance(true).formattedHebrew(trimNull(last_name));
    }

    public String getCity() {
        return StringUtils.getInstance(true).formattedHebrew(trimNull(city));
    }

    public String getAddress() {
        return StringUtils.getInstance(true).formattedHebrew(trimNull(address));
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
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
        return StringUtils.getInstance(true).formattedHebrew(trimNull(about));
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

    public String getId_number() {
        return trimNull(id_number);
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getLast_4_digits() {
        return last_4_digits;
    }

    public void setLast_4_digits(String last_4_digits) {
        this.last_4_digits = last_4_digits;
    }

    public String getProfession() {
        return StringUtils.getInstance(true).formattedHebrew(trimNull(profession));
    }

    public String getMarital_status() {
        return marital_status;
    }

    public String getNo_of_kids() {
        return no_of_kids;
    }

    public HashMap<String, String> getSocial() {
        return social;

    }
}