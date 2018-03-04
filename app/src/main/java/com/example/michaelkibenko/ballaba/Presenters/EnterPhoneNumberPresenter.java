package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.EnterPhoneNumberLayoutBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EnterPhoneNumberPresenter extends BasePresenter implements AdapterView.OnItemSelectedListener, TextWatcher, CompoundButton.OnCheckedChangeListener{
    public static final String PHONE_NUMBER_EXTRA_KEY = "phone_number_for_enter_code_screen";

    private BallabaPhoneNumber phoneNumber;
    private EnterPhoneNumberLayoutBinding binder;
    private Context context;
    private ArrayAdapter<CharSequence> spinnerArrayAdapter;
    private PhoneNumberUtil phoneUtil;


    public EnterPhoneNumberPresenter(Context context, EnterPhoneNumberLayoutBinding binder) {
        this.phoneNumber = new BallabaPhoneNumber();
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
        }else {
            binder.enterPhoneNumberNextButton.setBackgroundColor(context.getResources().getColor(R.color.gray_button_color, context.getTheme()));
            binder.enterPhoneNumberNextButton.setAlpha(0.50f);
            binder.enterPhoneNumberNextButton.setClickable(false);
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
        Intent enterCode = new Intent(this.context, EnterCodeActivity.class);
        enterCode.putExtra(PHONE_NUMBER_EXTRA_KEY, phoneNumber.getFullPhoneNumber());
        context.startActivity(enterCode);
    }
}