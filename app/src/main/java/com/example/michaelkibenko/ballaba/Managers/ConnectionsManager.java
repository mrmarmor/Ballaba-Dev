package com.example.michaelkibenko.ballaba.Managers;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.UserManager;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
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
import java.util.HashMap;
import java.util.Map;

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
                    if(error.networkResponse != null){
                        callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                    }else{
                        callback.reject(new BallabaErrorResponse(500, null));
                    }
                }
            });

            queue.add(jsonObjectRequest);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void enterCode(String phoneNUmber, String code, final BallabaResponseListener callback){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", phoneNUmber);
            jsonObject.put("code", code);
            jsonObject.put("fcm_token", DeviceUtils.getInstance(true, context).getFcmToken());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EndpointsHolder.AUTHENTICATE, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
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
                    params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return null;
                }
            };;

            queue.add(jsonObjectRequest);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void getConfigRequest(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndpointsHolder.CONFIG,
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
        queue.add(stringRequest);
    }

    public void logInByToken(final BallabaResponseListener callback, final String token){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndpointsHolder.LOGIN_BY_TOKEN
                , new Response.Listener<String>() {
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
                params.put(GlobalValues.globalToken, token);
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void getRandomProperties(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndpointsHolder.PROPERTY,
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
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        queue.add(stringRequest);

    }

    //TODO all these 3 method below could be easily replaced by one single generic method
    public void getPropertyById(final String PROPERTY_ID, final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET
                , EndpointsHolder.PROPERTY+"/"+PROPERTY_ID, new Response.Listener<String>() {
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
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getPropertyByLatLng(final String PARAMS, final BallabaResponseListener callback, int offset){
        StringRequest stringRequest = new StringRequest(Request.Method.GET
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
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        queue.add(stringRequest);

    }

    public void getPropertyByViewPort(final LatLngBounds bounds, final BallabaResponseListener callback){
        String SW = "?SW="+bounds.southwest.latitude+","+bounds.southwest.longitude;
        String NE = "&NE="+bounds.northeast.latitude+","+bounds.northeast.longitude;
        String limit = "&limit="+Integer.MAX_VALUE;
        StringRequest stringRequest = new StringRequest(Request.Method.GET
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
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getAttachmentsAddonsConfig(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET
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
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
                return params;
            }
        };

        queue.add(stringRequest);
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