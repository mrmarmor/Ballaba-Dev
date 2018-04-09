package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateOfEntranceFragment extends Fragment {

    private static final String TAG = DateOfEntranceFragment.class.getSimpleName();
    private Context context;
    private CalendarView calendarView;
    private Date enteredDate;
    private FilterResultEntity filterResult;
    private CheckBox isFlexibleCheckBox;
    private boolean isFlexible;

    public DateOfEntranceFragment() {
        // Required empty public constructor
    }

    public static DateOfEntranceFragment newInstance() {
        DateOfEntranceFragment fragment = new DateOfEntranceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterResult = ((MainActivity)context).presenter.filterResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_of_entrance, container, false);
        calendarView = view.findViewById(R.id.entranceDateCalendarView);
        isFlexibleCheckBox = view.findViewById(R.id.flexibleCheckBox);
        String languageToLoad  = "heb"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ((MainActivity)context).getBaseContext().getResources().updateConfiguration(config,
                ((MainActivity)context).getBaseContext().getResources().getDisplayMetrics());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                enteredDate = calendar.getTime();
                filterResult.setEnterDate(enteredDate);
            }
        });

        isFlexibleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFlexible = isChecked;
                filterResult.setFlexible(isFlexible);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
