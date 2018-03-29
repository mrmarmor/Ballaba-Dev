package com.example.michaelkibenko.ballaba.Managers;

import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 07/03/2018.
 */

public class BallabaUserManager {
    private static final String TAG = BallabaUserManager.class.getSimpleName();
    private static BallabaUserManager instance;
    private BallabaUser user;

    private BallabaUserManager() {
    }

    public static BallabaUserManager getInstance() {
        if(instance == null) {
            instance = new BallabaUserManager();
        }
        return instance;
    }

    public void setUser(BallabaUser user){
        this.user = user;
    }

    public BallabaUser generateUserFromJsonResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            String id = jsonObject.getString("id");
            String phone = jsonObject.getString("phone");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String gender = jsonObject.getString("gender");
            String tenantScore = jsonObject.getString("tenant_score");
            String landlordScore = jsonObject.getString("landlord_score");
            String guarantorScore = jsonObject.getString("guarantor_score");
            String dateCreated = jsonObject.getString("date_created");
            String dateUpdated = jsonObject.getString("date_updated");
            JSONObject auth = jsonObject.getJSONObject("auth");
            String globalToken = auth.getString("global_token");
            String sessionToken = auth.getString("session_token");
            String deviceId = auth.getString("device_id");
            BallabaUser user = new BallabaUser(id, phone, email, name, lastName, gender, tenantScore,landlordScore ,guarantorScore, dateCreated, dateUpdated,
                    sessionToken, deviceId, globalToken);

            return user;
        }catch (JSONException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public BallabaUser generateUserFromJsonResponse(JSONObject jsonObject){
        try{
            String id = jsonObject.getString("id");
            String phone = jsonObject.getString("phone");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String gender = jsonObject.getString("gender");
            String tenantScore = jsonObject.getString("tenant_score");
            String landlordScore = jsonObject.getString("landlord_score");
            String guarantorScore = jsonObject.getString("guarantor_score");
            String dateCreated = jsonObject.getString("date_created");
            String dateUpdated = jsonObject.getString("date_updated");
            JSONObject auth = jsonObject.getJSONObject("auth");
            String globalToken = auth.getString("global_token");
            String sessionToken = auth.getString("session_token");
            String deviceId = auth.getString("device_id");
            BallabaUser user = new BallabaUser(id, phone, email, name, lastName, gender, tenantScore,landlordScore ,guarantorScore, dateCreated, dateUpdated,
                    sessionToken, deviceId, globalToken);

            return user;
        }catch (JSONException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public String getUserSesionToken(){
        return user.getSessionToken();
    }
}
