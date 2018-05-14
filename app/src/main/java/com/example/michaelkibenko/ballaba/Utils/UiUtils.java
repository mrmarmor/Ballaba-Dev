package com.example.michaelkibenko.ballaba.Utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ChipsButtonStates.NOT_PRESSED;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ChipsButtonStates.PRESSED;
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

    @StringDef({PRESSED, NOT_PRESSED})
    public @interface ChipsButtonStates{
        String PRESSED = "PRESSED";
        String NOT_PRESSED = "NOT_PRESSED";
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
            btn.setEnabled(true);
            btn.setTextColor(Color.WHITE);
        }else {
            btn.setBackgroundColor(ctx.getResources().getColor(R.color.gray_button_color, ctx.getTheme()));
            btn.setAlpha(0.50f);
            btn.setClickable(false);
            btn.setEnabled(false);
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

    public Button onChipsButtonClick(Button button, String state){
        if(state.equals(ChipsButtonStates.NOT_PRESSED)){
            //change the state to pressed
            button.setBackgroundResource(R.drawable.chips_button_pressed);
            button.setTextColor(ctx.getResources().getColor(android.R.color.white,ctx.getTheme()));
            button.setTag(ChipsButtonStates.PRESSED);
        }else if(state.equals(ChipsButtonStates.PRESSED)){
            //change the state to not pressed
            button.setBackgroundResource(R.drawable.chips_button);
            button.setTextColor(ctx.getResources().getColor(R.color.colorPrimary,ctx.getTheme()));
            button.setTag(ChipsButtonStates.NOT_PRESSED);
        }else{
            Log.e(TAG, "Chips does not have tag");
        }

        return button;
    }

    public Bitmap uriToBitmap(Uri uri){
        try {
            InputStream stream = ctx.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] uriToBytes(Uri uri) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            InputStream inputStream = ctx.getContentResolver().openInputStream(uri);

            baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public Snackbar showSnackBar(View snackBarView, String message){
        Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary, ctx.getTheme()));
        snackBar.show();
        return snackBar;
    }

}
