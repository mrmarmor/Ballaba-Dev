package com.example.michaelkibenko.ballaba.Views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by User on 26/04/2018.
 */

public class MultiLanguagesDatePicker extends DatePicker {
    final private String TAG = MultiLanguagesDatePicker.class.getSimpleName();

    private Context context;
    private TextView titleTextView, formattedDateTextView;

    public MultiLanguagesDatePicker(Context context/*, String locale*/) {
        super(context);
        this.context = context;

        customizeDatePicker("he");
    }

    public MultiLanguagesDatePicker(Context context, AttributeSet attrs/*, String locale*/){
        super(context, attrs);
        this.context = context;

        customizeDatePicker("he");
    }

    public MultiLanguagesDatePicker customizeDatePicker(String locale){
        Resources res = context.getResources();

        //1. change datePicker language to hebrew. TODO IF WE WANT OTHER LANGUAGES, USE OTHER LOCALES!
        //1.a. change datePicker frame to hebrew:
        Locale.setDefault(new Locale(locale));

        //1.b. change calendar frame to hebrew:
        Configuration hebrewConfiguration = res.getConfiguration();
        hebrewConfiguration.setLocale(new Locale(locale));
        context.createConfigurationContext(hebrewConfiguration);

        //2. replace "2018" field from datePicker header with "תאריך כניסה" field
        //ViewGroup group = binderAsset.addPropAssetRentalPeriodDatePicker;
        try {
            //TODO move it to specific method called setText
            LinearLayout ll = (LinearLayout)getChildAt(0);
            for (int i = 0; i < 10 && !(ll.getChildAt(0) instanceof TextView); i++){
                ll = (LinearLayout)ll.getChildAt(0);//=layoutManager
            }
            //LinearLayout ll2 = (LinearLayout)ll1.getChildAt(0);//=child layoutManager
            //LinearLayout ll3 = (LinearLayout)ll1.getChildAt(0);//=child layoutManager
            titleTextView = (TextView)ll.getChildAt(0);//==textView "2018"
            formattedDateTextView = (TextView)ll.getChildAt(1);

            formattedDateTextView.setTypeface(null, Typeface.BOLD);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return this;
    }

    public TextView setTitle(final String text) {
        titleTextView.setText(text);

        //to prevent OS from writing "2018" when user changes date
        //titleTextView.addTextChangedListener(null);
        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(Calendar.getInstance().get(Calendar.YEAR)+""))
                    titleTextView.setText(text);
            }
        });

        return titleTextView;
    }

    /*public TextView setTextSize(TextView textView, Float size){
        textView.setTextSize(size);
        return textView;
    }

    public TextView setPaddingRelevant(){
        titleTextView.setPaddingRelative(0, 8, 0, 8);
        return titleTextView;
    }*/
}
