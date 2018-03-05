package com.example.michaelkibenko.ballaba.Presenters;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Common.BallabaSmsListener;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter implements TextWatcher {
    private Context context;
    private EnterCodeLayoutBinding binder;
    public String phoneNumber;
    private StringBuilder sbCode = new StringBuilder(4);
    private EditText[] editTexts;

    public EnterCodePresenter(Context context, EnterCodeLayoutBinding binding, String phoneNumber) {
        this.context = context;
        this.binder = binding;
        this.phoneNumber = phoneNumber;
        editTexts = new EditText[]{binder.enterCodeFirstLeftEditText, binder.enterCodeSecondLeftEditText, binder.enterCodeThirdLeftEditText, binder.enterCodeFourthLeftEditText};

        initEditTexts(editTexts);
        //initReceiver();
    }

    public EnterCodePresenter getInstance(){ return new EnterCodePresenter(context, binder, phoneNumber); }

    public void cancelButtonClicked(){
        ((EnterCodeActivity)context).onBackPressed();
    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        int codeLength = sbCode.length();
        editTexts[codeLength].clearFocus();
        editTexts[codeLength + 1].requestFocus();
        editTexts[codeLength + 1].setCursorVisible(true);
        sbCode.append(c);
        if (codeLength >= 3)
            //TODO send code to server
            Log.e("tag", editTexts[sbCode.length()].getText()+"");
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}

    private void initEditTexts(EditText[] editTexts){
        for (EditText et : editTexts)
            et.addTextChangedListener(this);
    }

}
