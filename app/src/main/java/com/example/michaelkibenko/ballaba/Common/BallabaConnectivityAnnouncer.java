package com.example.michaelkibenko.ballaba.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.michaelkibenko.ballaba.BallabaApplication;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by michaelkibenko on 18/02/2018.
 */

public class BallabaConnectivityAnnouncer {
    public static BallabaConnectivityAnnouncer instance;

    private boolean status;

    private Context context;

    private ArrayList<BallabaConnectivityListener> clients;

    public static BallabaConnectivityAnnouncer getInstance() {
        if(instance == null){
            instance = new BallabaConnectivityAnnouncer(BallabaApplication.getAppContext());
        }
        return instance;
    }

    private BallabaConnectivityAnnouncer(Context context){
        clients = new ArrayList<>();
        this.context = context;
        context.registerReceiver(new NetworkChangeReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public boolean getStatus(){
        return this.status;
    }

    public void register(BallabaConnectivityListener client){
        if(clients != null) {
            clients.add(client);
        }
    }

    public void unRegister(BallabaConnectivityListener client){
        if(clients != null){
            if(clients.contains(client)){
                clients.remove(client);
            }
        }
    }

    private void onConnectivityChange(boolean status){
        for (BallabaConnectivityListener client : clients) {
            client.onConnectivityChanged(status);
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isAvailable() || mobile.isAvailable()) {
                status = true;
            }else{
                status = false;
            }

            onConnectivityChange(status);
        }
    }

}
