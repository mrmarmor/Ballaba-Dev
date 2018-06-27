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
import android.widget.ProgressBar;

import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Adapters.property_managment_adapters.PropertyManageInterestedAdapter;
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
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_manage_interested, container, false);

        progressBar = view.findViewById(R.id.property_manage_interested_progress_bar);

        users = new ArrayList<>();
        getUsers();

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
        //if (show) ((PropertyManagementActivity)getActivity()).toolbarImagesVisibility(false , false , false ,false);
    }

    private void getUsers() {
        int propertyID = ((PropertyManagementActivity) getActivity()).getPropertyID();
        progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    toggleEmptyStateVisibility(users.isEmpty());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void reject(BallabaBaseEntity entity) {
                progressBar.setVisibility(View.GONE);
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
