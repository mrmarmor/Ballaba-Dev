package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.michaelkibenko.ballaba.BallabaApplication;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class ConnectionsManager {

    private static ConnectionsManager instance;

    private Context context;

    private RequestQueue queue;

    public static ConnectionsManager getInstance() {
        if(instance == null){
            instance = new ConnectionsManager(BallabaApplication.getAppContext());
        }
        return instance;
    }

    private ConnectionsManager(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(this.context);
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
