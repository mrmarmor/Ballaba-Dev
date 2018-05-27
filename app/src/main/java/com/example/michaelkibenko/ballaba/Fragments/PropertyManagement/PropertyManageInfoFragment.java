package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertyManageInfoBinding;

public class PropertyManageInfoFragment extends Fragment {
    private final String TAG = PropertyManageInfoFragment.class.getSimpleName();

    private FragmentPropertyManageInfoBinding binder;
    private View v;

    public PropertyManageInfoFragment() {}

    public static PropertyManageInfoFragment newInstance(/*String min, String max*/) {
        PropertyManageInfoFragment fragment = new PropertyManageInfoFragment();
        /*Bundle args = new Bundle();
        args.putString(FILTER_PRICE_MIN, min);
        args.putString(FILTER_PRICE_MAX, max);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO make server request and get data + progress dialog
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_property_manage_info, container, false);
        fetchDataFromServer();

        return v;
    }

    private void fetchDataFromServer(){
        ConnectionsManager.getInstance(getActivity()).getMyProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ((TextView)v.findViewById(R.id.property_info_views)).setText(((BallabaOkResponse)entity).body);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {

            }
        });
    }

}
