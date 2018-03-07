package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberActivity extends BaseActivity {
    private BallabaConnectivityListener client;
    private EnterPhoneNumberLayoutBinding binder;
    private EnterPhoneNumberPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.enter_phone_number_layout);
        presenter = new EnterPhoneNumberPresenter(this,binder);
        binder.setPresenter(presenter);

        client = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if (!is)
                    //TODO replace next line with a dialog
                    Toast.makeText(EnterPhoneNumberActivity.this, "no network", Toast.LENGTH_LONG).show();
            }
        };
        BallabaConnectivityAnnouncer.getInstance(this).register(client);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BallabaConnectivityAnnouncer.getInstance(this).unRegister(client);
    }
}
