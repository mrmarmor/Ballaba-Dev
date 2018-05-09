package com.example.michaelkibenko.ballaba.Presenters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWorkActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 17/04/2018.
 */

public class ScoringPersonalPresenter implements RadioButton.OnClickListener {
    private static String TAG = ScoringPersonalPresenter.class.getSimpleName();

    /*@IntDef({CHECKED, UNCHECKED})
    public @interface BUTTON_STATE {
        int CHECKED = 1, UNCHECKED = 0;
    }*/

    //private static ScoringPersonalPresenter instance;
    private AppCompatActivity activity;
    private ActivityScoringPersonalBinding binder;
    private ViewGroup root;
    private Calendar myCalendar;
    private EditText[] editTexts;
    private boolean familyStatusChecked, carStatusChecked , isDateInserted;
    private RadioGroup.OnCheckedChangeListener familyRadioGroupListener, carRadioGroupListener;
    public static String PERSONAL_DATE = "DATE",
            PERSOANL_CHILDS = "CHILDS",
            PERSONAL_CAR = "CAR",
            PERSONAL_FAMILY_STATUS = "FAMILY_STATUS";

    private RadioButton carRBSelected, familyRBSelected;
    private SimpleDateFormat sdf;

    //private Button[] buttons;
    //private @BUTTON_STATE int state;

    public ScoringPersonalPresenter(final AppCompatActivity activity, ActivityScoringPersonalBinding binding) {
        this.activity = activity;
        this.binder = binding;
        this.root = binder.scoringPersonalRoot;

        myCalendar = Calendar.getInstance();
        initButtons();
        initEditTexts();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                onClickOk();
            }
        };

        familyRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                familyRBSelected = group.findViewById(group.getCheckedRadioButtonId());
                familyStatusChecked = group.getCheckedRadioButtonId() != -1;
            }
        };

        carRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                carRBSelected = group.findViewById(group.getCheckedRadioButtonId());
                carStatusChecked = group.getCheckedRadioButtonId() != -1;
            }
        };

        binder.scoringFamilyStatusRadioGroup.setOnCheckedChangeListener(familyRadioGroupListener);
        binder.scoringCarRadioGroup.setOnCheckedChangeListener(carRadioGroupListener);

        binder.scoringPersonalOkButton.setOnClickListener(clickListener);
    }

    private void initEditTexts() {
        editTexts = new EditText[]{binder.scoringDateOfBirthYearEditText,
                binder.scoringDateOfBirthMonthEditText,
                binder.scoringDateOfBirthDayEditText};

        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(false);
            editTexts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();
                }
            });
        }
    }

    private void initButtons() {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof RadioGroup) {
                for (int j = 0; j < ((RadioGroup) v).getChildCount(); j++) {
                    View button = ((RadioGroup) v).getChildAt(j);
                    if (button instanceof RadioButton) {
                        buttonStateChanger((RadioButton) button);
                        button.setOnClickListener(this);
                    }
                }
            }
        }
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
                Log.d(TAG, myCalendar.toString());
            }

        };
        new DatePickerDialog(activity, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateDate() {

        String myFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        String formattedDate = sdf.format(myCalendar.getTime());
        String[] date = formattedDate.split("-");

        String year = date[0];
        String month = date[1];
        String day = date[2];

        Calendar minimumYearsCheckCalendar = Calendar.getInstance();
        minimumYearsCheckCalendar.setTime(new Date());
        minimumYearsCheckCalendar.add(Calendar.YEAR, -18);

        if(myCalendar.before(minimumYearsCheckCalendar)){ // user above 18 years old
            binder.scoringDateOfBirthError.setVisibility(View.INVISIBLE);
            Log.d(TAG, "updateDate: " + year + " " + month + " " + day);
            String[] monthArr = activity.getResources().getStringArray(R.array.months);
            for (int i = 0; i < monthArr.length; i++) {
                if (Integer.parseInt(month) == i) {
                    month = monthArr[i - 1];
                    break;
                }
            }

            editTexts[0].setText(year);
            editTexts[1].setText(month);
            editTexts[2].setText(day);
            validateDateEditTexts();
            checkFieldsInserted();
        }else { // user under 18 years old
            validateDateEditTexts();
            binder.scoringDateOfBirthError.setVisibility(View.VISIBLE);
        }

    }

    private void checkFieldsInserted(){
        for (int i = 0; i < editTexts.length; i++) {
            if (!editTexts[i].getText().toString().isEmpty()){
                isDateInserted = true;
                break;
            }
        }
        if (familyStatusChecked && carStatusChecked && isDateInserted){
            UiUtils.instance(true , activity).buttonChanger(binder.scoringPersonalOkButton , true);
        }
    }

    @Override
    public void onClick(View button) {
        ((RadioButton) button).setChecked(true);
        for (int i = 0; i < ((RadioGroup) button.getParent()).getChildCount(); i++) {
            View view = ((RadioGroup) button.getParent()).getChildAt(i);
            if (view instanceof RadioButton) {
                buttonStateChanger((RadioButton) view);
            }
        }
    }

    private void buttonStateChanger(RadioButton button) {
        Resources res = activity.getResources();
        if (button.isChecked()) {
            button.setTextColor(res.getColor(android.R.color.white, activity.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, activity.getTheme()));
            checkFieldsInserted();
        } else {
            button.setTextColor(res.getColor(R.color.black, activity.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, activity.getTheme()));
        }
    }

    //TODO use UiUtils.buttonChanger to disable button
    public void onClickOk() {

        binder.scoringFamilyStatusRadioGroup.setBackgroundResource(familyStatusChecked ? R.drawable.white_border : R.drawable.radio_group_error_border);
        binder.scoringCarRadioGroup.setBackgroundResource(carStatusChecked ? R.drawable.white_border : R.drawable.radio_group_error_border);

        Intent intent = new Intent(activity, ScoringWorkActivity.class);

        validateDateEditTexts();

        if (familyStatusChecked && carStatusChecked && isDateInserted) {
            intent.putExtra(PERSONAL_CAR, carRBSelected.getText());
            intent.putExtra(PERSONAL_FAMILY_STATUS, familyRBSelected.getText());
            intent.putExtra(PERSONAL_DATE, sdf != null ? sdf.format(myCalendar.getTime()) : null);
            intent.putExtra(PERSOANL_CHILDS, binder.scoringNumberOfChildrenEditText.getText().toString().trim());
            activity.startActivity(intent);
        }
    }

    private void validateDateEditTexts() {
        for (int i = 0; i < editTexts.length; i++) {
            if (!editTexts[i].getText().toString().trim().isEmpty()){
                isDateInserted = true;
                editTexts[i].setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.colorPrimary)));
            }else {
                binder.scoringMainScrollView.fullScroll(View.SCROLL_INDICATOR_TOP);
                editTexts[i].setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.red_error_phone)));
            }
        }
    }

}