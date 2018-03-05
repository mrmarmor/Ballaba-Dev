package com.example.michaelkibenko.ballaba.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Common.BallabaSmsListener;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodeActivity extends BaseActivity {
    private static String TAG = EnterCodeActivity.class.getSimpleName();
    private final int SMS_PERMISSION_REQ_CODE = 1;

    private EnterCodePresenter presenter;
    private BallabaSMSReceiver smsReceiver;
    private boolean isReceiverRunning;

    private EnterCodeLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_code_layout);

        String phoneNumber = getIntent().getStringExtra(EnterPhoneNumberPresenter.PHONE_NUMBER_EXTRA_KEY);
        presenter = new EnterCodePresenter(this, binder, phoneNumber);
        binder.setPresenter(presenter);

        registerReadSmsReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopReceiver();
    }

    private void registerReadSmsReceiver(){
        Log.e(TAG, "registerReadSmsReceiver");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EnterCodeActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS}, SMS_PERMISSION_REQ_CODE);
        }else{
            Log.e(TAG, "else called");
            runReceiver();
        }
    }


    public void runReceiver(){
        //TODO delete 2 lines below
        /*BallabaConnectivityAnnouncer announcer = BallabaConnectivityAnnouncer.getInstance();
        announcer.register(new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                Log.e("tag", "connected?"+is);
            }
        });
*/
        Log.e(TAG, "Before Receiver registration");
        if(!isReceiverRunning) {
            smsReceiver = new BallabaSMSReceiver();
            Log.e(TAG, "Receiver registration");
            IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(new BallabaSMSReceiver(), intentFilter);
            isReceiverRunning = true;
        }
    }
    public void stopReceiver(){
        if(isReceiverRunning)
            unregisterReceiver(smsReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    registerReadSmsReceiver();
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    public class SmsReceiver extends BroadcastReceiver {
//        //private BallabaSmsListener mListener;
//        public Pattern p = Pattern.compile("(|^)\\d{6}");
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e(TAG, "onReceive");
//            Toast.makeText(EnterCodeActivity.this, "Received", Toast.LENGTH_LONG).show();
//
//            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
//                Log.e("tag", "1");
//                Bundle data = intent.getExtras();
//                Object[] pdus = (Object[]) data.get("pdus");
//                for (int i = 0; i < pdus.length; i++) {
//                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    Log.e("tag", "2"+":"+ GlobalValues.appName+":"+smsMessage.getDisplayOriginatingAddress());
//
//                    if (smsMessage.getDisplayOriginatingAddress().equals(GlobalValues.appName)) {//read only sms from Ballaba
//
//                        String messageBody = smsMessage.getMessageBody();
//                    }
//                }
//            }
//        }
//    }

    public class BallabaSMSReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(EnterCodeActivity.this, "", Toast.LENGTH_LONG).show();
        }
    }



}