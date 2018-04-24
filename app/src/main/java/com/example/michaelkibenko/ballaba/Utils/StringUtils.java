package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        if (number.length() > 3) {
            for (int i = number.length() % 3; i < number.length() - 2; i += 3) {
                number = new StringBuilder(number).insert(i, ",").toString();
            }
        }

        return number;
    }

    public String formattedHebrew(@NonNull String s){
        try {
            return new String(s.getBytes("ISO_8859_1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long stringToTime(String dateString){
        if (dateString == null)
            return 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateString).getTime();
        } catch (ParseException e ) {
            e.printStackTrace();
            return 0L;
        }
    }
    public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date newDate = new Date();
        return sdf.format(date);
    }

    public Bitmap stringToBitmap(@Nullable String bitmapStr){
        byte[] decodedString = Base64.decode(bitmapStr, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}