package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.toolbox.StringRequest;

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
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void showSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //to get view from context as needed in the function below use: ((Activity)context).getWindow().getDecorView()
    public void hideSoftKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
