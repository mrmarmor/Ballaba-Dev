package com.example.michaelkibenko.ballaba;

import android.app.Application;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Twitter.initialize(getApplicationContext());
    }
}
