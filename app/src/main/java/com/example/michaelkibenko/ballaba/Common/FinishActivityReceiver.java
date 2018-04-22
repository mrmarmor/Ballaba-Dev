package com.example.michaelkibenko.ballaba.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.michaelkibenko.ballaba.Presenters.ScoringWorkPresenter;

/**
 * Created by User on 19/04/2018.
 */

public class FinishActivityReceiver extends BroadcastReceiver {
    public final static String ACTION_FINISH_ACTIVITY = "finish_activity";
    private AppCompatActivity activity;

    public FinishActivityReceiver(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_FINISH_ACTIVITY)) {
            activity.finish();
        }
    }
}
