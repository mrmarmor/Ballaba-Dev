package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.R;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class BaseActivity extends AppCompatActivity{

    private static final String TAG = BaseActivity.class.getSimpleName();
    private Snackbar defaultSnackBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }


    protected void showNetworkError(View parentView){
        getDefaultSnackBar(parentView, getResources().getString(R.string.no_network_err_msg), true).show();
    }

    protected void hideNetworkError(){
        if(defaultSnackBar != null) {
            if (defaultSnackBar.isShown()) {
                defaultSnackBar.dismiss();
            }else {
                Log.e(TAG, "The network error SnackBar is not shown and you want to dismiss it");
            }
        }else {
            Log.e(TAG, "The network error SnackBar is null and you want to dismiss it");
        }
    }

    protected Snackbar getDefaultSnackBar(View parentView, String text, boolean isShowAlways){
        if(defaultSnackBar == null){
            defaultSnackBar = Snackbar.make(parentView, text, isShowAlways?Snackbar.LENGTH_INDEFINITE:Snackbar.LENGTH_LONG);
            View snackBarView = defaultSnackBar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
            ((TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text)).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }else {
            defaultSnackBar.setText(text);
        }
        return defaultSnackBar;
    }
}