package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringPersonalActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWelcomeActivity;
import com.example.michaelkibenko.ballaba.R;

/**
 * Created by User on 23/04/2018.
 */

public class ContinueAddPropertyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_add_property);
    }

    public void onClickOk(View view){
        startActivity(new Intent(this, AddPropertyActivity.class));
    }

    public void onClickCancel(View view){
        finish();
    }
}
