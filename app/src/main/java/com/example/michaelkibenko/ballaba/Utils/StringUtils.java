package com.example.michaelkibenko.ballaba.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 14/03/2018.
 */

public class StringUtils {
    private static StringUtils instance;

    public static StringUtils getInstance(boolean isOnce){
        if(!isOnce) {
            if (instance != null)
                return instance;
        }

        return new StringUtils();
    }

    private StringUtils(){

    }

    public String formattedNumberWithComma(String number){
        if (number == null)
            return "";//TODO maybe return "missing price" instead

        if (number.length() > 3) {
            for (int i = number.length() % 3; i < number.length() - 2; i += 3) {
                number = new StringBuilder(number).insert(i, ",").toString();
            }
        }

        return number;
    }

    public String getFormattedDateString(Calendar calendar, boolean isTimeInculded){

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = "יום ";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay += "שני";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay += "שלישי";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay += "רביעי";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay += "חמישי";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay += "שישי";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay += "שבת";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay += "ראשון";
        }

        weekDay +=", "+calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);

        if(month == 0){
            weekDay += " לינואר";
        }else if(month == 1){
            weekDay += " לפברואר";
        } else if(month == 2){
            weekDay += " למרץ";
        }else if(month == 3){
            weekDay += " לאפריל";
        }else if(month == 4){
            weekDay += " למאי";
        }else if(month == 5){
            weekDay += " ליוני";
        }else if(month == 6){
            weekDay += " ליולי";
        }else if(month == 7){
            weekDay += " לאוגוסט";
        }else if(month == 8){
            weekDay += " לספטמבר";
        }else if(month == 9){
            weekDay += " לאוקטובר";
        }else if(month == 10){
            weekDay += " לנובמבר";
        }else if(month == 11){
            weekDay += " לדצמבר";
        }
        if (isTimeInculded){
            int min = calendar.get(Calendar.MINUTE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            weekDay += " בשעה " + (hour == 0 ? hour+ "0" : hour) + ":" + (min == 0 ? min+ "0" : min);
        }
        return weekDay;
    }

    public String formattedHebrew(@NonNull String s){
        try {
            if (s.length() > 0 && (s.codePointAt(0) < 1488 || s.codePointAt(0) > 1514))//= non-hebrew
                return new String(s.getBytes("ISO_8859_1"), "utf-8");
            else
                return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String trimNull(@NonNull String s){
        return s.equals("null") ? null : s;
    }

    public Long stringToTime(String dateString){
        if (dateString == null)
            return 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(dateString).getTime();
        } catch (ParseException e ) {
            e.printStackTrace();
            return 0L;
        }
    }
    public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Date newDate = new Date();
        return sdf.format(date);
    }

    public Bitmap stringToBitmap(@Nullable String bitmapStr){
        byte[] decodedString = Base64.decode(bitmapStr, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public /*String*/byte[] DrawableToBytes/*String*/(Drawable d){
        Bitmap bitmap = null;

        if (d instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable)d;
            if(bitmapDrawable.getBitmap() != null) {
                bitmap = bitmapDrawable.getBitmap();
            }
        }
        if (bitmap == null){
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
            //byte[] b = baos.toByteArray();
            //String bitmapString = Base64.encodeToString(b, Base64.DEFAULT);
            //return bitmapString;
        }
    }


}