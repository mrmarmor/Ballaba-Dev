package com.example.michaelkibenko.ballaba.Entities;

import com.google.gson.Gson;

/**
 * Created by User on 06/03/2018.
 */

public class BallabaUser extends BallabaBaseEntity {
    private String id, phone, email, first_name, last_name, gender, tenant_score, landlord_score, guarantor_score, date_created, date_updated, session_token
            , device_id, global_token;

    public BallabaUser(){}

    //TODO MAKE THIS CLASS BECOME A SINGLETON

    public BallabaUser(String id, String phone, String email, String first_name, String last_name, String gender, String tenant_score, String landlord_score
            , String guarantor_score, String date_created, String date_updated, String session_token, String device_id, String global_token) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.tenant_score = tenant_score;
        this.landlord_score = landlord_score;
        this.guarantor_score = guarantor_score;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.session_token = session_token;
        this.device_id = device_id;
        this.global_token = global_token;
    }

    public String getGlobal_token() {
        return global_token;
    }

    public BallabaUser fromStringToBallabaUser(String userString){
        return new Gson().fromJson(userString, BallabaUser.class);
    }

    public String getSessionToken(){
        return this.session_token;
    }
}
