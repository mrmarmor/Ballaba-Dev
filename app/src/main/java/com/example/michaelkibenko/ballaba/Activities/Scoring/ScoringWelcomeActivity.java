package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Common.FinishActivityReceiver;
import com.example.michaelkibenko.ballaba.R;

public class ScoringWelcomeActivity extends BaseActivity {
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_welcome);

        receiver = new FinishActivityReceiver(this);
        registerReceiver(receiver,
                new IntentFilter(FinishActivityReceiver.ACTION_FINISH_ACTIVITY));
    }

    public void onClickOk(View view){
        startActivity(new Intent(ScoringWelcomeActivity.this, ScoringPersonalDetailsActivity.class));
    }

    public void onClickCancel(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }
}
