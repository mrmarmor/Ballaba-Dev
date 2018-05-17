package com.example.michaelkibenko.ballaba.Holders;

/**
 * Created by michaelkibenko on 19/02/2018.
 */


public @interface EndpointsHolder {

    String LOGIN = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/login";
    String LOGIN_BY_TOKEN ="https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/loginbytoken";
    String AUTHENTICATE = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/authenticate";
    String CONFIG = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/config";
    String TESTING = "https://z1qybfaf36.execute-api.eu-west-1.amazonaws.com/v1/";
    String GOOGLE_PLACES_API = "https://maps.googleapis.com/maps/api/place";
    String USER = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/user/";
    String PROPERTY = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/property/";
    String PROPERTY_ATTACHMENTS_ADDONS = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/config/property";
    String GOOGLE_MAP_API = "http://maps.google.com/maps/api/staticmap?center=";
    String GOOGLE_MAP_API_SETTINGS = "&zoom=15&size=360x180&sensor=false&markers=color:0x00bfffff";
    //TODO if we want to detect if a latLng has a streetView image we can use this url:
    String GOOGLE_STREET_VIEW_API = "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=6.414382,10.013988&heading=151.78&pitch=-0.76&key=AIzaSyAQmnHnqeLXmnWrBIoF1TAdaEw7fVwdVDI";
    String VIEWPORT = "https://api.ballaba-it.com/v1/viewport/";
    String SAVE_PROPERTY = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/property/";
    String GET_SAVED = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/property/saved";
    String SCORING_DATA = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/user/me/scores";
    String SCORING_FIRST_IMAGE_ID = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/user/me/passport";
    String SCORING_SECOND_IMAGE_ID = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/user/me/selfie";
    String SCORING_LABELS = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/v1/config/scoring";
    String UPLOAD_METTINGS_DATES = "https://jiw9g3hymb.execute-api.eu-west-1.amazonaws.com/Dev/property/";
}
