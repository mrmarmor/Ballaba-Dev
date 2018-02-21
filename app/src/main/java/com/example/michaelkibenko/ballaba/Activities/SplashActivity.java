package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SplashLayoutBinding;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class SplashActivity extends BaseActivity {

    private SplashLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.splash_layout);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, PreAuthActivity.class));
            }
        }, 4000);
    }

}
