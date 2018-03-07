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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Common.BallabaSmsListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Holders.GlobalValues;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.GeneralUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import java.util.HashMap;
import java.util.Map;

import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.CODE_EXPIRED;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.INTERNAL_ERROR;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.NOT_A_VALID_PHONE_NUMBER;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.OK;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.USER_IS_BLOCKED;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter implements TextWatcher {
    private static String TAG = EnterCodePresenter.class.getSimpleName();

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
    public BallabaPhoneNumber phoneNumber;
    private StringBuilder sbCode = new StringBuilder(4);
    private EditText[] editTexts;

    public EnterCodePresenter(Context context, EnterCodeLayoutBinding binding, String countryCode, String phoneNumber) {
        this.context = context;
        this.binder = binding;
        this.phoneNumber = new BallabaPhoneNumber(phoneNumber, countryCode);
        editTexts = new EditText[]{binder.enterCodeFirstLeftEditText, binder.enterCodeSecondLeftEditText, binder.enterCodeThirdLeftEditText, binder.enterCodeFourthLeftEditText};

        initEditTexts(editTexts);
    }

    public EnterCodePresenter getInstance() {
        return new EnterCodePresenter(context, binder, phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber());
    }

    public void cancelButtonClicked() {
        ((EnterCodeActivity) context).onBackPressed();
    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        sbCode.append(c);
        int codeLength = sbCode.length();
        if (codeLength < 4) {
            editTexts[codeLength - 1].clearFocus();
            editTexts[codeLength].requestFocus();
            editTexts[codeLength].setCursorVisible(true);
        } else {
            onCodeCompleted();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}

    private void initEditTexts(EditText[] editTexts) {
        for (EditText et : editTexts)
            et.addTextChangedListener(this);
    }

    private void onCodeCompleted() {
        Map<String, String> params = GeneralUtils.getParams(new String[]{"phone", "code"}, new String[]{phoneNumber.getFullPhoneNumber(), sbCode.toString()});
        Log.e(TAG, params.toString());

        ConnectionsManager.getInstance(context).enterCode(params, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if (entity instanceof BallabaOkResponse) {
                    onFlowChanged(EnterPhoneNumberPresenter.Flows.OK);
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                if (entity instanceof BallabaErrorResponse) {
                    onFlowChanged(((BallabaErrorResponse) entity).statusCode);
                }
            }
        });
    }

    private void onFlowChanged(int statusCode) {
        switch (statusCode) {
            case Flows.OK:
                Intent intentToMainActivity = new Intent(context, MainActivity.class);
                //intentToMainActivity.putExtra(SOMETHING TO MOVE);
                context.startActivity(intentToMainActivity);
                break;

            case Flows.INTERNAL_ERROR:
                Toast.makeText(context, context.getString(R.string.error_network_internal), Toast.LENGTH_LONG).show();
                break;

            case Flows.NOT_A_VALID_PHONE_NUMBER:
                Toast.makeText(context, context.getString(R.string.error_network_not_valid_phone_number), Toast.LENGTH_LONG).show();
                break;

            case Flows.CODE_EXPIRED:
                Toast.makeText(context, context.getString(R.string.error_network_code_has_expired), Toast.LENGTH_LONG).show();
                break;

            case Flows.USER_IS_BLOCKED:
                Toast.makeText(context, context.getString(R.string.error_network_user_blocked), Toast.LENGTH_LONG).show();
                break;

            default:
                Toast.makeText(context, context.getString(R.string.error_network_default), Toast.LENGTH_LONG).show();
        }
    }
}
