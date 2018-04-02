package com.example.michaelkibenko.ballaba.Utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 11/03/2018.
 */

public class UiUtils {
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

    public void setFilterBarVisibility(boolean isVisible){
        View rootView = ((Activity)ctx).getWindow().getDecorView().findViewById(R.id.mainActivity_filter_included);
        rootView.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }
}
