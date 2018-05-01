package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.michaelkibenko.ballaba.BallabaApplication;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.google.gson.Gson;

/**
 * Created by michaelkibenko on 18/02/2018.
 */

public class SharedPreferencesManager {
    private static String SharedPreferencesKey = "Ballaba";
    public static SharedPreferencesManager instance;
    private Context context;
    private SharedPreferences preferences;

    public static SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context    /*BallabaApplication.getAppContext()/*this returns null!!!*/);
        }
        return instance;
    }

    private SharedPreferencesManager(Context context) {
        this.context = context;
        this.preferences = context.getApplicationContext().getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    public boolean putString(@SharedPreferencesKeysHolder String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    public boolean removeString(final @SharedPreferencesKeysHolder String KEY) {
        return getEditor().remove(KEY).commit();
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

    public String getString(@SharedPreferencesKeysHolder String key, String defValue) {
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

    public boolean putUser(@SharedPreferencesKeysHolder String key, BallabaUser user) {
        String json = new Gson().toJson(user);
        return getEditor().putString(key, json).commit();
    }

    public BallabaUser getUser(@SharedPreferencesKeysHolder String key, BallabaUser userDef){
        String json = getString(SharedPreferencesKeysHolder.USER, "");
        BallabaUser user = new Gson().fromJson(json, BallabaUser.class);
        return user == null? userDef : user;
    }

    public boolean putGalleryViewType(String viewType){
        return putString(SharedPreferencesKeysHolder.GALLERY_VIEWTYPE,viewType);
    }

    public String getGalleryViewType(String defaultValue){
        return getString(SharedPreferencesKeysHolder.GALLERY_VIEWTYPE,defaultValue);
    }
}
