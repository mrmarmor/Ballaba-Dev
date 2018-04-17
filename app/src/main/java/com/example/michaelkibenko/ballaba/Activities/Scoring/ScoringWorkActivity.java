package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoard;
import com.example.michaelkibenko.ballaba.R;

public class ScoringWorkActivity extends AppCompatActivity {
    private final String TAG = ScoringWorkActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_work);

        Log.d(TAG, getIntent().getStringExtra("status")+":"+getIntent().getStringExtra("car"));
    }
}
