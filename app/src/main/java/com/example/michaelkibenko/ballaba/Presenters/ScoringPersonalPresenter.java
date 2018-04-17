package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringPersonalActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import static com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter.BUTTON_STATE.CHECKED;
import static com.example.michaelkibenko.ballaba.Presenters.ScoringPersonalPresenter.BUTTON_STATE.UNCHECKED;

/**
 * Created by User on 17/04/2018.
 */

public class ScoringPersonalPresenter {
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
            if (v instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout)v).getChildCount(); j++){
                    View button = ((LinearLayout) v).getChildAt(j);
                    if (button instanceof Button){
                        button.setTag(BUTTON_STATE.UNCHECKED);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                buttonStateChanger((Button)v);
                            }
                        });
                    }
                }
            }
        }
    }

    private void buttonStateChanger(Button button){
        Resources res = context.getResources();
        if (Integer.parseInt(button.getTag().toString()) == BUTTON_STATE.CHECKED){
            button.setTextColor(res.getColor(R.color.black, context.getTheme()));
            button.setBackgroundColor(res.getColor(android.R.color.white, context.getTheme()));
            button.setTag(BUTTON_STATE.UNCHECKED);
        } else {
            button.setTextColor(res.getColor(android.R.color.white, context.getTheme()));
            button.setBackgroundColor(res.getColor(R.color.colorPrimary, context.getTheme()));
            button.setTag(BUTTON_STATE.CHECKED);
        }
    }

    /*public void onClickOk(){
        Toast.makeText(context, "on click", Toast.LENGTH_SHORT).show();
    }*/

}
