package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Views.MultiLanguagesDatePicker;

import java.util.Calendar;
import java.util.Date;

public class DateOfEntranceFragment extends Fragment {

    private static final String TAG = DateOfEntranceFragment.class.getSimpleName();
    private Context context;
    private MultiLanguagesDatePicker datePicker;
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
        if (context != null && ((MainActivity)context).presenter != null)
            filterResult = ((MainActivity)context).presenter.filterResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_of_entrance, container, false);
        datePicker = view.findViewById(R.id.entranceDate_datePicker);
        isFlexibleCheckBox = view.findViewById(R.id.flexibleCheckBox);

        datePicker.setTitle(getString(R.string.addProperty_asset_dateOfEntrance))
                  .setTextSize(14);

        final Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
                , new MultiLanguagesDatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(year, month, dayOfMonth);
                enteredDate = c.getTime();
                filterResult.setEnterDate(enteredDate);
            }
        });

        isFlexibleCheckBox.setChecked(filterResult.isFlexible());
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
