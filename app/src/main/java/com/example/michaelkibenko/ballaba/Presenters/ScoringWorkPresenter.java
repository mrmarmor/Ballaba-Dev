package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.Entities.ScoringUserData;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.GeneralUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringWorkBinding;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.json.JSONObject;


/**
 * Created by User on 18/04/2018.
 */

public class ScoringWorkPresenter implements RadioButton.OnClickListener
        , EditText.OnFocusChangeListener {

    private static final long SUCCESS_DELAY_TIME = 3000;
    public static String TAG = ScoringWorkPresenter.class.getSimpleName();

    private @interface SCREEN_STATES {
        int EMPTY = 1;
        int GUARANTOR = 2;
        int MORE_DATA = 3;
        int SUCCESS = 4;
        int GUARANTOR_SUCCESS = 5;
    }

    private AppCompatActivity activity;
    private ActivityScoringWorkBinding binder;
    private ViewGroup root;
    private RadioButton workStatusSelected, senioritySelected;
    private boolean workStatusChecked, seniorityChecked, incomeChecked;
    public static String WORK_STATUS = "WORK",
            WORK_SENIORITY = "SENIORITY",
            WORK_INCOME = "INCOME";

    private JSONObject object;
    private String personalDate, personalFamilyStatus, personalCar, persoanlChilds, personalWorkStatus;
    private @SCREEN_STATES int currentState = SCREEN_STATES.EMPTY;
    private ConstraintLayout emptyTransition, guarantorTransition, moreDataTransition, successTransition;
    private LayoutInflater inflater;
    private boolean isSecondTime;

    public ScoringWorkPresenter(final AppCompatActivity activity, ActivityScoringWorkBinding binding) {
        this.activity = activity;
        this.binder = binding;
        this.root = binder.scoringWorkRoot;

        initButtons();

        binder.scoringSiteEditText.setOnFocusChangeListener(this);
        binder.scoringEmailEditText.setOnFocusChangeListener(this);
        binder.scoringWorkIncomeEditText.setOnFocusChangeListener(this);
        inflater = LayoutInflater.from(activity);
        emptyTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_scoring_work, null, false);
        guarantorTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_scoring_work_guarantor_transition_layout, null, false);
        moreDataTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_scoring_work_more_transition_layout, null, false);
        successTransition = (ConstraintLayout) inflater.inflate(R.layout.activity_scoring_work_success_transition_layout, null, false);
    }

    private void changeScreenState(@SCREEN_STATES int newState){
        if(newState != this.currentState){
            this.currentState = newState;
            onScreenStateChanged();
        }
    }

    private void onScreenStateChanged(){
        ConstraintSet set = new ConstraintSet();
        if(this.currentState == SCREEN_STATES.EMPTY){
            set.clone(emptyTransition);
            binder.scoringWorkBottomAlphaView.setVisibility(View.GONE);
        }else if(this.currentState == SCREEN_STATES.GUARANTOR){
            set.clone(guarantorTransition);
            binder.scoringWorkBottomAlphaView.setVisibility(View.VISIBLE);
        }else if(this.currentState == SCREEN_STATES.GUARANTOR_SUCCESS){
            //TODO change copy and BL
            set.clone(successTransition);
            binder.scoringWorkBottomAlphaView.setVisibility(View.VISIBLE);
        }else if(this.currentState == SCREEN_STATES.MORE_DATA){
            set.clone(moreDataTransition);
            binder.scoringWorkBottomAlphaView.setVisibility(View.VISIBLE);
        }else if(this.currentState == SCREEN_STATES.SUCCESS){
            set.clone(successTransition);
            binder.scoringWorkBottomAlphaView.setVisibility(View.VISIBLE);
        }
        TransitionManager.beginDelayedTransition((ViewGroup) binder.getRoot());
        set.applyTo((ConstraintLayout) binder.getRoot());

    }

    private void initButtons() {
        /*for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof RadioGroup) {
                for (int j = 0; j < ((RadioGroup)v).getChildCount(); j++){
                    View button = ((RadioGroup)v).getChildAt(j);
                    if (button instanceof RadioButton){
                        buttonStateChanger((RadioButton)button);
                        button.setOnClickListener(this);
                    }
                }
            }
        }*/
        binder.scoringWorkSeniorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    buttonStateChanger(button);
                    senioritySelected = group.findViewById(group.getCheckedRadioButtonId());
                    seniorityChecked = group.getCheckedRadioButtonId() != -1;
                }
            }
        });

        binder.scoringWorkStatusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    buttonStateChanger(button);
                    RadioButton radioButton = group.findViewById(group.getCheckedRadioButtonId());
                    binder.scoringWorkSeniorityContainer.setVisibility(radioButton.getText().equals("בין עבודות") ? View.GONE : View.VISIBLE);
                    workStatusSelected = group.findViewById(group.getCheckedRadioButtonId());
                    workStatusChecked = group.getCheckedRadioButtonId() != -1;
                }
            }
        });

        binder.scoringWorkOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOk(true);
            }
        });

        binder.moreNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailAddressValid(false) || isWebsiteNameValid(false)){
                    isSecondTime = true;
                    onClickOk(false);
                }else{
                    //TODO set error
                }
            }
        });

        binder.guarantorNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteGuarantor();
            }
        });
    }

    private void checkFieldsInserted() {
        incomeChecked = !binder.scoringWorkIncomeEditText.getText().toString().isEmpty();
        // toggle income edit text border (for error - if empty)
        /*binder.scoringWorkIncomeEditText.
                setBackgroundTintList(incomeChecked ? ColorStateList.valueOf(activity.getResources().getColor(R.color.colorPrimary)) : ColorStateList.valueOf(activity.getResources().getColor(R.color.red_error_phone)));
        */
        if (workStatusChecked && seniorityChecked && incomeChecked) {
            UiUtils.instance(true, activity).buttonChanger(binder.scoringWorkOkButton, true);
        } else if (!workStatusChecked) {
            binder.scoringWorkScrollView.fullScroll(View.SCROLL_INDICATOR_TOP);
        } else {
            UiUtils.instance(true, activity).buttonChanger(binder.scoringWorkOkButton, false);
        }
        binder.scoringWorkStatusRadioGroup.setBackgroundResource(workStatusChecked ? R.drawable.white_border : R.drawable.radio_group_error_border);
    }

    @Override
    public void onClick(@NonNull final View button) {
        ((RadioButton) button).setChecked(true);
        for (int i = 0; i < ((RadioGroup) button.getParent()).getChildCount(); i++) {
            View view = ((RadioGroup) button.getParent()).getChildAt(i);
            if (view instanceof RadioButton) {
                buttonStateChanger((RadioButton) view);
            }
        }
    }

    private void buttonStateChanger(@NonNull final RadioButton button) {
        Resources res = activity.getResources();
        if (button.isChecked()) {
            button.setTextColor(res.getColor(android.R.color.white, activity.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, activity.getTheme()));
            checkFieldsInserted();
        } else {
            button.setTextColor(res.getColor(R.color.black, activity.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, activity.getTheme()));
            checkFieldsInserted();
        }
    }

    public void onClickOk(boolean isFirst) {

        // permission fields
        /*if (!isWebsiteNameValid()) {
            displayError("website");
            return;
        }
        if (!isEmailAddressValid()){
            displayError("email");
            return;
        }*/
        checkFieldsInserted();
        binder.scoringWorkStatusRadioGroup.setBackgroundResource(workStatusChecked ? R.drawable.white_border : R.drawable.radio_group_error_border);
        binder.scoringWorkSeniorityRadioGroup.setBackgroundResource(seniorityChecked ? R.drawable.white_border : R.drawable.radio_group_error_border);

        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {

            personalDate = activity.getIntent().getStringExtra(ScoringPersonalPresenter.PERSONAL_DATE);
            personalFamilyStatus = activity.getIntent().getStringExtra(ScoringPersonalPresenter.PERSONAL_FAMILY_STATUS);
            persoanlChilds = activity.getIntent().getStringExtra(ScoringPersonalPresenter.PERSOANL_CHILDS);
            personalCar = activity.getIntent().getStringExtra(ScoringPersonalPresenter.PERSONAL_CAR);
            personalWorkStatus = workStatusSelected.getText().toString().trim();


            //TODO send data to server
            convertUserDataToEnglish(personalFamilyStatus, personalWorkStatus);
            ScoringUserData userData = new ScoringUserData();

            userData.setBirthDate(BallabaUserManager.getInstance().getUser().getBirth_date());
            userData.setHasCar(personalCar.equals("יש") ? true : false);
            userData.setFamilyStatus(personalFamilyStatus);
            userData.setWorkStatus(personalWorkStatus);
            userData.setWorkSeniority(senioritySelected.getText().equals("יותר משנה") ? true : false);
            userData.setUserIncome(Integer.parseInt(binder.scoringWorkIncomeEditText.getText().toString().trim()));
            userData.setWorkEmail(isFirst ? binder.scoringSiteEditText.getText().toString().trim() : binder.scoringWorkMoreWorkEditText.getText().toString().trim());
            userData.setUserEmail(isFirst ? binder.scoringEmailEditText.getText().toString().trim() : binder.scoringWorkMoreEmailEditText.getText().toString().trim());
            userData.setBirthDate(BallabaUserManager.getInstance().getUser().getBirth_date());

            sendScoringData(userData);

        } else {
            Toast.makeText(activity, "Error: No data exists", Toast.LENGTH_LONG).show();
        }
        //activity.finish();
        //activity.sendBroadcast(new Intent(FinishActivityReceiver.ACTION_FINISH_ACTIVITY));
    }

    private void convertUserDataToEnglish(String familyStatus, String workStatus) {
        switch (familyStatus) {
            case "רווק/ה":
                personalFamilyStatus = "single";
                break;
            case "נשוי/ה":
                personalFamilyStatus = "married";
                break;
            case "גרוש/ה":
                personalFamilyStatus = "divorced";
                break;
            case "אלמן/ה":
                personalFamilyStatus = "widowed";
                break;
        }
        switch (workStatus) {
            case "עצמאי/ת":
                personalWorkStatus = "freelance";
                break;
            case "שכיר/ה":
                personalWorkStatus = "employee";
                break;
            case "גם וגם":
                personalWorkStatus = "combined";
                break;
            case "בין עבודות":
                personalWorkStatus = "unemployed";
                break;
        }
    }

    private void sendScoringData(ScoringUserData userData) {
        ConnectionsManager.getInstance(activity).sendScoringData(userData, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                //hide progressbar
                checkPropertyPermission();
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                //hide progressbar
                //show error snackbar
                //((BaseActivity)activity).getDefaultSnackBar(null, null, true).show();\
                Toast.makeText(activity, "REJECT", Toast.LENGTH_SHORT).show();

                // TODO: 06/06/2018 DELETE ->> TESTING
//                Intent intent = new Intent(activity, PropertyDescriptionActivity.class);
//                intent.putExtra("Prop" , BallabaUserManager.getInstance().getUser().userCurrentPropertyObservedID);
//                activity.startActivity(intent);
            }
        });
    }

    private void checkPropertyPermission(){
        ConnectionsManager.getInstance(activity).getPropertyPermission(BallabaUserManager.getInstance().getUser().userCurrentPropertyObservedID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                changeScreenState(SCREEN_STATES.SUCCESS);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(activity, PropertyDescriptionActivity.class);
                        intent.putExtra("Prop" , BallabaUserManager.getInstance().getUser().userCurrentPropertyObservedID);
                        activity.startActivity(intent);
                    }
                }, SUCCESS_DELAY_TIME);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                if(((BallabaErrorResponse)entity).statusCode == 403){
                    if((isEmailAddressValid(true) && isWebsiteNameValid(true)) || isSecondTime){
                        changeScreenState(SCREEN_STATES.GUARANTOR);
                    }else{
                        changeScreenState(SCREEN_STATES.MORE_DATA);
                    }
                }
            }
        });
    }

    private void inviteGuarantor(){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        BallabaPhoneNumber phoneNumber = new BallabaPhoneNumber();
        phoneNumber.setCountryCode("+"+binder.scoringWorkGuarantorCountryCodePicker.getSelectedCountryCode());
        phoneNumber.setPhoneNumber(binder.scoringWorkGuarantorPhoneNumberEditText.getText().toString());
        if(GeneralUtils.instance(true, activity).validatePhoneNumber(phoneNumber, phoneUtil)){
            ConnectionsManager.getInstance(activity).inviteGuarantor(phoneNumber.getFullPhoneNumber(), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    changeScreenState(SCREEN_STATES.GUARANTOR_SUCCESS);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        }
                    }, SUCCESS_DELAY_TIME);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    ((BaseActivity)activity).getDefaultSnackBar(binder.getRoot(), activity.getResources().getString(R.string.error_network_internal), false);
                }
            });
        }else{
            binder.scoringWorkGuarantorPhoneNumberEditTextLayout.setError(activity.getResources().getString(R.string.error_network_not_valid_phone_number));
        }
    }

    private boolean isWebsiteNameValid(boolean isFirst) {
        CharSequence website = isFirst ? binder.scoringSiteEditText.getText() : binder.scoringWorkMoreWorkEditText.getText();
        return Patterns.WEB_URL.matcher(website).matches();
    }

    private boolean isEmailAddressValid(boolean isFirst) {
        CharSequence email = isFirst ? binder.scoringEmailEditText.getText() : binder.scoringWorkMoreEmailEditText.getText();
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void displayError(String message) {
        Toast.makeText(activity, "Invalid " + message, Toast.LENGTH_SHORT).show();
    }

    //hiding softKeyboard when user taps out of edit text
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            UiUtils.instance(true, activity).hideSoftKeyboard(v);
            checkFieldsInserted();
        }
    }

    public void onClickBack() {
        activity.finish();
    }

}