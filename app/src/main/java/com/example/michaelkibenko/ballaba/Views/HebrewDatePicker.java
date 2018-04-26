package com.example.michaelkibenko.ballaba.Views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.HebrewCalendar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by User on 26/04/2018.
 */

public class HebrewDatePicker extends DatePicker {
    public HebrewDatePicker(Context context) {
        super(context);
    }

    private void init(Context mContext)
    {
       /* LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myPickerView = inflator.inflate(R.layout.datetimepicker, null);
        this.addView(myPickerView);*/
        initializeReference();
    }
    private void initializeReference()
    {

        /*month_plus = (Button) myPickerView.findViewById(R.id.month_plus);
        month_plus.setOnClickListener(month_plus_listener);
        month_display = (EditText) myPickerView.findViewById(R.id.month_display);
        month_minus = (Button) myPickerView.findViewById(R.id.month_minus);
        month_minus.setOnClickListener(month_minus_listener);

        date_plus = (Button) myPickerView.findViewById(R.id.date_plus);
        date_plus.setOnClickListener(date_plus_listener);
        date_display = (EditText) myPickerView.findViewById(R.id.date_display);
        date_display.addTextChangedListener(date_watcher);
        date_minus = (Button) myPickerView.findViewById(R.id.date_minus);
        date_minus.setOnClickListener(date_minus_listener);*/


        initData();
        //initFilterNumericDigit();

    }
    public void initData()
    {
        /*cal = Calendar.getInstance();
        month_display.setText(months[cal.get(Calendar.MONTH)]);
        date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
        hour_display.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        min_display.setText(String.valueOf(cal.get(Calendar.MINUTE)));*/
    }

    String[] months = { "Jan4", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec" };

    View.OnClickListener month_plus_listener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {

            try
            {
                /*cal.add(Calendar.MONTH, 1);

                month_display.setText(months[cal.get(Calendar.MONTH)]);
                year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
                date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                changeFilter();
                sendToListener();*/
            }
            catch (Exception e)
            {
                Log.e("", e.toString());
            }
        }
    };
    View.OnClickListener month_minus_listener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            try
            {
               /* cal.add(Calendar.MONTH, -1);
                month_display.setText(months[cal.get(Calendar.MONTH)]);
                year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
                date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));*/
                //changeFilter();
                //sendToListener();
            }
            catch (Exception e)
            {
                Log.e("", e.toString());
            }
        }
    };
}
