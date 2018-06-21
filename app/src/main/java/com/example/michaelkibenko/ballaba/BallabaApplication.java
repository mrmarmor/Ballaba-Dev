package com.example.michaelkibenko.ballaba;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class BallabaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BallabaConnectivityAnnouncer.getInstance(getApplicationContext());
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
    }
}
