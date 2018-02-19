package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.michaelkibenko.ballaba.BallabaApplication;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;

/**
 * Created by michaelkibenko on 18/02/2018.
 */

public class SharedPreferencesManager {
    private static String SharedPreferencesKey = "Ballaba";
    public SharedPreferencesManager instance;
    private Context context;
    private SharedPreferences preferences;

    public SharedPreferencesManager getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesManager(BallabaApplication.getAppContext());
        }
        return instance;
    }

    private SharedPreferencesManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    private boolean putString(@SharedPreferencesKeysHolder String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    private boolean putInt(@SharedPreferencesKeysHolder String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    private boolean putLong(@SharedPreferencesKeysHolder String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    private boolean putBoolean(@SharedPreferencesKeysHolder String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    private boolean putFloat(@SharedPreferencesKeysHolder String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    private String getString(@SharedPreferencesKeysHolder String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    private int getInt(@SharedPreferencesKeysHolder String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    private long getLong(@SharedPreferencesKeysHolder String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    private boolean getBoolean(@SharedPreferencesKeysHolder String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    private float getFloat(@SharedPreferencesKeysHolder String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    private boolean remove(@SharedPreferencesKeysHolder String key) {
        return getEditor().remove(key).commit();
    }
}
