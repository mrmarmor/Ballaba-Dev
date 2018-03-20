package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 06/03/2018.
 */

public class GeneralUtils {
    private final static String TAG = GeneralUtils.class.getSimpleName();

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
}
