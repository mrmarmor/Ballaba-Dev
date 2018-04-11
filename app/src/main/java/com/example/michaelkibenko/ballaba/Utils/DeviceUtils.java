package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import com.google.firebase.iid.FirebaseInstanceId;

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
        this.context = context.getApplicationContext();
    }

    public String getDeviceId(){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getFcmToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }

    /*public String readSms() {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        String message = "";

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                for(int index=0; index < cursor.getColumnCount(); index++)
                {
                    message += " " + cursor.getColumnName(index) + ":" + cursor.getString(index);
                }
                // use msgData
            } while (cursor.moveToNext());
     *//*   } else {
            // empty box, no SMS
        }*//*

        return message;
    }*/


}
