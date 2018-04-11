package com.example.michaelkibenko.ballaba.Holders;

/**
 * Created by michaelkibenko on 19/02/2018.
 */


public @interface EndpointsHolder {
    String LOGIN = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/login";
    String LOGIN_BY_TOKEN ="https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/loginbytoken";
    String AUTHENTICATE = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/authenticate";
    String CONFIG = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/config";
    String TESTING = "https://z1qybfaf36.execute-api.eu-west-1.amazonaws.com/DEV/";
    String GOOGLE_PLACES_API = "https://maps.googleapis.com/maps/api/place";
    String PROPERTY = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/property";
    String PROPERTY_ATTACHMENTS_ADDONS = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/config/property";

    String GOOGLE_MAP_API = "http://maps.google.com/maps/api/staticmap?center=";
    String GOOGLE_MAP_API_SETTINGS = "&zoom=15&size=360x180&sensor=false";
}