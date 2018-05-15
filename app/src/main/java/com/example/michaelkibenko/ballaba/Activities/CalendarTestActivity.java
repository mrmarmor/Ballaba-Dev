package com.example.michaelkibenko.ballaba.Activities;

import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropMeetingsFrag;
import com.example.michaelkibenko.ballaba.R;

public class CalendarTestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_activity);

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new AddPropMeetingsFrag()).commit();
    }
}
