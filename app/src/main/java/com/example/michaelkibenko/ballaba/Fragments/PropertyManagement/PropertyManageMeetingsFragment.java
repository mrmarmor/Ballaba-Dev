package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Adapters.property_managment_adapters.PropertyManageMeetingAdapter;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PropertyManageMeetingsFragment extends Fragment {

    private final String TAG = PropertyManageMeetingsFragment.class.getSimpleName();

    private LinearLayout emptyStateContainer;
    private RelativeLayout layoutContainer;
    private RecyclerView userRecyclerView;
    private List<BallabaUser> users;
    private int propertyID;
    private PropertyManageMeetingAdapter propertyManageMeetingAdapter;
    private Button futureBtn , pastBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_property_manage_meetings_new, container, false);

        propertyID = ((PropertyManagementActivity) getActivity()).getPropertyID();

        emptyStateContainer = v.findViewById(R.id.property_manage_meetings_empty_state_layout);
        layoutContainer = v.findViewById(R.id.property_manage_meetings_container);
        userRecyclerView = v.findViewById(R.id.property_manage_meetings_recycler_view);

        futureBtn = v.findViewById(R.id.property_manage_meetings_future_btn);
        futureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtnStyle(futureBtn , true);
                changeBtnStyle(pastBtn , false);
                getUsers(1);
            }
        });
        pastBtn = v.findViewById(R.id.property_manage_meetings_past_btn);
        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtnStyle(pastBtn , true);
                changeBtnStyle(futureBtn , false);
                getUsers(2);
            }
        });

        users = new ArrayList<>();

        getUsers(1);

        propertyManageMeetingAdapter = new PropertyManageMeetingAdapter(getActivity(), this, users);
        userRecyclerView.setAdapter(propertyManageMeetingAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));

        return v;
    }

    private void changeBtnStyle(Button btn, boolean b) {
        Resources resources = getActivity().getResources();
        btn.setTextColor(b ? Color.WHITE : resources.getColor(R.color.gray_text_color));
        btn.setBackground(resources.getDrawable(b ? R.drawable.chips_button_pressed : R.drawable.chips_button_with_gray_border));

    }

    private void getUsers(final int type) {
        ConnectionsManager.getInstance(getActivity()).getMeetingUsers(propertyID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("RES", "resolve: " + ((BallabaOkResponse) entity).body);
                try {
                    JSONObject object = new JSONObject(((BallabaOkResponse) entity).body);

                    JSONArray futureMeetingsArr = object.getJSONArray("future_meetings");
                    JSONArray pastMeetingsArr = object.getJSONArray("past_meetings");
                    users.clear();
                    switch (type){
                        case 1:
                            parseUsers(futureMeetingsArr);
                            break;
                        case 2:
                            parsePastUsers(pastMeetingsArr);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("RES", "resolve: " + ((BallabaErrorResponse) entity).message);
            }
        });
    }

    private void parseUsers(JSONArray array) throws JSONException {
        for (int j = 0; j < array.length(); j++) {

            JSONObject obj1 = array.getJSONObject(j);
            String userID = obj1.getString("id");
            String meetingTime = obj1.getString("meeting_time");

            JSONArray userArr = obj1.getJSONArray("user");

            for (int i = 0; i < userArr.length(); i++) {
                //totalFutureCountTV.setText(userArr.length() + " סה''כ");
                JSONObject obj = userArr.getJSONObject(i);


                String firstName = obj.getString("first_name");
                String lastName = obj.getString("last_name");
                String profileImage = obj.getString("profile_image");

                BallabaUser user = new BallabaUser();
                user.setId(userID);
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setProfile_image(profileImage);
                user.setMeeting_time(meetingTime);

                users.add(user);
            }
        }
        propertyManageMeetingAdapter = new PropertyManageMeetingAdapter(getActivity(), this, users);
        userRecyclerView.setAdapter(propertyManageMeetingAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
        //propertyManageMeetingAdapter.notifyDataSetChanged();
        toggleState(users.isEmpty());
    }

    private void parsePastUsers(JSONArray futureMeetingsArr) throws JSONException {
        for (int j = 0; j < futureMeetingsArr.length(); j++) {

            JSONObject obj1 = futureMeetingsArr.getJSONObject(j);
            String userID = obj1.getString("id");
            String meetingTime = obj1.getString("meeting_time");

            JSONArray userArr = obj1.getJSONArray("user");

            for (int i = 0; i < userArr.length(); i++) {
                //totalPastCountTV.setText(userArr.length() + " סה''כ");
                JSONObject obj = userArr.getJSONObject(j);


                String firstName = obj.getString("first_name");
                String lastName = obj.getString("last_name");
                String profileImage = obj.getString("profile_image");

                BallabaUser user = new BallabaUser();
                user.setId(userID);
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setProfile_image(profileImage);
                user.setMeeting_time(meetingTime);

                users.add(user);
            }
            propertyManageMeetingAdapter = new PropertyManageMeetingAdapter(getActivity(), this, users);
            userRecyclerView.setAdapter(propertyManageMeetingAdapter);
            userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
            //propertyManageMeetingAdapter.notifyDataSetChanged();

        }
        toggleState(users.isEmpty());
    }


    public void toggleState(boolean show) {
        emptyStateContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        if (show) ((PropertyManagementActivity)getActivity()).toolbarImagesVisibility(false , false , false ,false);
    }

    public void setAllCheck(boolean isCheck) {
        propertyManageMeetingAdapter.checkAll(isCheck);
    }

    public PropertyManageMeetingAdapter getAdapter() {
        return propertyManageMeetingAdapter;
    }
}
