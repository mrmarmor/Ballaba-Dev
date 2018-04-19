package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Common.FinishActivityReceiver;
import com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

public class ScoringPersonalActivity extends BaseActivity {
    private ActivityScoringPersonalBinding binder;
    private ScoringPersonalPresenter presenter;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_scoring_personal);
        presenter = new ScoringPersonalPresenter(this, binder);
        binder.setPresenter(presenter);

        receiver = new FinishActivityReceiver(this);
        registerReceiver(receiver, new IntentFilter(FinishActivityReceiver.ACTION_FINISH_ACTIVITY));
    }

 /*   public void onScoringButtonClick(View v){
        Toast.makeText(this, "on click1", Toast.LENGTH_SHORT).show();
    }*/

    public void onClickBack(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }
}