package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.michaelkibenko.ballaba.Presenters.ScoringWorkPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringWorkBinding;

public class ScoringWorkActivity extends AppCompatActivity {
    private final String TAG = ScoringWorkActivity.class.getSimpleName();

    private ActivityScoringWorkBinding binder;
    private ScoringWorkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_scoring_work);
        presenter = new ScoringWorkPresenter(this, binder);
        binder.setPresenter(presenter);

        Log.d(TAG, getIntent().getStringExtra("status")+":"+getIntent().getStringExtra("car"));
    }

 /*   public void onScoringButtonClick(View v){
        Toast.makeText(this, "on click1", Toast.LENGTH_SHORT).show();
    }*/

    /*public void onClickBack(View view){
        finish();
    }*/

}