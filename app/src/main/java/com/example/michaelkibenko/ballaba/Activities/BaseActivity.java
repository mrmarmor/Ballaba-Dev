package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.R;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class BaseActivity extends AppCompatActivity{

    private static final String TAG = BaseActivity.class.getSimpleName();
    private Snackbar networkErrorSB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }


    protected void showNetworkError(View parentView){
        if(networkErrorSB == null){
            networkErrorSB = Snackbar.make(parentView, getResources().getString(R.string.no_network_err_msg), Snackbar.LENGTH_INDEFINITE);
        }
        networkErrorSB.show();
    }

    protected void hideNetworkError(){
        if(networkErrorSB != null) {
            if (networkErrorSB.isShown()) {
                networkErrorSB.dismiss();
            }else {
                Log.e(TAG, "The network error SnackBar is not shown and you want to dismiss it");
            }
        }else {
            Log.e(TAG, "The network error SnackBar is null and you want to dismiss it");
        }
    }

}