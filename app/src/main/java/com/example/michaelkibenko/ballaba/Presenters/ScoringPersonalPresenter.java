package com.example.michaelkibenko.ballaba.Presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWorkActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

import java.util.ArrayList;
import java.util.Calendar;

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
        activity.registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
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
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(ScoringWorkPresenter.ACTION_FINISH_ACTIVITY)) {
                activity.finish();
            }
        }
    };

}
