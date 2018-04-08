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
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PriceFragment extends Fragment {
    public static final String FILTER_PRICE_MIN = "price min value"
                              , FILTER_PRICE_MAX = "price max value";

    private String priceMin, priceMax;

    private static PriceFragment instance;
    private Context context;
    private BallabaFragmentListener listener;

    public PriceFragment() {}

    public static PriceFragment newInstance(/*String param1, String param2*/) {
        PriceFragment fragment = new PriceFragment();

        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }
    public static PriceFragment getInstance(){
        if(instance == null){
            instance = newInstance();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_price, container, false);
        //onAttachFragment(getParentFragment());
        initSeekBar(v);

        return v;

    }

    private void initSeekBar(View v){
        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = v.findViewById(R.id.mainActivity_filter_price_slider);

// get min and max text view
        final TextView tvMin = v.findViewById(R.id.mainActivity_filter_price_textView_min);
        final TextView tvMax = v.findViewById(R.id.mainActivity_filter_price_textView_max);

        //TODO these values should be received from server or locally?
        priceMin = "1000";
        priceMax = "7000+";
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
                priceMin = String.valueOf(minValue);
                priceMax = String.valueOf(maxValue);
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
        if (context instanceof BallabaFragmentListener) {
            listener = (BallabaFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

  /*  @Override
    public void onPause() {
        super.onPause();

        if (listener != null) {
            listener.onFragmentInteraction(new Uri[]{Uri.parse(priceMin), Uri.parse(priceMax)});
        }
    }*/

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (!visible){
            if (listener != null) {
                HashMap<String, String> results = new HashMap<>(2);
                results.put(FILTER_PRICE_MIN, priceMin);
                results.put(FILTER_PRICE_MAX, priceMax);
                listener.onFragmentInteraction(results);
            }
        }
    }

   /* @Override
    public void onDetach() {
        super.onDetach();

        *//*Bundle bundle = new Bundle(2);
        bundle.putString(FILTER_PRICE_MIN, priceMin);
        bundle.putString(FILTER_PRICE_MAX, priceMax);
        setArguments(bundle);
*//*
        //TODO if we want to pass data to MainActivity:
        if (listener != null) {
            listener.onFragmentInteraction(new Uri[]{Uri.parse(priceMin), Uri.parse(priceMax)});
        }
        listener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
