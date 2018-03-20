package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;

/**
 * Created by michaelkibenko on 20/03/2018.
 */

public class SearchPropertiesManager {

    private static SearchPropertiesManager instance;
    private Context context;

    public static SearchPropertiesManager getInstance(Context context) {
        if(instance == null){
            instance = new SearchPropertiesManager(context);
        }

        return instance;
    }


    public SearchPropertiesManager(Context context) {
        this.context = context;
    }


}
