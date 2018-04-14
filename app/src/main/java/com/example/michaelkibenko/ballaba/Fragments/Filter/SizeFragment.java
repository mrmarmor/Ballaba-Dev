package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class SizeFragment extends Fragment {
    public static final String FILTER_SIZE_MIN = "size min value"
            , FILTER_SIZE_MAX = "size max value";

    private Context context;
    private BallabaFragmentListener listener;

    private String sizeMin, sizeMax;
    private FilterResultEntity filterResults;

    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMin, tvMax;

    public SizeFragment() {
        // Required empty public constructor
    }

    public static SizeFragment newInstance(String min, String max) {
        SizeFragment fragment = new SizeFragment();
        Bundle args = new Bundle();
        args.putString(FILTER_SIZE_MIN, min);
        args.putString(FILTER_SIZE_MAX, max);
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_size, container, false);
        this.sizeMin= getArguments().getString(FILTER_SIZE_MIN, "10");
        this.sizeMax = getArguments().getString(FILTER_SIZE_MAX, "1000");
        initSeekBar(v);

        return v;
    }

    private void initSeekBar(View v){
        // get seekbar from view
        rangeSeekbar = v.findViewById(R.id.mainActivity_filter_size_slider);
        rangeSeekbar.setMinValue(Float.parseFloat(sizeMin));
        rangeSeekbar.setMaxValue(Float.parseFloat(sizeMax));

// get min and max text view
        tvMin = v.findViewById(R.id.mainActivity_filter_size_textView_min);
        tvMax = v.findViewById(R.id.mainActivity_filter_size_textView_max);

        tvMin.setText(sizeMin);
        tvMax.setText(sizeMax);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                sizeMin = String.valueOf(minValue);
                sizeMax = String.valueOf(maxValue);
                filterResults.setFromSize(minValue.intValue());
                filterResults.setToSize(maxValue.intValue());
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri[] uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (!visible){
            if (listener != null) {
                HashMap<String, String> results = new HashMap<>(2);
                results.put(FILTER_SIZE_MIN, sizeMin);
                results.put(FILTER_SIZE_MAX, sizeMax);
                listener.onFragmentInteraction(results);            }
        }
    }

    public void updateRangeBar(String from, String to){
        sizeMin = from;
        sizeMax = to;
        rangeSeekbar.setMinValue(Float.parseFloat(sizeMin));
        rangeSeekbar.setMaxValue(Float.parseFloat(sizeMax));
        tvMin.setText(sizeMin);
        tvMax.setText(sizeMax);
    }
}
