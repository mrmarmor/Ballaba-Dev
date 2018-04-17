package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

public class ScoringPersonalActivity extends BaseActivity {
    private ActivityScoringPersonalBinding binder;
    private ScoringPersonalPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_scoring_personal);
        presenter = new ScoringPersonalPresenter(this, binder);
        binder.setPresenter(presenter);
    }

}