package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.TestingLayoutBinding;
import io.fabric.sdk.android.Fabric;

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
    }
}
