package com.example.michaelkibenko.ballaba.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.example.michaelkibenko.ballaba.Holders.EndpointsHolder.VIEWPORT;

/**
 * Created by User on 14/06/2018.
 */

public class GuaranteeReceiver extends BroadcastReceiver {
    private static final String TAG = GuaranteeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("param"), Toast.LENGTH_SHORT).show();
        String token = intent.getStringExtra("token");
//        sendNotification(token, "dummy user", "dummy message");
    }

    public void sendNotification(final String regToken, final String toUid, final String message) {
        final String LEGACY_SERVER_KEY = "AIzaSyBHsS2FCT2ahznLVAFRU0DWHrxsuAGBQMc";
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body",message);
                    dataJson.put("title","hello"+toUid);
                    json.put("notification",dataJson);
                    json.put("to",regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .header("Authorization","key="+ LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.d("firebaseMessage ", regToken+":"+finalResponse);
                    //addMessageToFirebase(mContext, toUid, message, response.isSuccessful());

                }catch (Exception e){
                    Log.d("firebaseMessageError ",e+"");
                }
                return null;
            }
        }.execute();

    }


    //with volley
   /* public void sendNotification1(final String regToken, final String toUid, final String message) {
        FirebaseMessaging.getInstance().subscribeToTopic("message");

        final String URL = "https://fcm.googleapis.com/fcm/send";

        try {
            JSONObject json = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("body", message);
            dataJson.put("title", "hello"+toUid);
            json.put("notification", dataJson);
            json.put("to", regToken);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "success: " + response);
                    //callback.resolve(new BallabaOkResponse());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "failure");
                    if (error.networkResponse != null) {
                        //callback.reject(new BallabaErrorResponse(error.networkResponse.statusCode, null));
                    } else {
                        //callback.reject(new BallabaErrorResponse(500, null));
                    }
                }
            }) *//*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getHeadersWithSessionToken();
                }
            }*//*;

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //queue.add(jsonObjectRequest);
        } catch (JSONException ex) {
            //callback.reject(new BallabaErrorResponse(500, "JSON parsing error"));
            ex.printStackTrace();
        }




    }
*/
}
