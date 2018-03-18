package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SplashLayoutBinding;

import static com.example.michaelkibenko.ballaba.Activities.SplashActivity.FLOW_TYPES.AUTHENTICATED;
import static com.example.michaelkibenko.ballaba.Activities.SplashActivity.FLOW_TYPES.NEED_AUTHENTICATION;

/**
 * Created by michaelkibenko on 19/02/2018.
 */

public class SplashActivity extends BaseActivity {

    @IntDef({AUTHENTICATED, NEED_AUTHENTICATION})
    public @interface FLOW_TYPES {
        int AUTHENTICATED = 1;
        int NEED_AUTHENTICATION = 0;
    }

    private static final String TAG = SplashActivity.class.getSimpleName();
    private BallabaConnectivityListener connectivityListener;
    private static final long MIN_SPLASH_DELAY = 3000;
    private SplashLayoutBinding binder;
    private long startTime,endTime;
    boolean isGetConfig, isLoggedIn, wasConnectivityProblem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.splash_layout);
        connectivityListener = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if(!is){
                    wasConnectivityProblem = true;
                    showNetworkError(binder.getRoot());
                }else if(wasConnectivityProblem){
                    hideNetworkError();
                    if(!isGetConfig){
                        getConfigRequestAndAuthenticate();
                    }else if(!isLoggedIn){
                        logInWithToken();
                    }
                }
            }
        };
        BallabaConnectivityAnnouncer.getInstance(this).register(connectivityListener);

        startTime = System.currentTimeMillis();
        if(BallabaConnectivityAnnouncer.getInstance(this).isConnected()){
            getConfigRequestAndAuthenticate();
        }
        else {
            showNetworkError(binder.getRoot());
        }
    }

    private void getConfigRequestAndAuthenticate(){
        ConnectionsManager.getInstance(this).getConfigRequest(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                isGetConfig = true;
                Log.d(TAG, "getConfig");
                logInWithToken();
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Toast.makeText(SplashActivity.this, "Here will be error dialog", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logInWithToken(){
        String token = SharedPreferencesManager.getInstance(SplashActivity.this).getString(SharedPreferencesKeysHolder.GLOBAL_TOKEN, null);
        if(token != null){
            ConnectionsManager.getInstance(SplashActivity.this).logInByToken(new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Log.d(TAG, "logInWithToken");
                    checkSplashDelay(FLOW_TYPES.AUTHENTICATED);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    Log.d(TAG, "logInWithToken rejected");
                    if(entity instanceof BallabaErrorResponse){
                        if(((BallabaErrorResponse)entity).statusCode != 500){
                            checkSplashDelay(FLOW_TYPES.NEED_AUTHENTICATION);
                        }
                    }
                }
            }, token);
        }else{
            checkSplashDelay(FLOW_TYPES.NEED_AUTHENTICATION);
            //first from here
        }
    }

    private void checkSplashDelay(final @FLOW_TYPES int what){
        Log.d(TAG, "checkSplashDelay");
        endTime = System.currentTimeMillis();
        if(endTime - startTime > MIN_SPLASH_DELAY){
            continueFlow(what);
        }else if(isGetConfig){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "checkSplashDelay in handler");
                    continueFlow(what);
                }
            }, endTime - startTime - MIN_SPLASH_DELAY);
        }
    }

    private void continueFlow(@FLOW_TYPES int what){
        Log.d(TAG, "continueFlow with " +what);
        Intent start;
        if(what == FLOW_TYPES.NEED_AUTHENTICATION) {
            start = new Intent(SplashActivity.this, EnterPhoneNumberActivity.class);
        }else if(what == FLOW_TYPES.AUTHENTICATED){
            start = new Intent(SplashActivity.this, TestingActivity.class);
        }else{
            start = new Intent(SplashActivity.this, EnterPhoneNumberActivity.class);
        }

        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(start);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        BallabaConnectivityAnnouncer.getInstance(this).unRegister(connectivityListener);
    }
}
