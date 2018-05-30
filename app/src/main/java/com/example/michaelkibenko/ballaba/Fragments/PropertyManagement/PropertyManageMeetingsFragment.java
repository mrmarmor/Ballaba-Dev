package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.List;


public class PropertyManageMeetingsFragment extends Fragment {

    private final String TAG = PropertyManageMeetingsFragment.class.getSimpleName();

    private LinearLayout  emptyStateContainer;
    private RelativeLayout layoutContainer;
    private RecyclerView appointedMeetingsRV , heldMeetingsRV;
    private List<BallabaUser> appointedUsers , heldUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_property_manage_meetings, container, false);

        emptyStateContainer = v.findViewById(R.id.property_manage_meetings_empty_state_layout);
        layoutContainer = v.findViewById(R.id.property_manage_meetings_container);
        appointedMeetingsRV = v.findViewById(R.id.property_manage_meetings_appointed_recycler_view);
        heldMeetingsRV = v.findViewById(R.id.property_manage_meetings_held_recycler_view);


        appointedUsers = new ArrayList<>();
        heldUsers = new ArrayList<>();

        toggleState(appointedUsers.isEmpty() && heldUsers.isEmpty());
        return v;
    }

    private void toggleState(boolean show) {
        layoutContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        emptyStateContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void setAllCheck(boolean isCheck) {

    }
}
