package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertyManageInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class PropertyManageInfoFragment extends Fragment {
    //private static final String PROPERTY_ID = "ID";
    private final String TAG = PropertyManageInfoFragment.class.getSimpleName();

    private FragmentPropertyManageInfoBinding binder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_property_manage_info, container, false);
        //final int propertyId = new PropertyManagementActivity().getPropertyID();
        fetchDataFromServer(((PropertyManagementActivity)getActivity()).getPropertyID()/*getArguments().getInt(PROPERTY_ID)*/);

        //fetchDataFromServer(propertyId/*getArguments().getInt(PROPERTY_ID)*/);

        return binder.getRoot();
    }

    private void fetchDataFromServer(int id){
        ConnectionsManager.getInstance(getActivity()).getMyProperties(id, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                HashMap<String, Integer> response = parseResponse(((BallabaOkResponse)entity).body);
                final int NUM_OF_VIEWS = response.get("view_count");
                final int NUM_OF_MEETINGS = response.get("meetings_count");
                final int NUM_OF_INTERESTED = response.get("interested_count");

                binder.propertyInfoViews.setText(NUM_OF_VIEWS+"");//TODO replace with string format
                binder.propertyInfoMeetings.setText(NUM_OF_MEETINGS+"");
                binder.propertyInfoInterested.setText(NUM_OF_INTERESTED+"");
            }

            @Override
            public void reject(BallabaBaseEntity entity) {

            }
        });
    }

    private HashMap<String, Integer> parseResponse(String data){
        HashMap<String, Integer> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            final Iterator<String> KEYS = jsonObject.keys();

            while(KEYS.hasNext() ) {
                final String KEY = KEYS.next();
                //if (jsonObject.get(KEY) instanceof JSONObject ) {
                    map.put(KEY, jsonObject.getInt(KEY));
                //}
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }
}
