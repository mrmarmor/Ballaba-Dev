package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters.PropertyManageInterestedAdapter;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PropertyManageInterestedFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BallabaUser> users;
    private PropertyManageInterestedAdapter adapter;
    private LinearLayout emptyStateContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_manage_interested, container, false);

        ((PropertyManagementActivity) getActivity()).showCheckboxInToolbar(true);

        users = new ArrayList<>();
        getUsers();
        /*for (int i = 0; i < 10; i++) {
            BallabaUser user = new BallabaUser();
            user.setId(String.valueOf(i));
            user.setFirst_name("danny " + i);
            user.setLast_name("cohen " + i);
            user.setProfile_image("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?fit=256%2C256&quality=100");
            users.add(user);
        }*/
        emptyStateContainer = view.findViewById(R.id.property_managment_interested_empty_state_layout);

        recyclerView = view.findViewById(R.id.property_manage_interested_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new PropertyManageInterestedAdapter(getActivity(), this, users);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void toggleEmptyStateVisibility(boolean show) {
        emptyStateContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void getUsers() {
        int propertyID = ((PropertyManagementActivity) getActivity()).getPropertyID();
        ConnectionsManager.getInstance(getActivity()).getInterestedUsers(propertyID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("RES", "resolve: " + ((BallabaOkResponse) entity).body);
                try {
                    JSONArray jsonArray = new JSONArray(((BallabaOkResponse) entity).body);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String userID = object.getString("id");

                        JSONArray userArr = object.getJSONArray("user");

                        for (int j = 0; j < userArr.length(); j++) {
                            JSONObject obj = userArr.getJSONObject(j);

                            String firstName = obj.getString("first_name");
                            String lastName = obj.getString("last_name");
                            String profileImage = obj.getString("profile_image");

                            BallabaUser user = new BallabaUser();
                            user.setId(userID);
                            user.setFirst_name(firstName);
                            user.setLast_name(lastName);
                            user.setProfile_image(profileImage);
                            users.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    toggleEmptyStateVisibility(users.isEmpty());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("RES", "reject: " + ((BallabaErrorResponse) entity).message);
            }
        });
    }

    public void setAllChecked(boolean isCheck) {
        adapter.checkAll(isCheck);
    }

    public PropertyManageInterestedAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<Integer> getSelectedUsersID() {
        return adapter.getSelectedUsersID();
    }

    public int getSelectedUserID() {
        return adapter.getSelectedUserID();
    }

}
