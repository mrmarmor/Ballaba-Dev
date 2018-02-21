package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberActivity extends BaseActivity {

    private EnterPhoneNumberLayoutBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_phone_number_layout);
    }
}
