package com.example.michaelkibenko.ballaba.Managers;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class OCRHelper {
    private static OCRHelper instance;
    private JSONObject selfiePhoto;

    public static OCRHelper getInstance() {
        if(instance == null){
            instance = new OCRHelper();
        }
        return instance;
    }

    public void setSelfiePhoto(JSONObject selfiePhoto) {
        this.selfiePhoto = selfiePhoto;
    }

    public JSONObject getSelfiePhoto() {
        return selfiePhoto;
    }
}
