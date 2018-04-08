package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.R;

public class DateOfEntranceFragment extends Fragment {

    private static DateOfEntranceFragment instance;
    private Context context;
    private BallabaFragmentListener listener;

    public DateOfEntranceFragment() {
        // Required empty public constructor
    }

    public static DateOfEntranceFragment newInstance(/*String param1, String param2*/) {
        DateOfEntranceFragment fragment = new DateOfEntranceFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }
    public static DateOfEntranceFragment getInstance(){
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_of_entrance, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri[] uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }
*/
    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BallabaFragmentListener) {
            listener = (BallabaFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
