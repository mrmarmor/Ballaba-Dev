package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Adapters.MeetingsPickerRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropMeetingsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddPropMeetingsFrag extends Fragment {

    private static final String TAG = AddPropMeetingsFrag.class.getSimpleName();
    private FragmentAddPropMeetingsBinding binder;
    private Context context;
    private ArrayList<BallabaMeetingsPickerDateEntity> fullDates;

    public AddPropMeetingsFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddPropMeetingsFrag newInstance() {
        AddPropMeetingsFrag fragment = new AddPropMeetingsFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_meetings, container, false);

        ((AddPropertyActivityNew) getActivity()).changePageIndicatorText(0);
        initCalendar("he");
        fullDates = new ArrayList<>();

        final MeetingsPickerRecyclerViewAdapter adapter = new MeetingsPickerRecyclerViewAdapter(context, fullDates);
        binder.addPropMeetingsDatesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binder.addPropMeetingsDatesRecyclerView.setAdapter(adapter);
        binder.addPropMeetingsDatesRecyclerView.setNestedScrollingEnabled(false);

        binder.addPropMeetingsCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                deleteNotEditedItems();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                BallabaMeetingsPickerDateEntity ballabaDate = new BallabaMeetingsPickerDateEntity();
                ballabaDate.currentDate = calendar.getTime();
                ballabaDate.calendar = calendar;
                fullDates.add(ballabaDate);
                adapter.updateItems();
            }
        });
        final String propertyID = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID , null);
        binder.addPropMeetingsNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsManager.getInstance(context).uploadPropertyMeetingsDates(Integer.parseInt(propertyID) , fullDates, new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                        Log.e(TAG, entity.toString());
                        Intent intent = new Intent(getActivity(), PropertyDescriptionActivity.class);
                        intent.putExtra(PropertyDescriptionActivity.PROPERTY , propertyID);
                        intent.putExtra("SHOW_CONTINUE_OPTION", false);
                        startActivity(intent);
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        Log.e(TAG, entity.toString());
                    }
                });
            }
        });

        return binder.getRoot();
    }

    private void deleteNotEditedItems() {
        ArrayList<BallabaMeetingsPickerDateEntity> objectsToDelete = new ArrayList<>();
        for (BallabaMeetingsPickerDateEntity entity : fullDates) {
            if (!entity.edited) {
                objectsToDelete.add(entity);
            }
        }

        for (BallabaMeetingsPickerDateEntity deleteble : objectsToDelete) {
            fullDates.remove(deleteble);
        }
    }

    private void initCalendar(String locale) {
        long now = Calendar.getInstance().getTimeInMillis();
        binder.addPropMeetingsCalendarView.setMinDate(now);

        // change calendar to hebrew:
        Configuration hebrewConfiguration = getResources().getConfiguration();
        hebrewConfiguration.setLocale(new Locale(locale));
        getActivity().createConfigurationContext(hebrewConfiguration);
        Locale.setDefault(new Locale(locale));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}
