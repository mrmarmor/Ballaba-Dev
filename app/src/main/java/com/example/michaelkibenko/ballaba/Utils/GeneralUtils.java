package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 06/03/2018.
 */

public class GeneralUtils {
    private final static String TAG = GeneralUtils.class.getSimpleName();

    private static GeneralUtils instance;
    private Context ctx;

    public static GeneralUtils instance(boolean isOnce, Context context){
        if(!isOnce) {
            if (instance == null) {
                instance = new GeneralUtils(context);
            }
            return instance;
        }
        return new GeneralUtils(context);
    }

    private GeneralUtils(Context context){
        this.ctx = context;
    }

    public static Map<String, String> getParams(final String[] KEYS, final String[] VALUES) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < KEYS.length; i++)
            params.put(KEYS[i], VALUES[i]);

        return params;
    }

    public static String getMatadataFromManifest(Context context, String key){
        String mApiKey = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            mApiKey = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        return mApiKey;
    }

    public boolean validatePhoneNumber(BallabaPhoneNumber phoneNumber, PhoneNumberUtil phoneUtil) {
        if (phoneNumber.getFullPhoneNumber().length() > 12) {
            try {
                Phonenumber.PhoneNumber targetPN = phoneUtil.parse(phoneNumber.getFullPhoneNumber(), "IL");
                return phoneUtil.isValidNumber(targetPN);
            } catch (NumberParseException ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static byte[] rotateImage(byte[] data, float angle) {
        Bitmap source = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap returable = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        returable.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        returable.recycle();
        return byteArray;
    }
}
