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
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.R;

import java.util.HashMap;

public class SizeFragment extends Fragment {
    public static final String FILTER_SIZE_MIN = "size min value"
            , FILTER_SIZE_MAX = "size max value";

    private static SizeFragment instance;
    private Context context;
    private BallabaFragmentListener listener;

    private String sizeMin, sizeMax;

    public SizeFragment() {
        // Required empty public constructor
    }

    public static SizeFragment newInstance(/*String param1, String param2*/) {
        SizeFragment fragment = new SizeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }
    public static SizeFragment getInstance(){
        if(instance == null){
            instance = newInstance();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_size, container, false);
        initSeekBar(v);

        return v;
    }

    private void initSeekBar(View v){
        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = v.findViewById(R.id.mainActivity_filter_size_slider);

// get min and max text view
        final TextView tvMin = v.findViewById(R.id.mainActivity_filter_size_textView_min);
        final TextView tvMax = v.findViewById(R.id.mainActivity_filter_size_textView_max);

        //TODO these values should be received from server or locally?
        sizeMin = "20";
        sizeMax = "100+";
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
                //Log.d("CRS=>", priceMin + " : " + priceMax);
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri[] uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        if (context instanceof BallabaFragmentListener) {
            listener = (BallabaFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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

}
