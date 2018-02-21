package com.example.michaelkibenko.ballaba.Entities;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class PhoneNumber {
    private String phoneNumber;
    private String countryCode;

    public PhoneNumber(String phoneNumber, String countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
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
}
