package com.example.michaelkibenko.ballaba.Presenters;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IntDef;
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

import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.CODE_EXPIRED;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.INTERNAL_ERROR;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.NOT_A_VALID_PHONE_NUMBER;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.OK;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.USER_IS_BLOCKED;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter implements TextWatcher {

    @IntDef({OK, NOT_A_VALID_PHONE_NUMBER, CODE_EXPIRED, INTERNAL_ERROR, USER_IS_BLOCKED})
    public @interface Flows {
        int OK = 200;
        int NOT_A_VALID_PHONE_NUMBER = 400;
        int CODE_EXPIRED = 401;
        int USER_IS_BLOCKED = 403;
        int INTERNAL_ERROR = 500;
    }

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
