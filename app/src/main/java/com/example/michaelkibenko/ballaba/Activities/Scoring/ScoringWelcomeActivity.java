package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoard;
import com.example.michaelkibenko.ballaba.R;

public class ScoringWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_welcome);
    }

    public void onClickOk(View view){
        startActivity(new Intent(ScoringWelcomeActivity.this, ScoringPersonalActivity.class));
    }

    public void onClickCancel(View view){
        finish();
    }
}
