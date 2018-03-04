package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodeActivity extends BaseActivity {

    private EnterCodeLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_code_layout);
        String phoneNumber = getIntent().getStringExtra(EnterPhoneNumberPresenter.PHONE_NUMBER_EXTRA_KEY);
        binder.setPresenter(new EnterCodePresenter(this, binder, phoneNumber));

    }
}