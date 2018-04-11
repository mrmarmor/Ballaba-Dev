package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.PropertyDescriptionComment;
import com.example.michaelkibenko.ballaba.Fragments.Filter.AttachmentsFragment;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.michaelkibenko.ballaba.Activities.SplashActivity.FLOW_TYPES.AUTHENTICATED;
import static com.example.michaelkibenko.ballaba.Activities.SplashActivity.FLOW_TYPES.NEED_AUTHENTICATION;
import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.AFTER_20;
import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.FIRST_20;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class BallabaSearchPropertiesManager {
    @IntDef({FIRST_20, AFTER_20})
    public @interface LAZY_LOADING_OFFSET_STATES {
        int FIRST_20 = 0;
        int AFTER_20 = 1;
    }

    private static final String TAG = BallabaSearchPropertiesManager.class.getSimpleName();
    private static BallabaSearchPropertiesManager instance;
    private Context context;
    StringUtils heb = StringUtils.getInstance(true, context);
    private ArrayList<BallabaPropertyResult> results;

    public static BallabaSearchPropertiesManager getInstance(Context context) {
        if(instance == null){
            instance = new BallabaSearchPropertiesManager(context);
        }

        return instance;
    }

    private BallabaSearchPropertiesManager(Context context) {
        this.context = context;
        results = new ArrayList<>();
    }

    public ArrayList<BallabaPropertyResult> getResults() {
        return results;
    }
    public ArrayList<String> getResultsByLocation() {
        ArrayList<String> locations = new ArrayList<>();
        for (BallabaPropertyResult result : results)
            locations.add(result.formattedAddress);

        return locations;

    }

    public int getResultsCount(){
        return results.size();
    }

    public void appendProperties(ArrayList<BallabaPropertyResult> res, boolean isLazyLoading){
        if(isLazyLoading){
            results.addAll(res);
        }else {
            results = res;
        }
    }

    public void getRandomProperties(final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getRandomProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);

                    if(results != null){
                        appendProperties(results, true);
                    }else {
                        Log.e(TAG, "results is null, Json parse error");
                    }

                    callback.resolve(entity);
                }else {
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        });
    }

    public void getPropertiesByLatLng(final LatLng latLng, final int offset
            , @LAZY_LOADING_OFFSET_STATES int state, final BallabaResponseListener callback){

        final String PARAMS = lazyLoadingStatesChanger(state, latLng, offset);
        ConnectionsManager.getInstance(context).getPropertyByLatLng(PARAMS, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);

                    if(results != null){
                        appendProperties(results, true);
                    }else {
                        Log.e(TAG, "results is null, Json parse error");
                    }

                    callback.resolve(entity);
                }else {
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        }, offset);
    }

    public void getPropertiesByViewPort(LatLngBounds bounds, final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByViewPort(bounds, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);
                    BallabaSearchPropertiesManager.this.results = results;
                    callback.resolve(entity);
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                callback.reject(entity);
            }
        });
    }

    private String lazyLoadingStatesChanger(final @LAZY_LOADING_OFFSET_STATES int state
            , final LatLng latLng, final int offset){
        if (state == FIRST_20){
            return "?latlong=" + latLng.latitude + "," + latLng.longitude;
        } else if (state == AFTER_20){
            return "?offset=" + offset;
        }

        return null;
    }

    public BallabaPropertyResult getPropertyById(@Nullable ArrayList<BallabaPropertyResult> target, String id){
        if(target == null){
            target = getResults();
        }

        for (BallabaPropertyResult prop : target) {
            if(prop.id.equals(id)){
                return prop;
            }
        }

        return null;
    }

    public ArrayList<BallabaPropertyResult> parsePropertyResults(String result){
        try{
            ArrayList<BallabaPropertyResult> returned = new ArrayList<>();
            JSONObject preObject = new JSONObject(result);
            JSONArray object = preObject.getJSONArray("properties");

            for (int i = 0; i < object.length(); i++) {
                JSONObject res = object.getJSONObject(i);
                String id = res.getString("id");
                String rooms = res.getString("rooms");
                String price = res.getString("price");
                String size = res.getString("size");
                String formattedAddress = res.getString("formatted_address");
                String rentPeriod = res.getString("rent_period");
                String numberOfPayments = res.getString("no_of_payments");
                JSONArray photosJsonArray = res.getJSONArray("photos");
                ArrayList<String> photos = new ArrayList<>();
                if (photosJsonArray != null) {
                    int len = photosJsonArray.length();
                    for (int g=0;g<len;g++){
                        photos.add(photosJsonArray.getJSONObject(g).getString("photo_url"));
                    }
                }
                boolean isSaved = res.getBoolean("is_saved");

                LatLng latLng = new LatLng(Double.parseDouble(res.getString("lat")), Double.parseDouble(res.getString("lng")));

                boolean isGuarantee = res.getBoolean("is_guaranteed");

                //TODO change lat lng object

                BallabaPropertyResult propertyResult = new BallabaPropertyResult(id, rooms, price, size,
                        formattedAddress, rentPeriod, numberOfPayments, photos, isSaved, latLng, isGuarantee);

                returned.add(propertyResult);
            }

            Log.d(TAG, returned.get(0).id);
            return returned;

        }catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public BallabaPropertyFull parsePropertiesFull(String response){
        try{
            JSONObject res = new JSONObject(response);
            String id = res.getString("id");
            String city = res.getString("city");
            String street = res.getString("street");
            String street_number = res.getString("street_number");
            String entry = res.getString("entry");
            String floor = res.getString("floor");
            String max_floor = res.getString("max_floor");
            String size = res.getString("size");
            String no_of_parking = res.getString("no_of_parking");
            String parking_price = res.getString("parking_price");
            boolean furniture = res.getBoolean("furniture");
            boolean electronics = res.getBoolean("electronics");
            String description = res.getString("description");
            String payment_date = res.getString("payment_date");
            String rooms = res.getString("rooms");
            String bathrooms = res.getString("bathrooms");
            String toilets = res.getString("toilets");
            String entry_date = res.getString("entry_date");
            String price = res.getString("price");
            String rent_period = res.getString("rent_period");
            String no_of_payments = res.getString("no_of_payments");
            String status = res.getString("status");
            boolean is_saved = res.getBoolean("is_saved");
            boolean is_guaranteed = res.getBoolean("is_guaranteed");
            boolean priority = res.getBoolean("priority");
            String country = res.getString("country");
            String zip_code = res.getString("zip_code");
            String level_1_area = res.getString("level_1_area");
            String level_2_area = res.getString("level_2_area");
            String google_place_id = res.getString("google_place_id");
            String lat = res.getString("lat");
            String lng = res.getString("lng");
            String formattedAddress = res.getString("formatted_address");
            boolean show = res.getBoolean("show");
            String created_at = res.getString("created_at");
            String updated_at = res.getString("updated_at");

            //BallabaPropertyFull.Landlord[] landlords = new BallabaPropertyFull.Landlord[]{};
            HashMap<String, String> landlords = parseLandlords(res.getJSONArray("landlords"));
            //String[] attachments = res.get
            ArrayList<String> attachments = parseAttachments(res.getJSONArray("attachments"));
            HashMap<String, String> payments = parsePayments(res.getJSONArray("payments"));
            HashMap<String, String> paymentMethods = parsePaymentMethods(res.getJSONArray("payment_methods"));
            HashMap<String, String> addons = parseAddons(res.getJSONArray("addons"));
            HashMap<String, String> photos = parsePhotos(res.getJSONArray("photos"));
            HashMap<String, String> openDoorDates = parseOpenDoorDates(res.getJSONArray("open_door_dates"));
            ArrayList<PropertyDescriptionComment> comments = parseComments(res.getJSONArray("comments"));
            //parseComments(res.getJSONArray("comments"));

      /*      BallabaPropertyFull.Attachment attachments = res.getString("");
            String[] payments = res.getString("payments");
            String[] payment_methods = res.getString("payment_methods");
            String[] addons = res.getString("addons");
            boolean isSaved = res.getBoolean("is_saved");

            JSONArray photosJsonArray = res.getJSONArray("photos");
            HashMap<String, String> photos = new HashMap<>();


            String[] open_door_dates = res.getString("open_door_dates");
            String[] comments = res.getString("comments");*/

            BallabaPropertyFull property = new BallabaPropertyFull(id, rooms, price, size,
                    formattedAddress, rent_period, no_of_payments, lat, lng, city, street,
                    street_number, entry, floor, max_floor, no_of_parking, parking_price,
                    description, payment_date, bathrooms, toilets, entry_date, status, country,
                    zip_code, level_1_area, level_2_area, google_place_id, created_at, updated_at,
                    furniture, electronics, show, priority, is_saved, is_guaranteed, landlords,
                    attachments, payments, paymentMethods, addons, photos, openDoorDates, comments);

            Log.d(TAG, property.id);
            return property;

        }catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    private HashMap<String, String> parseLandlords(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> landlords = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            landlords.put("id", jsonObject.getString("id"));
            landlords.put("first_name", heb.formattedHebrew(jsonObject.getString("first_name")));
            landlords.put("last_name", heb.formattedHebrew(jsonObject.getString("last_name")));
            landlords.put("city", heb.formattedHebrew(jsonObject.getString("city")));
            landlords.put("profile_image", jsonObject.getString("profile_image"));
            //landlords[i] = new BallabaPropertyFull().new Landlord();//jsonArray.getJSONObject(i).getString("photo_url"));
        }

        return landlords;
    }

    private ArrayList<String> parseAttachments(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<String> attachments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            //JSONObject jsonObject = jsonArray.getJSONObject(i);
            attachments.add(jsonArray.get(i).toString());
            /*attachments.put("id", jsonObject.getString("id"));
            attachments.put("property_id", jsonObject.getString("property_id"));
            attachments.put("attachment_type", jsonObject.getString("attachment_type"));*/
        }

        return attachments;
    }

    private HashMap<String, String> parsePayments(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> payments = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            payments.put("id", jsonObject.getString("id"));
            payments.put("property_id", jsonObject.getString("property_id"));
            payments.put("payment_type", jsonObject.getString("payment_type"));
            payments.put("price", jsonObject.getString("price"));
            payments.put("is_included", jsonObject.getBoolean("is_included")+"");
            payments.put("currency", jsonObject.getString("currency"));
        }

        return payments;
    }

    private HashMap<String, String> parsePaymentMethods(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> paymentMethods = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            paymentMethods.put("id", jsonObject.getString("id"));
            paymentMethods.put("property_id", jsonObject.getString("property_id"));
            paymentMethods.put("payment_method", jsonObject.getString("payment_method"));
        }

        return paymentMethods;
    }

    private HashMap<String, String> parseAddons(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> addons = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            addons.put("id", jsonObject.getString("id"));
            addons.put("property_id", jsonObject.getString("property_id"));
            addons.put("addon_type", jsonObject.getString("addon_type"));
        }

        return addons;
    }

    private HashMap<String, String> parsePhotos(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> photos = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            photos.put("tags", jsonObject.getString("tags"));
            photos.put("photo_url", jsonObject.getString("photo_url"));
            photos.put("sort_order", jsonObject.getString("sort_order"));
        }

        return photos;
    }

    private HashMap<String, String> parseOpenDoorDates(@NonNull JSONArray jsonArray) throws JSONException{
        HashMap<String, String> openDoorDates = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            openDoorDates.put("start_time", jsonObject.getString("start_time"));
            openDoorDates.put("end_time", jsonObject.getString("end_time"));
        }

        return openDoorDates;
    }

    private ArrayList<PropertyDescriptionComment> parseComments(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<PropertyDescriptionComment> comments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String createdAt = jsonObject.getString("created_at");
            String display = jsonObject.getString("display");

            PropertyDescriptionComment.User user = parseUser(jsonObject.getJSONObject("user"));

            ArrayList<HashMap<String, String>> positive = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            JSONArray positiveArr = jsonObject.getJSONArray("positive");
            if (positiveArr != null){
                for (int j = 0; j < positiveArr.length(); j++){
                    Log.d(TAG, positiveArr.getJSONObject(j).getString("content"));
                    map.put("content", heb.formattedHebrew(positiveArr.getJSONObject(j).getString("content")));
                    positive.add(map);
                }
            }

            ArrayList<HashMap<String, String>> negative = new ArrayList<>();
            JSONArray negativeArr = jsonObject.getJSONArray("negative");
            if (negativeArr != null){
                for (int j = 0; j < negativeArr.length(); j++){
                    map.put("content", heb.formattedHebrew(negativeArr.getJSONObject(j).getString("content")));
                    negative.add(map);
                }
            }

            ArrayList<PropertyDescriptionComment.Reply> replies = new ArrayList<>();
            PropertyDescriptionComment.Reply reply = new PropertyDescriptionComment().new Reply();
            JSONArray replArr = jsonObject.getJSONArray("replies");
            if (replArr != null){
                for (int j = 0; j < replArr.length(); j++){
                    reply.setCreated_at(replArr.getJSONObject(j).getString("created_at"));
                    reply.setContent(heb.formattedHebrew(replArr.getJSONObject(j).getString("content")));
                    reply.setUser(parseUser(replArr.getJSONObject(j).getJSONObject("user")));
                    replies.add(reply);
                }
            }

            comments.add(new PropertyDescriptionComment(createdAt, display, positive, negative, user, replies));
        }

        return comments;
    }

    private PropertyDescriptionComment.User parseUser(JSONObject userJson) throws JSONException {
        //TODO encode hebrew to utf-8
        String userId = userJson.getString("id");
        String userProfileImage = userJson.getString("profile_image");

        String userFirstName = heb.formattedHebrew(userJson.getString("first_name"));
        String userLastName = heb.formattedHebrew(userJson.getString("last_name"));

        PropertyDescriptionComment.User user = new PropertyDescriptionComment().
                new User(userId, userFirstName, userLastName, userProfileImage);

        return user;
    }
}
