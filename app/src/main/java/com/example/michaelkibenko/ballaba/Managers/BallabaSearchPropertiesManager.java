package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.FilterDimensions;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyDescriptionComment;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    private StringUtils heb = StringUtils.getInstance(true);
    private ArrayList<BallabaPropertyResult> results;
    private FilterDimensions filterDimensions;
    private String currentSearchEndpoint;
    private BallabaPropertyFull currentPropertyFull;
    private ArrayList<BallabaPropertyFull> propertiesFull;

    public static BallabaSearchPropertiesManager getInstance(Context context) {
        if(instance == null){
            instance = new BallabaSearchPropertiesManager(context);
        }

        return instance;
    }

    private BallabaSearchPropertiesManager(Context context) {
        this.context = context;
        results = new ArrayList<>();
        propertiesFull = new ArrayList<>();
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

    public BallabaPropertyFull getPropertyFull() {
        return currentPropertyFull;
    }
    public void setPropertyFull(BallabaPropertyFull propertyFull) {
        this.currentPropertyFull = propertyFull;
    }

    public ArrayList<BallabaPropertyFull> getPropertiesFull() {
        return propertiesFull;
    }
    public void setPropertiesFull(ArrayList<BallabaPropertyFull> propertiesFull) {
        this.propertiesFull = propertiesFull;
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

    public void getPropertiesByLatLng(final LatLng latLng, final BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByLatLng(latLng,new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    ArrayList<BallabaPropertyResult> results = parsePropertyResults(((BallabaOkResponse) entity).body);

                    if(results != null){
                        appendProperties(results, false);
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

    public void getPropertiesByAddressAndFilter(ArrayList<String> cities, FilterResultEntity filterResult, BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).getPropertyByAddress(cities, filterResult, callback);
    }

    public void getLazyLoadingResults(final boolean isRefresh, BallabaResponseListener callback){
        ConnectionsManager.getInstance(context).lazyLoading(isRefresh, callback);
    }

    public String getCurrentSearchEndpoint() {
        return currentSearchEndpoint;
    }

    public void setCurrentSearchEndpoint(String currentSearchEndpoint) {
        this.currentSearchEndpoint = currentSearchEndpoint;
    }

    public BallabaPropertyFull getPropertyById(String id){
        for (BallabaPropertyFull prop : propertiesFull) {
            if(prop.id.equals(id)){
                return prop;
            }
        }

        return null;
    }

    public FilterDimensions parseFilterDimens(String result){
        try{
            JSONObject jsonObject = new JSONObject(result);
            String max_price = jsonObject.getString("max_price");
            String min_price = jsonObject.getString("min_price");
            String max_size = jsonObject.getString("max_size");
            String min_size = jsonObject.getString("min_size");
            String max_rooms = jsonObject.getString("max_rooms");
            String min_rooms = jsonObject.getString("min_rooms");

            FilterDimensions filterDimensions = new FilterDimensions(max_price, min_price, max_size, min_size, max_rooms, min_rooms);
            this.filterDimensions = filterDimensions;
            return filterDimensions;
        }catch (JSONException ex){
            ex.printStackTrace();
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
                ArrayList<String> photos  = new ArrayList<>();
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
            return returned;

        } catch (JSONException | NullPointerException ex){
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public BallabaPropertyFull parsePropertiesFull(String response){
        try{
            JSONObject res = new JSONObject(response);
            String id = heb.trimNull(res.getString("id"));
            String city = heb.trimNull(heb.formattedHebrew(res.getString("city")));
            String street = heb.trimNull(heb.formattedHebrew(res.getString("street")));
            String street_number = heb.trimNull(res.getString("street_number"));
            String entry = heb.trimNull(res.getString("entry"));
            String floor = heb.trimNull(res.getString("floor"));
            String max_floor = heb.trimNull(res.getString("max_floor"));
            String size = heb.trimNull(res.getString("size"));
            String no_of_parking = heb.trimNull(res.getString("no_of_parking"));
            String parking_price = heb.trimNull(res.getString("parking_price"));
            boolean furniture = res.getBoolean("furniture");
            boolean electronics = res.getBoolean("electronics");
            String description = heb.trimNull(heb.formattedHebrew(res.getString("description")));
            String payment_date = heb.trimNull(res.getString("payment_date"));
            String rooms = heb.trimNull(res.getString("rooms"));
            String bathrooms = heb.trimNull(res.getString("bathrooms"));
            String toilets = heb.trimNull(res.getString("toilets"));
            String entry_date = heb.trimNull(res.getString("entry_date"));
            String price = heb.trimNull(res.getString("price"));
            String rent_period = heb.trimNull(res.getString("rent_period"));
            String no_of_payments = heb.trimNull(res.getString("no_of_payments"));
            String status = heb.trimNull(res.getString("status"));
            boolean is_saved = res.getBoolean("is_saved");
            boolean is_guaranteed = res.getBoolean("is_guaranteed");
            boolean priority = res.getBoolean("priority");
            String country = heb.trimNull(heb.formattedHebrew(res.getString("country")));
            String zip_code = heb.trimNull(res.getString("zip_code"));//TODO
            String level_1_area = heb.trimNull(res.getString("level_1_area"));
            String level_2_area = heb.trimNull(res.getString("level_2_area"));
            String google_place_id = heb.trimNull(res.getString("google_place_id"));
            String lat = heb.trimNull(res.getString("lat"));
            String lng = heb.trimNull(res.getString("lng"));
            String formattedAddress = heb.trimNull(heb.formattedHebrew(res.getString("formatted_address")));
            boolean show = res.getBoolean("show");
            String created_at = heb.trimNull(res.getString("created_at"));
            String updated_at = heb.trimNull(res.getString("updated_at"));

            //BallabaPropertyFull.Landlord[] landlords = new BallabaPropertyFull.Landlord[]{};
            ArrayList<HashMap<String, String>> landlords = parseLandlords(res.getJSONArray("landlords"));
            ArrayList<String> attachments = parseAttachments(res.getJSONArray("attachments"));
            ArrayList<HashMap<String, String>> payments = parsePayments(res.getJSONArray("payments"));
            ArrayList<HashMap<String, String>> paymentMethods = parsePaymentMethods(res.getJSONArray("payment_methods"));
            ArrayList<HashMap<String, String>> addons = parseAddons(res.getJSONArray("addons"));
            ArrayList<HashMap<String, String>> photos = parsePhotos(res.getJSONArray("photos"));
            ArrayList<HashMap<String, String>> openDoorDates = parseOpenDoorDates(res.getJSONArray("open_door_dates"));
            ArrayList<PropertyDescriptionComment> comments = parseComments(res.getJSONArray("comments"));
            //TODO for testing only:
//            comments.add(parseComments(res.getJSONArray("comments")).get(0));
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

    private ArrayList<HashMap<String, String>> parseLandlords(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> landlords = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", heb.trimNull(jsonObject.getString("id")));
            map.put("first_name", heb.trimNull(heb.formattedHebrew(jsonObject.getString("first_name"))));
            map.put("last_name", heb.trimNull(heb.formattedHebrew(jsonObject.getString("last_name"))));
            map.put("city", heb.trimNull(heb.formattedHebrew(jsonObject.getString("city"))));
            map.put("profile_image", heb.trimNull(jsonObject.getString("profile_image")));
            landlords.add(map);
            //landlords[i] = new BallabaPropertyFull().new Landlord();//jsonArray.getJSONObject(i).getString("photo_url"));
        }

        return landlords;
    }

    private ArrayList<String> parseAttachments(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<String> attachments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            attachments.add(heb.trimNull(jsonArray.get(i).toString()));
        }

        return attachments;
    }

    private ArrayList<HashMap<String, String>> parsePayments(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> payments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", heb.trimNull(jsonObject.getString("id")));
            map.put("property_id", heb.trimNull(jsonObject.getString("property_id")));
            map.put("payment_type", heb.trimNull(jsonObject.getString("payment_type")));
            map.put("price", heb.trimNull(heb.formattedNumberWithComma(jsonObject.getString("price"))));
            map.put("is_included", jsonObject.getBoolean("is_included")+"");
            map.put("currency", heb.trimNull(jsonObject.getString("currency")));
            payments.add(map);
        }

        return payments;
    }

    private ArrayList<HashMap<String, String>> parsePaymentMethods(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> paymentMethods = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", heb.trimNull(jsonObject.getString("id")));
            map.put("property_id", heb.trimNull(jsonObject.getString("property_id")));
            map.put("payment_method", heb.trimNull(jsonObject.getString("payment_method")));
            paymentMethods.add(map);
        }

        return paymentMethods;
    }

    private ArrayList<HashMap<String, String>> parseAddons(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> addons = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", heb.trimNull(jsonObject.getString("id")));
            map.put("property_id", heb.trimNull(jsonObject.getString("property_id")));
            map.put("addon_type", heb.trimNull(jsonObject.getString("addon_type")));
            addons.add(map);
        }

        return addons;
    }

    private ArrayList<HashMap<String, String>> parsePhotos(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> photos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("tags", heb.trimNull(jsonObject.getString("tags")));
            map.put("photo_url", heb.trimNull(jsonObject.getString("photo_url")));
            map.put("sort_order", heb.trimNull(jsonObject.getString("sort_order")));
            photos.add(map);
        }

        return photos;
    }

    private ArrayList<HashMap<String, String>> parseOpenDoorDates(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<HashMap<String, String>> openDoorDates = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("is_repeat", heb.trimNull(jsonObject.getString("repeat")));
            map.put("id", heb.trimNull(jsonObject.getString("id")));
            map.put("start_time", heb.trimNull(jsonObject.getString("start_time")));
            map.put("end_time", heb.trimNull(jsonObject.getString("end_time")));
            openDoorDates.add(map);
        }

        return openDoorDates;
    }

    private ArrayList<PropertyDescriptionComment> parseComments(@NonNull JSONArray jsonArray) throws JSONException{
        ArrayList<PropertyDescriptionComment> comments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String createdAt = heb.trimNull(jsonObject.getString("created_at"));
            String display = heb.trimNull(jsonObject.getString("display"));

            PropertyDescriptionComment.User user = parseUser(jsonObject.getJSONObject("user"));

            ArrayList<HashMap<String, String>> positive = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            JSONArray positiveArr = jsonObject.getJSONArray("positive");
            if (positiveArr != null){
                for (int j = 0; j < positiveArr.length(); j++){
                    Log.d(TAG, positiveArr.getJSONObject(j).getString("content"));
                    map.put("content", heb.trimNull(heb.formattedHebrew(positiveArr.getJSONObject(j).getString("content"))));
                    positive.add(map);
                }
            }

            map = new HashMap<>();
            ArrayList<HashMap<String, String>> negative = new ArrayList<>();
            JSONArray negativeArr = jsonObject.getJSONArray("negative");
            if (negativeArr != null){
                for (int j = 0; j < negativeArr.length(); j++){
                    map.put("content", heb.trimNull(heb.formattedHebrew(negativeArr.getJSONObject(j).getString("content"))));
                    negative.add(map);
                }
            }

            ArrayList<PropertyDescriptionComment.Reply> replies = new ArrayList<>();
            PropertyDescriptionComment.Reply reply = new PropertyDescriptionComment().new Reply();
            JSONArray replArr = jsonObject.getJSONArray("replies");
            if (replArr != null){
                for (int j = 0; j < replArr.length(); j++){
                    reply.setCreated_at(heb.trimNull(replArr.getJSONObject(j).getString("created_at")));
                    reply.setContent(heb.trimNull(heb.formattedHebrew(replArr.getJSONObject(j).getString("content"))));
                    reply.setUser(parseUser(replArr.getJSONObject(j).getJSONObject("user")));
                    replies.add(reply);
                }
            }

            comments.add(new PropertyDescriptionComment(createdAt, display, positive, negative, user, replies));
        }

        return comments;
    }

    private PropertyDescriptionComment.User parseUser(JSONObject userJson) throws JSONException {
        String userId = heb.trimNull(userJson.getString("id"));
        String userProfileImage = heb.trimNull(userJson.getString("profile_image"));

        String userFirstName = heb.trimNull(heb.formattedHebrew(userJson.getString("first_name")));
        String userLastName  = heb.trimNull(heb.formattedHebrew(userJson.getString("last_name")));

        PropertyDescriptionComment.User user = new PropertyDescriptionComment().
                new User(userId, userFirstName, userLastName, userProfileImage);

        return user;
    }
}
