package com.example.michaelkibenko.ballaba.Managers;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.UserManager;
import android.text.LoginFilter;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
                    params.put("device_id", DeviceUtils.getInstance(true, context).getDeviceId());
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
                Log.e(TAG, error.getMessage());
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
        queue.add(stringRequest);
    }

    public void getProperty(final String deviceId, final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndpointsHolder.PROPERTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                params.put("deviceId", deviceId);
                return params;
            }
        };

    }

    /*public void apiRequest( final StringBuilder sb, final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, sb.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        new Gson().toJson()
                        BallabaOkResponse a = new BallabaOkResponse();

                        callback.resolve(new BallabaOkResponse());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                if(error.networkResponse != null){
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                }else{
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        });

        queue.add(stringRequest);
    }*/

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



    /*public class UrlTask extends AsyncTask<StringBuilder, String, StringBuilder> {
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            //Log.d(TAG, apiKey);//AIzaSyBJ-y7b2ymSPDjM9vTKwHI8hFq072eehPk



            @Override
            protected StringBuilder doInBackground(StringBuilder... sb) {
                try {
                    URL url = new URL(sb[0].toString());
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

            @Override
            protected void onPostExecute(StringBuilder stringBuilder) {
                super.onPostExecute(stringBuilder);
            }
        }
*/
    //}
}