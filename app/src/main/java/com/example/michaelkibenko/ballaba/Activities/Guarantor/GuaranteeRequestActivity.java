package com.example.michaelkibenko.ballaba.Activities.Guarantor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivityWithActionBar;
import com.example.michaelkibenko.ballaba.Common.GuaranteeFcmService;
import com.example.michaelkibenko.ballaba.Common.GuaranteeFirebaseInstanceIdService;
import com.example.michaelkibenko.ballaba.Common.GuaranteeReceiver;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityGuaranteeRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class GuaranteeRequestActivity extends BaseActivityWithActionBar {
    private ActivityGuaranteeRequestBinding binder;

    private GuaranteeReceiver guaranteeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_guarantee_request);

        initViews();
    }

    private void initViews() {
        //TODO guarantor device should fetch tenant user name and profile image from server or from notification.
        //TODO for now, static data will be set.
        Drawable tenantProfileImage = getResources().getDrawable(R.drawable.add_user_b_lue_60);
        String tenantFullName = "צחי קורן";
        String tenantExplanation = "אחי, תעזור לחבר בקטנה...";
        String guaranteePrice = "₪" + StringUtils.getInstance(true).formattedNumberWithComma("30000");

        binder.guaranteeRequestTenantProfileImage.setImageDrawable(tenantProfileImage);
        binder.guaranteeRequestTenantNameTextView.setText(tenantFullName);
        binder.guaranteeRequestTenantExplanation.setText(tenantExplanation);
        binder.guaranteeRequestGuaranteePrice.setText(guaranteePrice);
    }

    public void onClickConfirm(View view) {
        guaranteeReceiver = new GuaranteeReceiver();
        registerReceiver(guaranteeReceiver, new IntentFilter("com.example.michaelkibenko.ballaba.Common.GuaranteeReceiver"));

        startService(new Intent(this, GuaranteeFcmService.class));
        startService(new Intent(this, GuaranteeFirebaseInstanceIdService.class));
        Log.d("GuaranteeRequest", "token: "+FirebaseInstanceId.getInstance().getToken());

        /*TESTING*/String regToken = "fTnTNRA6um0:APA91bHNHHt6yD1ECAnX3Uy17U8UXBj5PpbjmeD1NB7XkUMwAg4KndEllfoysAFhQxp5lrmFwSIiOkjmGVn72X5Dr0iaBb9D5SsbmLWhIlymdEZtI7s_XL4Zb5EDXMcNYvmR-NEGun1I";

        regToken = FirebaseInstanceId.getInstance().getToken();

        if (regToken != null)
            sendNotification(regToken, "user name", "my message");
        finish();
    }

    public void onClickDecline(View view) {
        Toast.makeText(this, "declined", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void sendNotification(final String regToken, final String toUser, final String message) {
        final String LEGACY_SERVER_KEY = "AIzaSyBHsS2FCT2ahznLVAFRU0DWHrxsuAGBQMc";
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
         //FirebaseMessaging.getInstance().subscribeToTopic("PHONE_NUMBER);
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body",message);
                    dataJson.put("title","hello "+toUser);
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
                    Log.d("firebaseMessage :", response.isSuccessful()+":"+response.message()+":"+response.code()+":"+response.sentRequestAtMillis()+":"+finalResponse);
                    Log.d("firebase token :", regToken);

                }catch (IOException | JSONException | NumberFormatException e){
                    Log.d("firebaseMessageError ",e+"");
                }
                return null;
            }
        }.execute();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (guaranteeReceiver != null)
            unregisterReceiver(guaranteeReceiver);
    }
}



  /*private void sendNotification2(final String regToken, final String toUid, final String message) {
        Log.d("firebase", "firebase: " + regToken);//f0esj6gU9mw:APA91bGzMfVQQkBCTDDH9uxSB4TCTbT97eEnXJU09SOk1VDpyGZEd1rXeia35v7a_jJ2jqqIr3fpzthRsA9revrRdo5dLMxsINGxD3ViYSqizYsYwh5OI-sq09fai5IdTVL_TXpBVD3X
        Bundle b = new Bundle();
        b.putString("to", regToken);
        FirebaseMessaging.getInstance().send(new RemoteMessage(b));

        FirebaseMessaging.getInstance().subscribeToTopic("0507729906")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                        Log.d("firebase topic: ", msg);
                        Toast.makeText(GuaranteeRequestActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("firebase failure", e.getMessage());
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("firebase guarantee: ", intent+"");
            }
        }, new IntentFilter());
    }*/
