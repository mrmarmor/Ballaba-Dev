package com.example.michaelkibenko.ballaba.Entities;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class BallabaPhoneNumber extends BallabaBaseEntity{
    private String phoneNumber;
    private String countryCode;

    public BallabaPhoneNumber() {
        phoneNumber = "";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFullPhoneNumber(){
        return getCountryCode()+getPhoneNumber();
    }

}