package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.GeneralUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Map;

import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.INTERNAL_ERROR;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.NOT_A_VALID_PHONE_NUMBER;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.OK;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.USER_IS_BLOCKED;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberPresenter extends BasePresenter implements AdapterView.OnItemSelectedListener, TextWatcher, CompoundButton.OnCheckedChangeListener{

    private static final String TAG = EnterPhoneNumberPresenter.class.getSimpleName();

    @IntDef({OK, NOT_A_VALID_PHONE_NUMBER, INTERNAL_ERROR, USER_IS_BLOCKED})
    public @interface Flows {
        int OK = 200;
        int NOT_A_VALID_PHONE_NUMBER = 400;
        int INTERNAL_ERROR = 500;
        int USER_IS_BLOCKED = 403;
    }

    public static final String PHONE_NUMBER_EXTRA_KEY = "phone_number_for_enter_code_screen";
    public static final String COUNTRY_CODE_EXTRA_KEY = "country_code_for_enter_code_screen";

    private BallabaPhoneNumber phoneNumber;
    private EnterPhoneNumberLayoutBinding binder;
    private Context context;
    private ArrayAdapter<CharSequence> spinnerArrayAdapter;
    private PhoneNumberUtil phoneUtil;

    public EnterPhoneNumberPresenter(Context context, EnterPhoneNumberLayoutBinding binder) {
        this.phoneNumber = new BallabaPhoneNumber(/*spinnerArrayAdapter.getItem(0).toString(), binder.enterPhoneNumberET.getText().toString()*/);
        this.context = context;
        this.binder = binder;
        phoneUtil = PhoneNumberUtil.getInstance();
        initViews();
    }

    private void initViews(){
        spinnerArrayAdapter = ArrayAdapter.createFromResource(context,
                R.array.country_codes, android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binder.countryCodesSpinner.setAdapter(spinnerArrayAdapter);
        binder.countryCodesSpinner.setOnItemSelectedListener(this);

        binder.countryCodesSpinner.setSelection(0);
        binder.enterPhoneNumberET.addTextChangedListener(this);

        binder.enterPhoneNumberCheckbox.setOnCheckedChangeListener(this);
    }

    public BallabaPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    private boolean validatePhoneNumber(BallabaPhoneNumber phoneNumber){
        if(phoneNumber.getFullPhoneNumber().length()>12) {
            try {
                Phonenumber.PhoneNumber targetPN = phoneUtil.parse(phoneNumber.getFullPhoneNumber(), "IL");
                return phoneUtil.isValidNumber(targetPN);
            } catch (NumberParseException ex) {
                return false;
            }
        }else {
            return false;
        }
    }

    private void nextButtonChanger(boolean is){
        if(is){
            binder.enterPhoneNumberNextButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
            binder.enterPhoneNumberNextButton.setAlpha(1f);
            binder.enterPhoneNumberNextButton.setClickable(true);
            binder.enterPhoneNumberNextButton.setTextColor(Color.WHITE);
        }else {
            binder.enterPhoneNumberNextButton.setBackgroundColor(context.getResources().getColor(R.color.gray_button_color, context.getTheme()));
            binder.enterPhoneNumberNextButton.setAlpha(0.50f);
            binder.enterPhoneNumberNextButton.setClickable(false);
            binder.enterPhoneNumberNextButton.setTextColor(Color.BLACK);
        }
    }

    //spinner item listener
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getPhoneNumber().setCountryCode(spinnerArrayAdapter.getItem(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //Enter phone number edit text listeners
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getPhoneNumber().setPhoneNumber(charSequence.toString());
        if(validatePhoneNumber(phoneNumber) && binder.enterPhoneNumberCheckbox.isChecked()){
            nextButtonChanger(true);
        }else {
            nextButtonChanger(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    //Terms of using checked change listener

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(validatePhoneNumber(phoneNumber) && b){
            nextButtonChanger(true);
        }else{
            nextButtonChanger(false);
        }
    }

    public void onNextButtonClick(){
        String deviceId = DeviceUtils.getInstance(true, context).getDeviceId();
        Map<String, String> params = GeneralUtils.getParams(new String[]{"phone", "device_id"}, new String[]{phoneNumber.getFullPhoneNumber(), deviceId});
        Log.d(TAG, "onNextButtonClick");
        ConnectionsManager.getInstance(context).loginWithPhoneNumber(params, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d(TAG, "loginWithPhoneNumber");
                if(entity instanceof BallabaOkResponse){
                    onFlowChanged(Flows.OK);
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                if(entity instanceof BallabaErrorResponse){
                    binder.enterPhoneNumberTextErrorAnswer.setVisibility(View.VISIBLE);
                    Log.d(TAG, "loginWithPhoneNumber rejected "+((BallabaErrorResponse)entity).statusCode);
                    onFlowChanged(((BallabaErrorResponse)entity).statusCode);
                }
            }
        });
    }

    private void onFlowChanged(int statusCode){
        switch (statusCode){
            case Flows.OK :
                Intent enterCode = new Intent(context, EnterCodeActivity.class);
                enterCode.putExtra(COUNTRY_CODE_EXTRA_KEY, phoneNumber.getCountryCode());
                enterCode.putExtra(PHONE_NUMBER_EXTRA_KEY, phoneNumber.getPhoneNumber());
                context.startActivity(enterCode);
                break;

            case Flows.INTERNAL_ERROR:
                binder.enterPhoneNumberTextErrorAnswer.setText(R.string.error_network_internal);
                break;

            case Flows.NOT_A_VALID_PHONE_NUMBER:
                binder.enterPhoneNumberTextErrorAnswer.setText(R.string.error_network_not_valid_phone_number);
                break;

            case Flows.USER_IS_BLOCKED:
                binder.enterPhoneNumberTextErrorAnswer.setText(R.string.error_network_user_blocked);
                break;

            default:
                binder.enterPhoneNumberTextErrorAnswer.setText(R.string.error_network_default);
        }
    }
}
