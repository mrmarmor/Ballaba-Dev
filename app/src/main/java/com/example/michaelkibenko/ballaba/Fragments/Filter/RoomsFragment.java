package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.R;

import java.util.HashMap;

public class RoomsFragment extends Fragment {
    public static final String FILTER_ROOMS_MIN = "rooms min value"
            , FILTER_ROOMS_MAX = "rooms max value";

    private Context context;
    private String roomsMin, roomsMax;
    private FilterResultEntity filterResults;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMin, tvMax;

    public RoomsFragment() {
        // Required empty public constructor
    }

    public static RoomsFragment newInstance(String min, String max) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString(FILTER_ROOMS_MIN, min);
        args.putString(FILTER_ROOMS_MAX, max);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterResults = ((MainActivity)context).presenter.filterResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rooms, container, false);
        this.roomsMin = getArguments().getString(FILTER_ROOMS_MIN, "0");
        this.roomsMax = getArguments().getString(FILTER_ROOMS_MAX, "20");
        initSeekBar(v);
        return v;
    }

    private void initSeekBar(View v){
        // get seekbar from view
        rangeSeekbar = v.findViewById(R.id.mainActivity_filter_rooms_slider);
        rangeSeekbar.setMinValue(Float.parseFloat(roomsMin));
        rangeSeekbar.setMaxValue(Float.parseFloat(roomsMax));

// get min and max text view
        tvMin = v.findViewById(R.id.mainActivity_filter_rooms_textView_min);
        tvMax = v.findViewById(R.id.mainActivity_filter_rooms_textView_max);
        tvMin.setText(String.valueOf(roomsMin));
        tvMax.setText(String.valueOf(roomsMax));

        //TODO these values should be received from server or locally?
// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if(minValue.floatValue() - minValue.intValue() == 0){
                    tvMin.setText(String.valueOf(minValue.intValue()));
                }else{
                    tvMin.setText(String.valueOf(minValue.floatValue()));
                }

                if(maxValue.floatValue() - maxValue.intValue() == 0){
                    tvMax.setText(String.valueOf(maxValue.intValue()));
                }else{
                    tvMax.setText(String.valueOf(maxValue.floatValue()));
                }

                roomsMin = minValue.floatValue()+"";
                roomsMax = maxValue.floatValue()+"";
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                roomsMin = String.valueOf(minValue);
                roomsMax = String.valueOf(maxValue);

                roomsMin = minValue+"";
                roomsMax = maxValue+"";

                filterResults.setFromRooms(Float.parseFloat(roomsMin));
                filterResults.setToRooms(Float.parseFloat(roomsMax));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void updateRangeBar(String from, String to){
        roomsMin = from;
        roomsMax = to;
        rangeSeekbar.setMinValue(Float.parseFloat(roomsMin));
        rangeSeekbar.setMaxValue(Float.parseFloat(roomsMax));
        tvMin.setText(roomsMin);
        tvMax.setText(roomsMax);
    }

}
