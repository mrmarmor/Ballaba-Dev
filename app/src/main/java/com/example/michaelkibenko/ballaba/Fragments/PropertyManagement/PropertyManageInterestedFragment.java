package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.List;

public class PropertyManageInterestedFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BallabaUser> users;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_manage_interested, container, false);

        ((PropertyManagementActivity)getActivity()).showInterestedToolbar(true);

        users = new ArrayList<>();

        getUsers();
        for (int i = 0; i < 10; i++) {
            BallabaUser user = new BallabaUser();
            user.setFirst_name("danny " + i);
            user.setLast_name("cohen " + i);
            user.setProfile_image("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100");
            users.add(user);
        }
        recyclerView = view.findViewById(R.id.property_manage_interested_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
        PropertyManageInterestedAdapter adapter = new PropertyManageInterestedAdapter(getActivity() , users);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getUsers() {

    }

}
