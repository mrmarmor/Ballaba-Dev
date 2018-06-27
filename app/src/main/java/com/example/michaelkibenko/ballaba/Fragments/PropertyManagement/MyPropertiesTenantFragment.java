package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;

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

import com.example.michaelkibenko.ballaba.Adapters.MyPropertiesTenantAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.MyPropertiesLandlord;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPropertiesTenantFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<MyPropertiesLandlord> myPropertiesTenantList;
    private View v;
    private MyPropertiesTenantAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.my_properties_tenant_fragment, container, false);
        progressBar = v.findViewById(R.id.my_properties_tenant_progress_bar);

        myPropertiesTenantList = new ArrayList<>();
        getTenantProperties();
        adapter = new MyPropertiesTenantAdapter(getActivity(), myPropertiesTenantList);
        initRecyclerView();

        return v;
    }

    private void getTenantProperties() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectionsManager.getInstance(getActivity()).getTenantProperties(new BallabaResponseListener() {
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
        /*progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < 10; i++) {
            myPropertiesTenantList.add(new MyPropertiesLandlord("Tel Aviv , Hertzel " + i , i , i * 2 , 60 * i / 2 , new String[]{"https://37b3a77d7df28c23c767-53afc51582ca456b5a70c93dcc61a759.ssl.cf3.rackcdn.com/1024x768/54850_3971_001.jpg"}));
        }*/
    }

    private void parseResponse(String response) {
        int id, rooms, size;
        String address , price;
        MyPropertiesLandlord landlord;

        try {
            JSONObject object = new JSONObject(response);
            JSONObject obj = object.getJSONObject("current_properties");

            String[] photos = new String[1];
            id = obj.getInt("id");
            address = obj.getString("formatted_address");
            rooms = obj.getInt("rooms");
            size = obj.getInt("size");
            price = obj.getString("price");
            JSONArray photosArr = obj.getJSONArray("photos");
            if (!photosArr.isNull(0))
                photos[0] = photosArr.getJSONObject(0).getString("photo_url");
            landlord = new MyPropertiesLandlord(address, id, rooms, size, photos , price);
            myPropertiesTenantList.add(landlord);
            //Log.d("WOW", "parseResponse: " + photos[0]);
            //}
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        recyclerView = v.findViewById(R.id.my_properties_tenant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
