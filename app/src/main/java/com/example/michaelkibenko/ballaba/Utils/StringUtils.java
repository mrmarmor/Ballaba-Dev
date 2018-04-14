package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by User on 14/03/2018.
 */

public class StringUtils {
    private Context context;
    private static StringUtils instance;

    public static StringUtils getInstance(boolean isOnce, Context context){
        if(!isOnce) {
            if (instance != null)
                return instance;
        }

        return new StringUtils(context);
    }

    private StringUtils(Context context){
        if (context != null)
            this.context = context.getApplicationContext();
    }

    public String formattedNumberWithComma(String number){
        for (int i = number.length() % 3; i < number.length() - 2 ; i += 3){
            number = new StringBuilder(number).insert(i, ",").toString();
        }

        return number;
    }

    public String formattedHebrew(String s){
        try {
            return new String(s.getBytes("ISO_8859_1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
