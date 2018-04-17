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
    private final int SMS_PERMISSION_REQ_CODE = 100;

    private EnterPhoneNumberLayoutBinding binder;
    private EnterPhoneNumberPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_phone_number_layout);
        presenter = new EnterPhoneNumberPresenter(this, binder);
        binder.setPresenter(presenter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BallabaConnectivityAnnouncer.getInstance(this).unRegister(presenter.connectivityListener);
        UiUtils.instance(true, this).hideSoftKeyboard(getWindow().getDecorView());
    }

    public void getPermissionsToReadSms(View v){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BROADCAST_SMS}, SMS_PERMISSION_REQ_CODE);
        }else{
            presenter.sendPhoneNumber();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_REQ_CODE: {
                //here we do not check the permission request result because of needless
                    presenter.sendPhoneNumber();
            }
        }
    }
}
