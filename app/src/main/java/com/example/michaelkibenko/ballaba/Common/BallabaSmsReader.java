/*
package com.example.michaelkibenko.ballaba.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.michaelkibenko.ballaba.BallabaApplication;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * Created by User on 05/03/2018.
 *//*


public class BallabaSmsReader {
    private static BallabaSmsReader instance;

    private boolean status;

    private Context context;

    //private ArrayList<BallabaSmsListener> clients;

    public static BallabaSmsReader getInstance() {
        if(instance == null){
            instance = new BallabaSmsReader(BallabaApplication.getAppContext());//TODO this context is null
        }
        return instance;
    }

    private BallabaSmsReader(Context context){
        clients = new ArrayList<>();
        this.context = context;
        context.registerReceiver(new BallabaSmsReader.SmsReceiver(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    public boolean getStatus(){
        return this.status;
    }

    public void register(BallabaSmsListener client){
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



    class SmsReceiver extends BroadcastReceiver {
        private BallabaSmsListener mListener;
        public Pattern p = Pattern.compile("(|^)\\d{6}");

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    //String sender = smsMessage.getDisplayOriginatingAddress();
                    //String phoneNumber = smsMessage.getDisplayOriginatingAddress();
                    //String senderNum = phoneNumber;
                    String messageBody = smsMessage.getMessageBody();
                    try {
                        if (messageBody != null) {
                            Matcher m = p.matcher(messageBody);
                            if (m.find()) {
                                mListener.messageReceived(m.group(0));
                            } else {
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        public void bindListener(BallabaSmsListener listener) {
            mListener = listener;
        }
    }
}
*/
