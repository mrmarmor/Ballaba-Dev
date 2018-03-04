package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.michaelkibenko.ballaba.BallabaApplication;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class ConnectionsManager implements ImageLoader.ImageCache{

    private static ConnectionsManager instance;
    private Context context;
    private RequestQueue queue;
    public ImageLoader imageLoader;
    private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

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

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }

    public void logInWithPhoneNumber(final BallabaResponseListener callback, final String phoneNumber){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndpointsHolder.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.resolve(new BallabaOkResponse());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phoneNumber);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}