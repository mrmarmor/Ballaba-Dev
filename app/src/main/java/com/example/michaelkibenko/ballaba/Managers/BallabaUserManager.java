package com.example.michaelkibenko.ballaba.Managers;

import android.util.Log;

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
        if (instance == null) {
            instance = new BallabaUserManager();
        }
        return instance;
    }

    public void setUser(BallabaUser user) {
        this.user = user;
    }

    private String id, phone, email, name, lastName, city, address, streetNumber, aptNo, idNumber, birthDate, about, tenantScore
            , landlordScore, guarantorScore, dateCreated, dateUpdated, sessionToken, fcmToken, globalToken, profileImage;
    private boolean isScored, islandlord, isCreditAvailbable;

    public BallabaUser generateUserFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            id = jsonObject.getString("id");
            phone = jsonObject.getString("phone");
            email = jsonObject.getString("email");
            name = jsonObject.getString("first_name");//stringUtils.formattedHebrew(jsonObject.getString("first_name"));
            lastName = jsonObject.getString("last_name");//stringUtils.formattedHebrew(jsonObject.getString("last_name"));
            city = jsonObject.getString("city");//stringUtils.formattedHebrew(jsonObject.getString("city"));
            address = jsonObject.getString("address");//stringUtils.formattedHebrew(jsonObject.getString("address"));

            //TODO Marik should add data field named "street_number". Then, i will fetch this field from response:
                //streetNumber = jsonObject.getString("street_number");
            /*TODO for now it will be: */ streetNumber = "DUMMY UNTIL SERVER WILL RECEIVE";

            aptNo = jsonObject.getString("apt_no");
            idNumber = jsonObject.getString("id_number");
            birthDate = jsonObject.getString("birth_date").replace("-", "/");
            about = jsonObject.getString("about");
            profileImage = jsonObject.getString("profile_image");

            //TODO if i need score i can get it here, but yet it is not received from server and throwing JSONException
            //tenantScore = jsonObject.getString("tenant_score");
            //landlordScore = jsonObject.getString("landlord_score");
            //guarantorScore = jsonObject.getString("guarantor_score");

            if (jsonObject.has("created_at") && jsonObject.has("updated_at")) {
                dateCreated = jsonObject.getString("created_at").replace("-", "/");
                dateUpdated = jsonObject.getString("updated_at").replace("-", "/");
            }

            if (jsonObject.has("scoring_status"))
                isScored = jsonObject.getBoolean("scoring_status");

            if (jsonObject.has("auth")) {
                JSONObject auth = jsonObject.getJSONObject("auth");
                sessionToken = auth.getString("session_token");
                globalToken = auth.getString("global_token");
                fcmToken = auth.getString("fcm_token");
            }

            if (jsonObject.has("is_landlord"))
                islandlord = jsonObject.getBoolean("is_landlord");

            if (jsonObject.has("is_credit_available"))
                isCreditAvailbable = jsonObject.getBoolean("is_credit_available");

            return new BallabaUser(id, phone, email, name, lastName, city, address, streetNumber, aptNo, idNumber
                    , birthDate, about, isScored, tenantScore, landlordScore, guarantorScore, dateCreated
                    , dateUpdated, sessionToken, fcmToken, globalToken, profileImage, islandlord, isCreditAvailbable);

        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage());
            return null;/*new BallabaUser(id, phone, email, name, lastName, city, address, aptNo
                    , birthDate, about, isScored, tenantScore, landlordScore, guarantorScore, dateCreated
                    , dateUpdated, sessionToken, fcmToken, globalToken, profileImage);*/
        }
    }

    public String getUserSesionToken() {
        if (user != null)
            return user.getSessionToken();
        else
            return null;
    }

    public BallabaUser getUser() {
        return user;
    }
}