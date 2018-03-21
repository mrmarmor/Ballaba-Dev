package com.example.michaelkibenko.ballaba.Presenters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;
import com.example.michaelkibenko.ballaba.Activities.TermsOfUseActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
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
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.INTERNAL_ERROR;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.NOT_A_VALID_PHONE_NUMBER;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.OK;
import static com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter.Flows.USER_IS_BLOCKED;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberPresenter extends BasePresenter implements AdapterView.OnItemSelectedListener, TextWatcher
        , CompoundButton.OnCheckedChangeListener, EditText.OnFocusChangeListener{

    private static final String TAG = EnterPhoneNumberPresenter.class.getSimpleName();
    private boolean wasConnectivityProblemm;

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
    public BallabaConnectivityListener connectivityListener;


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
        binder.enterPhoneNumberET.setOnFocusChangeListener(this);
        binder.enterPhoneNumberET.requestFocus();

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

    //spinner item listener
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getPhoneNumber().setCountryCode(spinnerArrayAdapter.getItem(i).toString());
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    //hiding softKeyboard when user taps out of edit text
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            UiUtils.instance(true, context).hideSoftKeyboard(v);
        }
    }

    //Enter phone number edit text listeners
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getPhoneNumber().setPhoneNumber(charSequence.toString());
        boolean canGoOn = validatePhoneNumber(phoneNumber) && binder.enterPhoneNumberCheckbox.isChecked();
        UiUtils.instance(true, context).buttonChanger(binder.enterPhoneNumberNextButton, canGoOn);

    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}

    //Terms of using checked change listener
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isButtonChecked) {
        boolean canGoOn = validatePhoneNumber(phoneNumber) && isButtonChecked;
        UiUtils.instance(true, context).buttonChanger(binder.enterPhoneNumberNextButton, canGoOn);
    }
    public void onTermsOfUseClick(){
        context.startActivity(new Intent(context, TermsOfUseActivity.class));
    }

    private void listenToNetworkChanges(){
        connectivityListener = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if (!is){
                    wasConnectivityProblemm = true;
                    ((BaseActivity)context).showNetworkError(binder.getRoot());
                }else if(wasConnectivityProblemm){
                    ((BaseActivity)context).hideNetworkError();
                    sendPhoneNumber();
                }

            }
        };
        BallabaConnectivityAnnouncer.getInstance(context).register(connectivityListener);
    }

    public void sendPhoneNumber(){
        if(BallabaConnectivityAnnouncer.getInstance(context).isConnected()) {

            ((BaseActivity)context).getDefaultSnackBar(binder.getRoot(), context.getString(R.string.enter_phone_number_snackBar_text), true);

            circleProgressBarChanger(true);
            UiUtils.instance(true, context).buttonChanger(binder.enterPhoneNumberNextButton, false);
            UiUtils.instance(true, context).hideSoftKeyboard(((Activity) context).getWindow().getDecorView());

            ConnectionsManager.getInstance(context).loginWithPhoneNumber(phoneNumber.getFullPhoneNumber(), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Log.d(TAG, "loginWithPhoneNumber");
                    if (entity instanceof BallabaOkResponse) {
                        //if the request completed some milliseconds before the connection time out
                        wasConnectivityProblemm = false;
                        onFlowChanged(Flows.OK);
                    }
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    if (entity instanceof BallabaErrorResponse) {
                        //TODO replace next line with snackbar
                        binder.enterPhoneNumberTextErrorAnswer.setVisibility(View.VISIBLE);
                        Log.d(TAG, "loginWithPhoneNumber rejected " + ((BallabaErrorResponse) entity).statusCode);
                        onFlowChanged(((BallabaErrorResponse) entity).statusCode);
                    }
                }
            });
        }else {
            ((BaseActivity)context).showNetworkError(binder.getRoot());
            listenToNetworkChanges();
        }
    }

    private void onFlowChanged(int statusCode){
        switch (statusCode){
            case Flows.OK :
                circleProgressBarChanger(false);
                UiUtils.instance(true, context).buttonChanger(binder.enterPhoneNumberNextButton, true);

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

    private void circleProgressBarChanger(boolean isShow){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)binder.enterPhoneNumberNextButton.getLayoutParams();

        if (isShow) {
            params.width = 120;
            binder.enterPhoneNumberNextButton.setLayoutParams(params);
            binder.enterPhoneNumberNextButton.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            binder.enterPhoneNumberNextButton.setPaddingRelative((int)context.getResources().getDimension(R.dimen.small_margin), 0, 0, 0);

            binder.enterPhoneNumberNextButtonProgress.setVisibility(View.VISIBLE);
            binder.enterPhoneNumberNextButtonProgress.getIndeterminateDrawable().setColorFilter(
                    context.getResources().getColor(R.color.colorPrimary, context.getTheme()), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            params.width = 105;
            binder.enterPhoneNumberNextButton.setLayoutParams(params);
            binder.enterPhoneNumberNextButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            binder.enterPhoneNumberNextButtonProgress.setVisibility(View.GONE);
        }
    }

}
