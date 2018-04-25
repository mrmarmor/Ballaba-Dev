package com.example.michaelkibenko.ballaba.Presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWorkActivity;
import com.example.michaelkibenko.ballaba.Common.FinishActivityReceiver;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 17/04/2018.
 */

public class ScoringPersonalPresenter implements RadioButton.OnClickListener{
    private static String TAG = ScoringPersonalPresenter.class.getSimpleName();

    /*@IntDef({CHECKED, UNCHECKED})
    public @interface BUTTON_STATE {
        int CHECKED = 1, UNCHECKED = 0;
    }*/

    //private static ScoringPersonalPresenter instance;
    private AppCompatActivity activity;
    private ActivityScoringPersonalBinding binder;
    private ViewGroup root;
    //private Button[] buttons;
    //private @BUTTON_STATE int state;

    public ScoringPersonalPresenter(AppCompatActivity activity, ActivityScoringPersonalBinding binding) {
        this.activity = activity;
        this.binder = binding;
        this.root = binder.scoringPersonalRoot;

        initButtons();
        setSpinnerYears(new ArrayList<String>());
    }

    private void initButtons(){
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof RadioGroup) {
                for (int j = 0; j < ((RadioGroup)v).getChildCount(); j++){
                    View button = ((RadioGroup) v).getChildAt(j);
                    if (button instanceof RadioButton){
                        buttonStateChanger((RadioButton)button);
                        button.setOnClickListener(this);
                    }
                }
            }
        }

        binder.scoringPersonalOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOk(v);
            }
        });
    }

    @Override
    public void onClick(View button) {
        ((RadioButton)button).setChecked(true);
        for (int i = 0; i < ((RadioGroup)button.getParent()).getChildCount(); i++) {
            View view = ((RadioGroup) button.getParent()).getChildAt(i);
            if (view instanceof RadioButton) {
                buttonStateChanger((RadioButton)view);
            }
        }
    }

    private void buttonStateChanger(RadioButton button){
        Resources res = activity.getResources();
        if (button.isChecked()){
            button.setTextColor(res.getColor(android.R.color.white, activity.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, activity.getTheme()));
        } else {
            button.setTextColor(res.getColor(R.color.black, activity.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, activity.getTheme()));
        }
    }

    //TODO use UiUtils.buttonChanger to disable button
    public void onClickOk(View view){
        Intent intent = new Intent(activity, ScoringWorkActivity.class);
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof RadioGroup) {
                for (int j = 0; j < ((RadioGroup)v).getChildCount(); j++){
                    View button = ((RadioGroup) v).getChildAt(j);
                    if (button instanceof RadioButton && ((RadioButton)button).isChecked()){
                        intent.putExtra(v.getTag()+"", ((RadioButton)button).getText());
                    }
                }
            }

            intent.putExtra(binder.scoringDateOfBirthDaySpinner.getTag()+""
                    , binder.scoringDateOfBirthDaySpinner.getSelectedItem()+"");
            intent.putExtra(binder.scoringDateOfBirthMonthSpinner.getTag()+""
                    , binder.scoringDateOfBirthMonthSpinner.getSelectedItem()+"");
            intent.putExtra(binder.scoringDateOfBirthYearSpinner.getTag()+""
                    , binder.scoringDateOfBirthYearSpinner.getSelectedItem()+"");

        }

        activity.startActivity(intent);
    }

    private void setSpinnerYears(ArrayList<String> years){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 18; i >= 1900; i--) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, years);
        binder.scoringDateOfBirthYearSpinner.setAdapter(adapter);

        binder.scoringDateOfBirthYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpinnerMonths(position);
                Log.d("tag", ":"+binder.scoringDateOfBirthMonthSpinner.getAdapter().getCount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binder.scoringDateOfBirthMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tag", position + ":" + binder.scoringDateOfBirthMonthSpinner.getAdapter().getCount());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setSpinnerMonths(int position){
        String[] months = new String[12];
        if (position == 0) {
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            for (int i = 0; i < currentMonth; i++) {
                months[i] = activity.getResources().getStringArray(R.array.months)[i];
            }
        } else {
            months = activity.getResources().getStringArray(R.array.months);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, months);
        binder.scoringDateOfBirthMonthSpinner.setAdapter(adapter);
    }

}