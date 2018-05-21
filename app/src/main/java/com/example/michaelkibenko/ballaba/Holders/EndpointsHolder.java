package com.example.michaelkibenko.ballaba.Holders;

/**
 * Created by michaelkibenko on 19/02/2018.
 */


public @interface EndpointsHolder {

    String LOGIN = "https://api.ballaba-it.com/v1/login";
    String LOGIN_BY_TOKEN ="https://api.ballaba-it.com/v1/loginbytoken";
    String AUTHENTICATE = "https://api.ballaba-it.com/v1/authenticate";
    String CONFIG = "https://api.ballaba-it.com/v1/config";
    String TESTING = "https://api.ballaba-it.com/v1/";
    String GOOGLE_PLACES_API = "https://maps.googleapis.com/maps/api/place";
    String USER = "https://api.ballaba-it.com/v1/user/";
    String PROPERTY = "https://api.ballaba-it.com/v1/property/";
    String PROPERTY_ATTACHMENTS_ADDONS = "https://api.ballaba-it.com/v1/config/property";
    String GOOGLE_MAP_API = "http://maps.google.com/maps/api/staticmap?center=";
    String GOOGLE_MAP_API_SETTINGS = "&zoom=15&size=360x180&sensor=false&markers=color:0x00bfffff";

    //TODO if we want to detect if a latLng has a streetView image we can use this url:
    String GOOGLE_STREET_VIEW_API = "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=6.414382,10.013988&heading=151.78&pitch=-0.76&key=AIzaSyAQmnHnqeLXmnWrBIoF1TAdaEw7fVwdVDI";

    String VIEWPORT = "https://api.ballaba-it.com/v1/viewport/";

    //String SAVE_PROPERTY = "https://api.ballaba-it.com/v1/property/";

    String GET_SAVED = "https://api.ballaba-it.com/v1/property/saved";

    String SCORING_DATA = "https://api.ballaba-it.com/v1/user/me/scores";
    String SCORING_FIRST_IMAGE_ID = "https://api.ballaba-it.com/v1/user/me/passport";
    String SCORING_SECOND_IMAGE_ID = "https://api.ballaba-it.com/v1/user/me/selfie";

    String SCORING_LABELS = "https://api.ballaba-it.com/v1/config/scoring";
}
