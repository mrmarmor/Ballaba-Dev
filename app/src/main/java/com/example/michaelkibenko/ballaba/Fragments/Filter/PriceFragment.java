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
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PriceFragment extends Fragment {
    public static final String FILTER_PRICE_MIN = "price min value"
                              , FILTER_PRICE_MAX = "price max value";

    private String priceMin, priceMax;

    private Context context;
    private FilterResultEntity filterResults;

    public PriceFragment() {}

    public static PriceFragment newInstance(String min, String max) {
        PriceFragment fragment = new PriceFragment();
        Bundle args = new Bundle();
        args.putString(FILTER_PRICE_MIN, min);
        args.putString(FILTER_PRICE_MAX, max);
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
        View v = inflater.inflate(R.layout.fragment_price, container, false);

        this.priceMax = getArguments().getString(FILTER_PRICE_MAX, "20000");
        this.priceMin = getArguments().getString(FILTER_PRICE_MIN, "0");

        initSeekBar(v);
        return v;
    }

    private void initSeekBar(View v){
        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = v.findViewById(R.id.mainActivity_filter_price_slider);
        rangeSeekbar.setMaxValue(Float.parseFloat(priceMax));
        rangeSeekbar.setMinValue(Float.parseFloat(priceMin));


// get min and max text view
        final TextView tvMin = v.findViewById(R.id.mainActivity_filter_price_textView_min);
        final TextView tvMax = v.findViewById(R.id.mainActivity_filter_price_textView_max);

        tvMin.setText(priceMin);
        tvMax.setText(priceMax);
// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
                priceMax = maxValue.intValue() +"";
                priceMin = minValue.intValue() + "";
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                priceMin = String.valueOf(minValue);
                priceMax = String.valueOf(maxValue);
                priceMax = maxValue.intValue() +"";
                priceMin = minValue.intValue() + "";

                filterResults.setFromPrice(minValue.intValue());
                filterResults.setToPrice(maxValue.intValue());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
