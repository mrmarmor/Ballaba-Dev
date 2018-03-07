package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SplashLayoutBinding;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private BallabaConnectivityListener connectivityListener;
    private long MIN_SPLASH_DELAY = 3000;
    private SplashLayoutBinding binder;
    private long startTime;
    boolean isGetConfig, isLoggedIn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.splash_layout);
        connectivityListener = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if(!is){
                    Toast.makeText(SplashActivity.this, "Here will be error dialog because of no internet", Toast.LENGTH_LONG).show();
                }else{
                    if(!isGetConfig){
                        getConfigRequest();
                    }else if(!isLoggedIn){

                    }
                }
            }
        };
        BallabaConnectivityAnnouncer.getInstance(this).register(connectivityListener);

        startTime = System.currentTimeMillis();
        if(BallabaConnectivityAnnouncer.getInstance(this).getStatus()){
            getConfigRequest();
        }
        else {
            Toast.makeText(SplashActivity.this, "Here will be error dialog because of no internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getConfigRequest(){
        ConnectionsManager.getInstance(this).getConfigRequest(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                isGetConfig = true;
                long endTime = System.currentTimeMillis();
                if(endTime - startTime > MIN_SPLASH_DELAY){
                    continueFlow();
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            continueFlow();
                        }
                    }, endTime - startTime - MIN_SPLASH_DELAY);
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Toast.makeText(SplashActivity.this, "Here will be error dialog", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void continueFlow(){
        Intent start = new Intent(SplashActivity.this, TestingActivity.class);
        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(start);
        finish();
    }


}
