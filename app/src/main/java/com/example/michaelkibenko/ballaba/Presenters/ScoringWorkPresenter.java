package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.FinishActivityReceiver;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringWorkBinding;

/**
 * Created by User on 18/04/2018.
 */

public class ScoringWorkPresenter implements RadioButton.OnClickListener
        , EditText.OnFocusChangeListener {

    public static String TAG = ScoringWorkPresenter.class.getSimpleName();

    private AppCompatActivity activity;
    private ActivityScoringWorkBinding binder;
    private ViewGroup root;

    public ScoringWorkPresenter(AppCompatActivity activity, ActivityScoringWorkBinding binding) {
        this.activity = activity;
        this.binder = binding;
        this.root = binder.scoringWorkRoot;

        initButtons();

        binder.scoringSiteEditText.setOnFocusChangeListener(this);
        binder.scoringEmailEditText.setOnFocusChangeListener(this);
    }

    private void initButtons(){
        for (int i = 0; i < root.getChildCount(); i++) {
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
        }

        binder.scoringWorkOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOk();
            }
        });
    }

    @Override
    public void onClick(@NonNull final View button) {
        ((RadioButton)button).setChecked(true);
        for (int i = 0; i < ((RadioGroup)button.getParent()).getChildCount(); i++) {
            View view = ((RadioGroup) button.getParent()).getChildAt(i);
            if (view instanceof RadioButton) {
                buttonStateChanger((RadioButton)view);
            }
        }
    }

    private void buttonStateChanger(@NonNull final RadioButton button){
        Resources res = activity.getResources();
        if (button.isChecked()){
            button.setTextColor(res.getColor(android.R.color.white, activity.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, activity.getTheme()));
        } else {
            button.setTextColor(res.getColor(R.color.black, activity.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, activity.getTheme()));
        }
    }

    public void onClickOk(){
        if (!isWebsiteNameValid()) {
            displayError("website");
            return;
        }
        if (!isEmailAddressValid()){
            displayError("email");
            return;
        }

        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            for (int i = 0; i < root.getChildCount(); i++) {
                View v = root.getChildAt(i);
                if (v instanceof RadioGroup) {
                    for (int j = 0; j < ((RadioGroup)v).getChildCount(); j++){
                        View button = ((RadioGroup) v).getChildAt(j);
                        if (button instanceof RadioButton && ((RadioButton)button).isChecked()){
                            bundle.putString(v.getTag()+"", ((RadioButton)button).getText()+"");
                        }
                    }
                }
            }

            bundle.putString(binder.scoringSiteEditText.getTag() +""
                           , binder.scoringSiteEditText.getText()+"");
            bundle.putString(binder.scoringEmailEditText.getTag() +""
                           , binder.scoringEmailEditText.getText()+"");

            //TODO send data to server

            for (final String KEY : bundle.keySet()) {
                Log.d(TAG, String.format("%s %s", KEY, bundle.get(KEY)));
            }
        } else {
            Toast.makeText(activity, "Error: No data exists", Toast.LENGTH_LONG).show();
        }

        activity.finish();
        activity.sendBroadcast(new Intent(FinishActivityReceiver.ACTION_FINISH_ACTIVITY));
    }

    private boolean isWebsiteNameValid(){
        CharSequence website = binder.scoringSiteEditText.getText();
        return Patterns.WEB_URL.matcher(website).matches();
    }
    private boolean isEmailAddressValid(){
        CharSequence email = binder.scoringEmailEditText.getText();
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void displayError(String message){
        Toast.makeText(activity, "Invalid "+message, Toast.LENGTH_SHORT).show();
    }

    //hiding softKeyboard when user taps out of edit text
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            UiUtils.instance(true, activity).hideSoftKeyboard(v);
        }
    }

    public void onClickBack(){
        activity.finish();
    }

}