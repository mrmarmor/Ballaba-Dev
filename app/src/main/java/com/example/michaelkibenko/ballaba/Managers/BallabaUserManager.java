package com.example.michaelkibenko.ballaba.Managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

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
            String city = jsonObject.getString("city");
            String address = jsonObject.getString("address");
            String aptNo = jsonObject.getString("apt_no");
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
            String profileImage = jsonObject.getString("profile_image");
            //Bitmap profileImage = StringUtils.getInstance(true, null)
            //        .stringToBitmap(jsonObject.getString("profile_image"));

            BallabaUser user = new BallabaUser(id, phone, email, name, lastName, city, address, aptNo, gender, tenantScore
                    , landlordScore , guarantorScore, dateCreated, dateUpdated, sessionToken, deviceId, globalToken, profileImage);

            return user;
        }catch (JSONException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    //TODO we can merge "generateUserFromJsonResponse(JSONObject jsonObject)" and
    //TODO  "generateUserFromJsonResponse(String response)" (above) into one function.
    //TODO it is still duplicated, because we are afraid of incompatible parsing of data among different devices or different versions of java.
    public BallabaUser generateUserFromJsonResponse(JSONObject jsonObject){
        try{
            String id = jsonObject.getString("id");
            String phone = jsonObject.getString("phone");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String city = jsonObject.getString("city");
            String address = jsonObject.getString("address");
            String aptNo = jsonObject.getString("apt_no");
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
            String profileImage = jsonObject.getString("profile_image");
            //Bitmap profileImage = StringUtils.getInstance(true, null)
            //        .stringToBitmap(jsonObject.getString("profile_image"));

            BallabaUser user = new BallabaUser(id, phone, email, name, lastName, city, address, aptNo, gender, tenantScore
                    , landlordScore , guarantorScore, dateCreated, dateUpdated, sessionToken, deviceId, globalToken, profileImage);

            return user;
        }catch (JSONException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public String getUserSesionToken(){
        return user.getSessionToken();
    }

    public BallabaUser getUser() {
        return user;
    }
}