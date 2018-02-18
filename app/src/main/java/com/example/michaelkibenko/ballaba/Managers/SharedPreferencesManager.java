package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michaelkibenko on 18/02/2018.
 */

public class SharedPreferencesManager {
    private static String SharedPreferencesKey = "Ballaba";
    public SharedPreferencesManager instance;
    private Context context;
    private SharedPreferences preferences;

    public SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
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

    private boolean putString(@SharedPreferencesKeys String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    private boolean putInt(@SharedPreferencesKeys String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    private boolean putLong(@SharedPreferencesKeys String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    private boolean putBoolean(@SharedPreferencesKeys String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    private boolean putFloat(@SharedPreferencesKeys String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    private String getString(@SharedPreferencesKeys String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    private int getInt(@SharedPreferencesKeys String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    private long getLong(@SharedPreferencesKeys String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    private boolean getBoolean(@SharedPreferencesKeys String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    private float getFloat(@SharedPreferencesKeys String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    private boolean remove(@SharedPreferencesKeys String key) {
        return getEditor().remove(key).commit();
    }
}
