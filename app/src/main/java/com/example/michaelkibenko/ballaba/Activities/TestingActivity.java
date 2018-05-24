package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringCameraActivity;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.TestingLayoutBinding;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class TestingActivity extends BaseActivity {

    private TestingLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.testing_layout);
        binder.setPresenter(new TestingPresenter(this));

        findViewById(R.id.camera_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingActivity.
                        this , ScoringCameraActivity.class));
            }
        });

        findViewById(R.id.myProperty_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingActivity.this , MyPropertiesBaseActivity.class));
            }
        });
    }
}
