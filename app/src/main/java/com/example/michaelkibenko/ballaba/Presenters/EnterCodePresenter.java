package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter {
    private Context context;
    private EnterCodeLayoutBinding binder;
    public String phoneNumber;
    public EnterCodePresenter(Context context, EnterCodeLayoutBinding binding, String phoneNumber) {
        this.context = context;
        this.binder = binding;
        this.phoneNumber = phoneNumber;
    }

    public void cancelButtonClicked(){
        ((EnterCodeActivity)context).onBackPressed();
    }
}
