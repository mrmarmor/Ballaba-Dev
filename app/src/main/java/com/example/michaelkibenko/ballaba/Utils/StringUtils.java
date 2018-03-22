package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;

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
        //for (int i = number.length() % 3; i >= 3 ; i -= 3){
        for (int i = number.length() % 3; i < number.length() ; i += 3){
            number = new StringBuilder(number).insert(i, ",").toString();
        }

        return number;
    }

}
