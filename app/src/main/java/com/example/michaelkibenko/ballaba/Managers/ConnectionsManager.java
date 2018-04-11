package com.example.michaelkibenko.ballaba.Managers;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.UserManager;
import android.text.LoginFilter;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.michaelkibenko.ballaba.BallabaApplication;
import com.example.michaelkibenko.ballaba.Config;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class ConnectionsManager{
    private static final String TAG = ConnectionsManager.class.getSimpleName();
    
    private static ConnectionsManager instance;
    private Context context;
    private RequestQueue queue;


    public static ConnectionsManager getInstance(Context context) {
        if(instance == null){
            instance = new ConnectionsManager(context);
        }
        return instance;
    }

    private ConnectionsManager(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(this.context);
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public <T> void addRequestToQueue(Request<T> request) {
        getQueue().add(request);
    }

    public void loginWithPhoneNumber(final String phoneNumber, final BallabaResponseListener callback){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phone", phoneNumber);


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EndpointsHolder.LOGIN, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.resolve(new BallabaOkResponse());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                        } else {
                            callback.reject(new BallabaErrorResponse(500, null));
                        }
                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonObjectRequest);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
    }

    public void enterCode(String phoneNUmber, final String code, final BallabaResponseListener callback){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phone", phoneNUmber);
                jsonObject.put("code", code);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EndpointsHolder.AUTHENTICATE, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BallabaUser user = BallabaUserManager.getInstance().generateUserFromJsonResponse(response);
                        if (user == null) {
                            callback.reject(new BallabaErrorResponse(500, null));
                        } else {
                            callback.resolve(user);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                        } else {
                            callback.reject(new BallabaErrorResponse(500, null));
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return null;
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonObjectRequest);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
    }

    public void getConfigRequest(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(GET, EndpointsHolder.CONFIG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        callback.resolve(new BallabaOkResponse());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("platform", Config.PLATFORM_NAME);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void logInByToken(final BallabaResponseListener callback, final String token){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndpointsHolder.LOGIN_BY_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BallabaUser user = BallabaUserManager.getInstance().generateUserFromJsonResponse(response);
                        if(user == null){
                            callback.reject(new BallabaErrorResponse(500, null));
                        }else {
                            callback.resolve(user);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("global_token", token);
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void getRandomProperties(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(GET, EndpointsHolder.PROPERTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BallabaOkResponse okResponse = new BallabaOkResponse();
                        okResponse.setBody(response);
                        callback.resolve(okResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                params.put("session_token", BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    public void getPropertyByLatLng(final String PARAMS, final BallabaResponseListener callback, int offset){
        StringRequest stringRequest = new StringRequest(GET
                , EndpointsHolder.PROPERTY + PARAMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    Log.e(TAG, error+"\n"+ error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                params.put("session_token", BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    public void getPropertyByViewPort(final LatLngBounds bounds, final BallabaResponseListener callback){
        String SW = "?SW="+bounds.southwest.latitude+","+bounds.southwest.longitude;
        String NE = "&NE="+bounds.northeast.latitude+","+bounds.northeast.longitude;
        String limit = "&limit="+Integer.MAX_VALUE;
        StringRequest stringRequest = new StringRequest(GET
                , EndpointsHolder.PROPERTY+SW+NE+limit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    Log.e(TAG, error+"\n"+ error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                params.put("session_token", BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void getAttachmentsAddonsConfig(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(GET
                , EndpointsHolder.PROPERTY_ATTACHMENTS_ADDONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    Log.e(TAG, error+"\n"+ error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                params.put("session_token", BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void getPropertyByAddress(ArrayList<String> addresses, FilterResultEntity filterResult, final BallabaResponseListener callback){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?address=");
        for (String address : addresses) {
            stringBuilder.append(address);
            stringBuilder.append("_");
        }
        stringBuilder.deleteCharAt(stringBuilder.toString().length()-1);

        StringBuilder filterStringBuilder = new StringBuilder();
        boolean isIncludeFilter = false;
        if(filterResult.getFromPrice() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromPrice="+filterResult.getFromPrice());
        }
        if(filterResult.getToPrice() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toPrice="+filterResult.getToPrice());
        }
        if(filterResult.getFromRooms() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromRooms="+filterResult.getFromRooms());
        }
        if(filterResult.getToRooms() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toRooms="+filterResult.getToRooms());
        }
        if(filterResult.getFromSize() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromSize="+filterResult.getFromSize());
        }
        if(filterResult.getToSize() != 0){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toSize="+filterResult.getToSize());
        }
        if(filterResult.isElectronics() != null){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&electronics="+filterResult.isElectronics());
        }
        if(filterResult.isFurnished() != null){
            if(!isIncludeFilter){
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&furniture="+filterResult.isFurnished());
        }
        if(filterResult.getAttachments_ids().contains("3")){
            if(!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&parking=true");
        }
        if(filterResult.getAttachments_ids().size()>0){
            if(!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            for (String id : filterResult.getAttachments_ids()){
                PropertyAttachmentAddonEntity entity = PropertyAttachmentsAddonsHolder.getInstance().getAttachmentsById(id);
                filterStringBuilder.append("&"+entity.title);
            }
        }

        String queryFilter = filterStringBuilder.toString();
        String queryAdresses = stringBuilder.toString();
        String queryUrl = EndpointsHolder.PROPERTY_BY_ADDRESS+queryAdresses+queryFilter;
        StringRequest getByAddress = new StringRequest(GET, queryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    Log.e(TAG, error+"\n"+ error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
                params.put("session_token", BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        getByAddress.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(getByAddress);
    }

    public StringBuilder apiRequest(StringBuilder sb) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.d(TAG, "request result: " + jsonResults);
        } catch(
                IOException e)

        {
            Log.e(TAG, "Error connecting to Places API", e);
            return null;
        } finally

        {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return jsonResults;
    }
}