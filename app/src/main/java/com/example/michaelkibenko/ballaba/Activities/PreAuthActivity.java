package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.michaelkibenko.ballaba.Presenters.PreAuthPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PreAuthLayoutBinding;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class PreAuthActivity extends BaseActivity {

    private PreAuthLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.pre_auth_layout);
        binder.setPresenter(new PreAuthPresenter(this));
    }
}
