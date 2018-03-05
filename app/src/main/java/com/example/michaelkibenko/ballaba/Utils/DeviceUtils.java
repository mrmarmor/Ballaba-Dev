package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by User on 05/03/2018.
 */

public class DeviceUtils {
    private final Context context;
    private static DeviceUtils instance;

    public static DeviceUtils getInstance(boolean isOnce, Context context){
        if(!isOnce) {
            if (instance == null) {
                instance = new DeviceUtils(context);
            }
            return instance;
        }
        return new DeviceUtils(context);
    }

    private DeviceUtils(Context context){
        this.context = context;
    }

    public String getDeviceId(){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


}
