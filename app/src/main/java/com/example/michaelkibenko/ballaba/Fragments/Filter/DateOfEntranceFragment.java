package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.R;

import java.util.Locale;

public class DateOfEntranceFragment extends Fragment {

    private static DateOfEntranceFragment instance;
    private Context context;
    private CalendarView calendarView;

    public DateOfEntranceFragment() {
        // Required empty public constructor
    }

    public static DateOfEntranceFragment newInstance() {
        DateOfEntranceFragment fragment = new DateOfEntranceFragment();
        return fragment;
    }
    public static DateOfEntranceFragment getInstance(){
        if(instance == null){
            instance = newInstance();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_of_entrance, container, false);
        calendarView = view.findViewById(R.id.entranceDateCalendarView);
        String languageToLoad  = "heb"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ((MainActivity)context).getBaseContext().getResources().updateConfiguration(config,
                ((MainActivity)context).getBaseContext().getResources().getDisplayMetrics());
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
