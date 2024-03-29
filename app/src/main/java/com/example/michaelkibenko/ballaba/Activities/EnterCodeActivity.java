package com.example.michaelkibenko.ballaba.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodeActivity extends BaseActivity {
    private static String TAG = EnterCodeActivity.class.getSimpleName();

    private EnterCodePresenter presenter;
    private BallabaSMSReceiver smsReceiver;
    private boolean isReceiverRunning;
    private EnterCodeLayoutBinding binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_code_layout);

        String phoneNumber = getIntent().getStringExtra(EnterPhoneNumberPresenter.PHONE_NUMBER_EXTRA_KEY);
        String countryCode = getIntent().getStringExtra(EnterPhoneNumberPresenter.COUNTRY_CODE_EXTRA_KEY);
        presenter = new EnterCodePresenter(this, binder, countryCode, phoneNumber);
        binder.setPresenter(presenter);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            runSMSReader();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        UiUtils.instance(true, this).hideSoftKeyboard(getWindow().getDecorView());
        stopSmsReceiver();
    }

    @Override
    protected void onDestroy() {
        BallabaConnectivityAnnouncer.getInstance(this).unRegister(presenter.connectivityListener);
        super.onDestroy();
    }

    public void runSMSReader(){
        if(!isReceiverRunning) {
            smsReceiver = new BallabaSMSReceiver();
            IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(smsReceiver, intentFilter);
            isReceiverRunning = true;
        }
    }
    public void stopSmsReceiver(){
        try {
            if (isReceiverRunning)//this condition prevents duplicated processes of receivers
                unregisterReceiver(smsReceiver);
        } catch (IllegalArgumentException e){//this catch prevents "Receiver not registered" exception error
            Log.d(TAG, e.getMessage());
        }
    }

    public class BallabaSMSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");

            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for (Object mPdus : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) mPdus);

                    if (smsMessage.getDisplayOriginatingAddress().equals(GlobalValues.appName)) {//read only sms from Ballaba
                        String messageBody = smsMessage.getMessageBody();
                        Log.d(TAG, messageBody.substring(0, 4));
                        presenter.autoFillCode(messageBody.substring(0, 4));
                    }
                }
            }
        }
    }
}