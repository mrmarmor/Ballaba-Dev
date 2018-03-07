package com.example.michaelkibenko.ballaba;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class BallabaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BallabaConnectivityAnnouncer.getInstance(getApplicationContext());
    }
}
