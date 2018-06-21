package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.michaelkibenko.ballaba.Adapters.MyPropertiesLandlordAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.MyPropertiesLandlord;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPropertiesLandlordFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyPropertiesLandlordAdapter adapter;
    private List<MyPropertiesLandlord> myPropertiesLandlordsList;
    private View v;
    private ProgressBar progressBar;
    private StringUtils stringUtils = StringUtils.getInstance(true);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.my_properties_landlord_fragment, container, false);
        progressBar = v.findViewById(R.id.my_properties_landlord_progress_bar);

        myPropertiesLandlordsList = new ArrayList<>();
        getLandlordProperties();
        adapter = new MyPropertiesLandlordAdapter(getActivity(), myPropertiesLandlordsList);
        initRecyclerView();

        return v;
    }

    private void getLandlordProperties() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectionsManager.getInstance(getActivity()).getLandlordProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                progressBar.setVisibility(View.GONE);
                String response = ((BallabaOkResponse) entity).body.toString();
                parseResponse(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                progressBar.setVisibility(View.GONE);
                Log.d("WOW", "reject: " + entity);
            }
        });
    }

    private void parseResponse(String response) {
        int id = 0, rooms = 0, size = 0;
        String address = null;
        MyPropertiesLandlord landlord = null;

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String[] photos = new String[1];
                id = obj.getInt("id");
                address = stringUtils.formattedHebrew(obj.getString("formatted_address"));
                rooms = obj.getInt("rooms");
                size = obj.getInt("size");
                JSONArray photosArr = obj.getJSONArray("photos");
                if (!photosArr.isNull(0))
                    photos[0] = photosArr.getJSONObject(0).getString("photo_url");
                landlord = new MyPropertiesLandlord(address, id, rooms, size, photos , null);
                myPropertiesLandlordsList.add(landlord);
                //Log.d("WOW", "parseResponse: " + photos[0]);
            }
            //adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        recyclerView = v.findViewById(R.id.my_properties_landlord_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
