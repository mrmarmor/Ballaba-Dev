package com.example.michaelkibenko.ballaba.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberActivity extends BaseActivity {
    private final String TAG = EnterPhoneNumberActivity.class.getSimpleName();
    private final int SMS_PERMISSION_REQ_CODE = 1;

    private BallabaConnectivityListener client;
    private EnterPhoneNumberLayoutBinding binder;
    private EnterPhoneNumberPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_phone_number_layout);
        presenter = new EnterPhoneNumberPresenter(this, binder);
        binder.setPresenter(presenter);

        if(!BallabaConnectivityAnnouncer.getInstance(this).isConnected())
            Toast.makeText(EnterPhoneNumberActivity.this, "Here will be error dialog because of no internet", Toast.LENGTH_LONG).show();

        listenToNetworkChanges();
    }

    private void listenToNetworkChanges(){
        client = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if (!is){
                    //TODO replace next line with a dialog
                    Toast.makeText(EnterPhoneNumberActivity.this, "Here will be error dialog because of no internet", Toast.LENGTH_LONG).show();
                    Snackbar.make(binder.enterPhoneNumberRootLayout, "Here will be error dialog because of no internet", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EnterPhoneNumberActivity.this, "TESTING: network is on!", Toast.LENGTH_LONG).show();
                    Snackbar.make(binder.enterPhoneNumberRootLayout, "TESTING: network is on!", Toast.LENGTH_LONG).show();

                    //TODO here we prevent user from sending phone when there is no network. However, if we don't prevent him, he got an error message
                    //TODO in "enter_phone_number_text_error_answer" textView anyway. Decide what is better.
                }

            }
        };
        BallabaConnectivityAnnouncer.getInstance(this).register(client);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BallabaConnectivityAnnouncer.getInstance(this).unRegister(client);
        UiUtils.instance(true, this).hideSoftKeyboard(getWindow().getDecorView());
    }

    public void getPermissionsToReadSms(View v){
        Log.d(TAG, "registerReadSmsReceiver");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS}, SMS_PERMISSION_REQ_CODE);
            presenter.sendPhoneNumber();

        }else{
            Log.d(TAG, "sms receiver permission had already been given");
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.sendPhoneNumber(true);
                } else {
                    presenter.sendPhoneNumber(false);

                }
            }
        }
    }*/
}
