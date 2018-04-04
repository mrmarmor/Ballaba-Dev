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
import com.example.michaelkibenko.ballaba.R;

public class RoomsFragment extends Fragment {
    public static final String FILTER_ROOMS_MIN = "rooms min value"
            , FILTER_ROOMS_MAX = "rooms max value";

    private static RoomsFragment instance;
    private Context context;
    private BallabaFragmentListener listener;

    private String roomsMin, roomsMax;

    public RoomsFragment() {
        // Required empty public constructor
    }

    public static RoomsFragment newInstance(/*String param1, String param2*/) {
        RoomsFragment fragment = new RoomsFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }
    public static RoomsFragment getInstance(){
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
        View v = inflater.inflate(R.layout.fragment_rooms, container, false);
        initSeekBar(v);

        return v;
    }

    private void initSeekBar(View v){
        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = v.findViewById(R.id.mainActivity_filter_rooms_slider);

// get min and max text view
        final TextView tvMin = v.findViewById(R.id.mainActivity_filter_rooms_textView_min);
        final TextView tvMax = v.findViewById(R.id.mainActivity_filter_rooms_textView_max);

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
                roomsMin = String.valueOf(minValue);
                roomsMax = String.valueOf(maxValue);
                //Log.d("CRS=>", priceMin + " : " + priceMax);
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri[] uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (!visible){
            if (listener != null) {
                listener.onFragmentInteraction(new Uri[]{Uri.parse(roomsMin), Uri.parse(roomsMax)});
            }
        }
    }

}
