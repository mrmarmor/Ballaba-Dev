package com.example.michaelkibenko.ballaba.Activities.PromiseAgreementActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.R;

public class PromiseCanceletionActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promise_cancelation_activity);

        Intent intent = getIntent();
        int propID = intent.getIntExtra("ID" , -1);
        Toast.makeText(this, propID + " ", Toast.LENGTH_SHORT).show();
    }
}
