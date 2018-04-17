package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringPersonalActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWorkActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import static com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter.BUTTON_STATE.CHECKED;
import static com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter.BUTTON_STATE.UNCHECKED;

/**
 * Created by User on 17/04/2018.
 */

public class ScoringPersonalPresenter implements RadioButton.OnClickListener{
    private static String TAG = ScoringPersonalPresenter.class.getSimpleName();

    @IntDef({CHECKED, UNCHECKED})
    public @interface BUTTON_STATE {
        int CHECKED = 1, UNCHECKED = 0;
    }

    private static ScoringPersonalPresenter instance;
    private Context context;
    private ActivityScoringPersonalBinding binder;
    private Button[] buttons;
    private @BUTTON_STATE int state;

    public ScoringPersonalPresenter(Context context, ActivityScoringPersonalBinding binding) {
        this.context = context;
        this.binder = binding;

        initButtons();
    }

    private void initButtons(){
        ViewGroup root = binder.scoringPersonalRoot;
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof RadioGroup) {
                for (int j = 0; j < ((RadioGroup)v).getChildCount(); j++){
                    View button = ((RadioGroup) v).getChildAt(j);
                    if (button instanceof RadioButton){
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
        Resources res = context.getResources();
        if (button.isChecked()){
            button.setTextColor(res.getColor(android.R.color.white, context.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, context.getTheme()));
        } else {
            button.setTextColor(res.getColor(R.color.black, context.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, context.getTheme()));
        }
    }

    public void onClickOk(View view){
        Intent intent = new Intent(context, ScoringWorkActivity.class);
        ViewGroup root = binder.scoringPersonalRoot;
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

        context.startActivity(intent);
    }

}
