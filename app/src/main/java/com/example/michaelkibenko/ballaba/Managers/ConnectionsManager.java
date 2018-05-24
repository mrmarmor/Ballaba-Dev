package com.example.michaelkibenko.ballaba.Managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Config;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingDate;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Entities.ScoringUserData;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;
import static com.example.michaelkibenko.ballaba.Holders.EndpointsHolder.VIEWPORT;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class ConnectionsManager {
    private static final String TAG = ConnectionsManager.class.getSimpleName();

    private static ConnectionsManager instance;
    private Context context;
    private RequestQueue queue;

    public static ConnectionsManager getInstance(Context context) {
        if (instance == null) {
            instance = new ConnectionsManager(context);
        }
        return instance;
    }

    public static ConnectionsManager newInstance(Context context) {
        instance = new ConnectionsManager(context);
        return instance;
    }

    private ConnectionsManager(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(this.context);
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public <T> void addRequestToQueue(Request<T> request) {
        getQueue().add(request);
    }

    private Map<String, String> getHeadersWithSessionToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
        params.put(GlobalValues.sessionToken, BallabaUserManager.getInstance().getUserSesionToken());
        Log.d(TAG, "device id: " + params.get(GlobalValues.deviceId) + "\nsession token: " + params.get(GlobalValues.sessionToken));
        return params;
    }

    public void loginWithPhoneNumber(final String phoneNumber, final BallabaResponseListener callback) {
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

    public void enterCode(String phoneNUmber, final String code, final BallabaResponseListener callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", phoneNUmber);
            jsonObject.put("code", code);
            jsonObject.put("fcm_token", DeviceUtils.getInstance(true, context).getFcmToken());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EndpointsHolder.AUTHENTICATE, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BallabaUser user = BallabaUserManager.getInstance().generateUserFromJsonResponse(response.toString());
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
                    params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
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
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void getConfigRequest(final BallabaResponseListener callback) {
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

    public void logInByToken(final BallabaResponseListener callback, final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndpointsHolder.LOGIN_BY_TOKEN
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                Log.e(TAG, "Error: " + error.getMessage());
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
                params.put(GlobalValues.globalToken, token);
                params.put(GlobalValues.deviceId, DeviceUtils.getInstance(true, context).getDeviceId());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void getRandomProperties(final BallabaResponseListener callback) {
        BallabaSearchPropertiesManager.getInstance(context).setCurrentSearchEndpoint(EndpointsHolder.PROPERTY);
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
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    //TODO all these 3 method below could be easily replaced by one single generic method
    public void getPropertyById(final String PROPERTY_ID, final BallabaResponseListener callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET
                , EndpointsHolder.PROPERTY + PROPERTY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        queue.add(stringRequest);
    }

    public void getPropertyByLatLng(LatLng latLng, final BallabaResponseListener callback) {
        String params = "?latlong=" + latLng.latitude + "," + latLng.longitude;
        String endpoint = EndpointsHolder.PROPERTY + params;
        BallabaSearchPropertiesManager.getInstance(context).setCurrentSearchEndpoint(endpoint);
        StringRequest stringRequest = new StringRequest(GET
                , endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    public void getPropertyByViewPort(final LatLngBounds bounds, final BallabaResponseListener callback) {
        String SW = "?SW=" + bounds.southwest.latitude + "," + bounds.southwest.longitude;
        String NE = "&NE=" + bounds.northeast.latitude + "," + bounds.northeast.longitude;
        String limit = "&limit=" + Integer.MAX_VALUE;
        String endpoint = EndpointsHolder.PROPERTY + SW + NE + limit;
        BallabaSearchPropertiesManager.getInstance(context).setCurrentSearchEndpoint(endpoint);
        StringRequest stringRequest = new StringRequest(GET
                , endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void getAttachmentsAddonsConfig(final BallabaResponseListener callback) {
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
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void getPropertyByAddress(ArrayList<String> addresses, FilterResultEntity filterResult, final BallabaResponseListener callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?address=");
        for (String address : addresses) {
            String addressEncoded = URLEncoder.encode(address);
            stringBuilder.append(addressEncoded);
            stringBuilder.append("_");
        }
        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);

        StringBuilder filterStringBuilder = new StringBuilder();
        boolean isIncludeFilter = false;
        if (filterResult.getFromPrice() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromPrice=" + filterResult.getFromPrice());
        }
        if (filterResult.getToPrice() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toPrice=" + filterResult.getToPrice());
        }
        if (filterResult.getFromRooms() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromRooms=" + filterResult.getFromRooms());
        }
        if (filterResult.getToRooms() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toRooms=" + filterResult.getToRooms());
        }
        if (filterResult.getFromSize() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&fromSize=" + filterResult.getFromSize());
        }
        if (filterResult.getToSize() != 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&toSize=" + filterResult.getToSize());
        }
        if (filterResult.isElectronics() != null) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&electronics=" + filterResult.isElectronics());
        }
        if (filterResult.isFurnished() != null) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&furniture=" + filterResult.isFurnished());
        }
        if (filterResult.getAttachments_ids().contains("3")) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            filterStringBuilder.append("&parking=true");
        }
        if (filterResult.getAttachments_ids().size() > 0) {
            if (!isIncludeFilter) {
                filterStringBuilder.append("&filter = true");
                isIncludeFilter = true;
            }
            for (String id : filterResult.getAttachments_ids()) {
                PropertyAttachmentAddonEntity entity = PropertyAttachmentsAddonsHolder.getInstance().getAttachmentById(id);
                filterStringBuilder.append("&" + entity.title);
            }
        }

        String queryFilter = filterStringBuilder.toString();
        String queryAdresses = stringBuilder.toString();
        String queryUrl = EndpointsHolder.PROPERTY + queryAdresses + queryFilter;
        BallabaSearchPropertiesManager.getInstance(context).setCurrentSearchEndpoint(queryUrl);
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
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        getByAddress.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(getByAddress);
    }

    public void lazyLoading(final BallabaResponseListener callback) {
        int size = BallabaSearchPropertiesManager.getInstance(context).getResults().size();
        String queryUrl = BallabaSearchPropertiesManager.getInstance(context).getCurrentSearchEndpoint();

        if (queryUrl != null) {
            if (isQueryAdded(queryUrl)) {
                queryUrl += "&offset=" + size;
            } else {
                queryUrl += "?&offset=" + size;
            }
            StringRequest lazyloading = new StringRequest(GET, queryUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BallabaOkResponse okResponse = new BallabaOkResponse();
                    okResponse.setBody(response);
                    callback.resolve(okResponse);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                    } else {
                        Log.e(TAG, error + "\n" + error.getMessage());
                        callback.reject(new BallabaErrorResponse(500, null));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getHeadersWithSessionToken();
                }
            };

            lazyloading.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(lazyloading);
        }
    }

    public void getViewports(final BallabaResponseListener callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEWPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse okResponse = new BallabaOkResponse();
                okResponse.setBody(response);
                callback.resolve(okResponse);            }
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
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void saveViewPort(String name, LatLngBounds bounds, float zoom, final BallabaResponseListener callback) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("NE", bounds.northeast.latitude + "," + bounds.northeast.longitude);
            jsonObject.put("SW", bounds.southwest.latitude + "," + bounds.southwest.longitude);
            jsonObject.put("name", name);
            jsonObject.put("zoom_level", zoom);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VIEWPORT, jsonObject, new Response.Listener<JSONObject>() {
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getHeadersWithSessionToken();
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        } catch (JSONException ex) {
            callback.reject(new BallabaErrorResponse(500, "JSON parsing error"));
            ex.printStackTrace();
        }
    }

    public void removeViewport(String propertyId, final BallabaResponseListener callback) {
        String query = VIEWPORT + propertyId;
        StringRequest stringRequest = new StringRequest(DELETE, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                callback.resolve(new BallabaOkResponse());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }


    public void addToFavoritesProperty(String propertyId) {
        String query = EndpointsHolder.PROPERTY + propertyId + "/save";
        StringRequest stringRequest = new StringRequest(POST, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                callback.resolve(new BallabaOkResponse());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.networkResponse != null) {
//                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
//                } else {
//                    Log.e(TAG, error + "\n" + error.getMessage());
//                    callback.reject(new BallabaErrorResponse(500, null));
//                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void removeFromFavoritesProperty(String propertyId) {
        String query = EndpointsHolder.PROPERTY + propertyId + "/save";
        StringRequest stringRequest = new StringRequest(DELETE, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                callback.resolve(new BallabaOkResponse());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.networkResponse != null) {
//                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
//                } else {
//                    Log.e(TAG, error + "\n" + error.getMessage());
//                    callback.reject(new BallabaErrorResponse(500, null));
//                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void uploadUser(/*final String userId, */JSONObject userData
            , final BallabaResponseListener callback) throws JSONException {

        final ProgressDialog pd = ((BaseActivity)context).getDefaultProgressDialog(context, "Uploading...");
        pd.show();

        String url = EndpointsHolder.USER;//userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PUT, url, userData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                BallabaUser user = BallabaUserManager.getInstance().generateUserFromJsonResponse(response.toString());
                if (user == null) {
                    callback.reject(new BallabaErrorResponse(500, null));
                } else {
                    callback.resolve(user);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                @StringRes String message = context.getString(R.string.error_property_upload);
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, message));
                } else {
                    callback.reject(new BallabaErrorResponse(500, message));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);

    }

    public void uploadProperty(final JSONObject propertyData, final BallabaResponseListener callback) {
        final ProgressDialog pd = ((BaseActivity)context).getDefaultProgressDialog(context, "Uploading...");
        pd.show();

        //TODO IMPORTANT!!
        //TODO if user fill invalid city or address server receive reject. so i need to tell user about it!!!

        String url = getUrl(propertyData);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, url, propertyData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                try {
                    if (response.has("id")) {
                        //next line is due to a server bug: when user uploads photo it returns id that represent the photo, not the property
                        String id = response.has("property_id")? response.get("property_id")+"":response.get("id")+"";
                        SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_ID, id);
                    }

                    Date now = new Date(Calendar.getInstance().getTimeInMillis());
                    String nowStr = StringUtils.getInstance(true, context).dateToString(now);
                    SharedPreferencesManager.getInstance(context).putString(
                            SharedPreferencesKeysHolder.PROPERTY_UPLOAD_DATE, nowStr);
                    if (response.has("photo_url")) {
                        //BallabaPropertyPhoto photo = parsePhotoResponse(response);
                        //callback.resolve(photo);
                        callback.resolve(new BallabaPropertyPhoto(Integer.parseInt(response.getString("id"))));
                    } else {
                        callback.resolve(new BallabaBaseEntity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                @StringRes String message = context.getString(R.string.error_property_upload);
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, message));
                } else {
                    callback.reject(new BallabaErrorResponse(500, message));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    public void deletePropertyPhoto(final String propertyId, final int photoId, final BallabaResponseListener callback){
        final String url = String.format("%s%s/photos/%s", EndpointsHolder.PROPERTY, propertyId, photoId);
        Log.d(TAG, "delete url: " + url);

        StringRequest stringRequest = new StringRequest(DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.resolve(new BallabaOkResponse());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, error.getMessage()));
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void getSavedProperties(final BallabaResponseListener callback) {
        StringRequest stringRequest = new StringRequest(GET, EndpointsHolder.GET_SAVED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse ok = new BallabaOkResponse();
                ok.body = response;
                callback.resolve(ok);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }


    /*public void getScoringLabels(final BallabaResponseListener callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, EndpointsHolder.SCORING_LABELS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                BallabaOkResponse callbackResponce = new BallabaOkResponse();
                callbackResponce.setBody(response.toString());
                callback.resolve(callbackResponce);
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
        }){
            @Override
            public Map<String, String> getHeaders() {
                return getHeadersWithSessionToken();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(jsonObjectRequest);
    }*/

    public void uploadScoringID(JSONObject object , boolean firstImg  , final BallabaResponseListener callback){
        JsonObjectRequest request = new JsonObjectRequest(POST,
                firstImg ? EndpointsHolder.SCORING_FIRST_IMAGE_ID : EndpointsHolder.SCORING_SECOND_IMAGE_ID,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BallabaOkResponse callbackResponce = new BallabaOkResponse();
                        callbackResponce.setBody(response.toString());
                        callback.resolve(callbackResponce);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, error.getMessage()));
                } else {
                    callback.reject(new BallabaErrorResponse(500, error.getMessage()));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeadersWithSessionToken();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    public void sendScoringData(ScoringUserData userData, final BallabaResponseListener callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("birth_date" , userData.getBirthDate().trim().toString());
            jsonObject.put("car_owner" , userData.isHasCar());
            jsonObject.put("marital_status" , userData.getFamilyStatus().trim().toString());
            jsonObject.put("no_of_kids" , userData.getNumOfChilds());
            jsonObject.put("occupational_status" , userData.getWorkStatus().trim().toString());
            jsonObject.put("seniority" , userData.getWorkSeniority());
            jsonObject.put("income" , userData.getUserIncome());
            jsonObject.put("work_website" , userData.getWorkEmail().trim().toString());
            jsonObject.put("work_contact" , userData.getUserEmail().trim().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, EndpointsHolder.SCORING_DATA, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                BallabaOkResponse callbackResponce = new BallabaOkResponse();
                callbackResponce.setBody(response.toString());
                callback.resolve(callbackResponce);

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
                return getHeadersWithSessionToken();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);

    }

    public void uploadPropertyMeetingsDates(ArrayList<BallabaMeetingsPickerDateEntity> data, final BallabaResponseListener callback){
        try{
            JSONArray jsonArray = parseOpenDoorDatesToJsonArray(data);
            //TODO chnage the property id
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(POST, EndpointsHolder.UPLOAD_METTINGS_DATES+"1/"+"opendoor", jsonArray, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    BallabaOkResponse ok = new BallabaOkResponse();
                    callback.resolve(ok);
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
                    return getHeadersWithSessionToken();
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void getMyProperties(final BallabaResponseListener callback) {
        StringRequest stringRequest = new StringRequest(GET, EndpointsHolder.MY_PROPERTIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BallabaOkResponse ok = new BallabaOkResponse();
                ok.body = response;
                callback.resolve(ok);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                } else {
                    Log.e(TAG, error + "\n" + error.getMessage());
                    callback.reject(new BallabaErrorResponse(500, null));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersWithSessionToken();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private JSONArray parseOpenDoorDatesToJsonArray(ArrayList<BallabaMeetingsPickerDateEntity> data) throws JSONException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        JSONArray returnable = new JSONArray();

        for (BallabaMeetingsPickerDateEntity item : data) {
            for (BallabaMeetingDate meeting : item.dates) {
                JSONObject object = new JSONObject();
                object.put("start_time", dateFormat.format(meeting.from));
                object.put("end_time", dateFormat.format(meeting.to));
                object.put("is_personal", meeting.isPrivate);
                if(meeting.isRepeat && meeting.numberOfRepeats.equals("ללא הגבלה")){
                    object.put("repeat", true);
                }else{
                    object.put("repeat", false);
                    returnable.put(object);
                    int repeats = Integer.parseInt(meeting.numberOfRepeats);
                    int daysTimeValue = 7;
                    for (int i = 0; i <= repeats; i++) {
                        JSONObject repeatObject = new JSONObject();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(meeting.from);
                        cal.add(Calendar.DAY_OF_MONTH, daysTimeValue);
                        repeatObject.put("start_time", dateFormat.format(cal.getTime()));
                        cal.setTime(meeting.to);
                        cal.add(Calendar.DAY_OF_MONTH, daysTimeValue);
                        repeatObject.put("end_time", dateFormat.format(cal.getTime()));
                        repeatObject.put("is_personal", meeting.isPrivate);
                        repeatObject.put("repeat", false);
                        daysTimeValue +=7;
                        returnable.put(repeatObject);
                    }
                }
            }
        }

        return returnable;
    }

    private boolean isQueryAdded(String url) {
        char[] charArray = url.toCharArray();
        for (char input : charArray) {
            if (input == '?') {
                return true;
            }
        }
        return false;
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
        } catch (
                IOException e) {
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

    //this method changes url for step "uploading photos" in add property, from "https://api.ballaba-it.com/dev/property/"
    // to "https://api.ballaba-it.com/v1/property/{property_id}/photos"
    private String getUrl(JSONObject propertyData){
        String url = EndpointsHolder.PROPERTY;
        try {
            if (propertyData.has("step") && propertyData.get("step").equals("photos"))
                url = "https://api.ballaba-it.com/Dev/property/" + propertyData.get("property_id") + "/photos";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*private BallabaPropertyPhoto parsePhotoResponse(JSONObject jsonObject) throws JSONException{
        final int id = Integer.parseInt(jsonObject.get("id") + "");
        final Uri photo = Uri.parse(jsonObject.get("photo_url")+"");
        final HashSet<PropertyAttachmentAddonEntity> tags = null;//TODO parse tags!!!

        return new BallabaPropertyPhoto(id, true, photo, tags);
    }*/
}