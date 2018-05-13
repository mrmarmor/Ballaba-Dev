package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropMeetingsBinding;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPropMeetingsFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPropMeetingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPropMeetingsFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropMeetingsBinding binder;

    public AddPropMeetingsFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddPropMeetingsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropMeetingsFrag fragment = new AddPropMeetingsFrag();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        binderMain = binding;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_meetings, container, false);

        initCalendar("he");

        return binder.getRoot();
    }

    private void initCalendar(String locale){
        long now = Calendar.getInstance().getTimeInMillis();
        binder.addPropMeetingsCalendarView.setMinDate(now);

        // change calendar to hebrew:
        Configuration hebrewConfiguration = getResources().getConfiguration();
        hebrewConfiguration.setLocale(new Locale(locale));
        getActivity().createConfigurationContext(hebrewConfiguration);
        Locale.setDefault(new Locale(locale));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
