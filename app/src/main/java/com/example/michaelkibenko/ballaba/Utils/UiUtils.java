package com.example.michaelkibenko.ballaba.Utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ScreenStates.FULL;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ScreenStates.HALF;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ScreenStates.HIDE;

/**
 * Created by User on 11/03/2018.
 */

public class UiUtils {
    public @interface ScreenStates {
        float FULL = 0;
        float HALF = 0.5f;
        float HIDE = 1;
    }

    private static UiUtils instance;
    private Context ctx;

    public static UiUtils instance(boolean isOnce, Context context){
        if(!isOnce) {
            if (instance == null) {
                instance = new UiUtils(context);
            }
            return instance;
        }
        return new UiUtils(context);
    }

    private UiUtils(Context context){
        this.ctx = context;
    }

    public void showSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //to get view from context as needed in the function below use: ((Activity)context).getWindow().getDecorView()
    public void hideSoftKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void buttonChanger(Button btn, boolean is){
        if(is){
            btn.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary, ctx.getTheme()));
            btn.setAlpha(1f);
            btn.setClickable(true);
            btn.setTextColor(Color.WHITE);
        }else {
            btn.setBackgroundColor(ctx.getResources().getColor(R.color.gray_button_color, ctx.getTheme()));
            btn.setAlpha(0.50f);
            btn.setClickable(false);
            btn.setTextColor(Color.BLACK);
        }
    }

    /*public void setFilterBarVisibility(boolean isVisible){
        View rootView = ((Activity)ctx).getWindow().getDecorView().findViewById(R.id.mainActivity_filter_included);
        rootView.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }*/

    public void setFilterBarVisibility(@ScreenStates float state){
        View rootView = ((MainActivity)ctx).getWindow().getDecorView();//.findViewById(R.id.mainActivity_filter_included);

        Guideline guideline = rootView.findViewById(R.id.mainActivity_filter_guideline_top);//mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = state;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = rootView.findViewById(R.id.mainActivity_filter_included);
        ConstraintSet constraintSetHeight = new ConstraintSet();
        constraintSetHeight.clone(ctx, R.layout.search_filter_screen);
        constraintSetHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1

        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSetHeight.applyTo(constraintLayout);
    }
}
