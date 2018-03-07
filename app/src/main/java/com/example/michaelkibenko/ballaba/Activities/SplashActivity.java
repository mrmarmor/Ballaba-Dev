package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SplashLayoutBinding;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class SplashActivity extends BaseActivity {
    private final String TOKEN_NOT_EXISTS = "token not exists";

    private SplashLayoutBinding binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.splash_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                continueFlow();
            }
        }, 4000);
    }

    private void continueFlow(){
        Intent start;
        String token = SharedPreferencesManager.getInstance(this).getString(SharedPreferencesKeysHolder.GLOBAL_TOKEN, TOKEN_NOT_EXISTS);
        if (token.equals(TOKEN_NOT_EXISTS)) {
            start = new Intent(SplashActivity.this, TestingActivity.class);
        } else {
            start = new Intent(SplashActivity.this, MainActivity.class);
        }

        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(start);
        finish();
    }
}
