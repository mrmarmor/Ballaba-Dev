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

import com.example.michaelkibenko.ballaba.Adapters.MyPropertiesLandlordAdapter;
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

public class MyPropertiesLandlordFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyPropertiesLandlordAdapter adapter;
    private List<MyPropertiesLandlord> myPropertiesLandlordsList;
    private View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.my_properties_landlord_fragment, container, false);

        myPropertiesLandlordsList = new ArrayList<>();
        /*myPropertiesLandlordsList.add(new MyPropertiesLandlord("אחד העם 1 , תל אביב", 1, 2, 65, new String[]{"https://37b3a77d7df28c23c767-53afc51582ca456b5a70c93dcc61a759.ssl.cf3.rackcdn.com/1024x768/54850_3971_001.jpg"}));
        myPropertiesLandlordsList.add(new MyPropertiesLandlord("בן גוריון 12 , תל אביב", 2, 8, 200, new String[]{"https://2e04f91eb4f37516324c-f47ac95ea68e17b39c6e95ff0fa62fe6.ssl.cf3.rackcdn.com/1024x768/53527_2779_00.jpg"}));
        myPropertiesLandlordsList.add(new MyPropertiesLandlord("בן יהודה 5 , יהוד", 3, 4, 120, new String[]{"https://37cbbe48430d59b57b0f-f8fba6cde51be10037079fdf735372bc.ssl.cf3.rackcdn.com/1024x768/55639_6068_02.jpg"}));
        myPropertiesLandlordsList.add(new MyPropertiesLandlord("בן דוד 22 , פתח תקווה", 4, 2, 50, new String[]{"https://c50039.ssl.cf3.rackcdn.com/uploads/photo/file/42580235/default.jpg"}));
        myPropertiesLandlordsList.add(new MyPropertiesLandlord("הנחל 7 , באר שבע", 5, 2, 90, new String[]{"http://www.nyhabitat.com/picture-ny-apt/12699/12699D62.jpg"}));
        myPropertiesLandlordsList.add(new MyPropertiesLandlord("ז'בוטינסקי 2048 , אורנית", 6, 2, 130, new String[]{"http://www.nyhabitat.com/picture-ny-apt/16481/16481D10.jpg"}));
        */
        getLandlordProperties();
        adapter = new MyPropertiesLandlordAdapter(getActivity(), myPropertiesLandlordsList);
        initRecyclerView();


        return v;
    }

    private void getLandlordProperties() {
        ConnectionsManager.getInstance(getActivity()).getLandlordProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                String response = ((BallabaOkResponse) entity).body.toString();
                parseResponse(response);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("WOW", "reject: " + entity);
            }
        });
    }

    private void parseResponse(String response) {
        int id = 0, rooms = 0, size = 0;
        String address = null;
        String[] photos = new String[1];

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                for (int j = 0; j < obj.length(); j++) {
                    id = obj.getInt("id");
                    address = obj.getString("formatted_address");
                    rooms = obj.getInt("rooms");
                    size = obj.getInt("size");
                    JSONArray photosArr = obj.getJSONArray("photos");
                    photos[0] = photosArr.getJSONObject(0).getString("photo_url");

                }
                MyPropertiesLandlord landlord = new MyPropertiesLandlord(address, id, rooms, size, photos);
                myPropertiesLandlordsList.add(landlord);
            }
            adapter.notifyDataSetChanged();
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
