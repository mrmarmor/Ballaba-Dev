package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttachmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttachmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttachmentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static AttachmentsFragment instance;
    private OnFragmentInteractionListener mListener;
    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private static Context context;
    private static ActivityMainLayoutBinding binder;

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance(Context mContext, ActivityMainLayoutBinding binding/*String param1, String param2*/) {
        AttachmentsFragment fragment = new AttachmentsFragment();
        context = mContext;
        binder = binding;
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }
    public static AttachmentsFragment getInstance(Context context, ActivityMainLayoutBinding binder){
        if(instance == null){
            instance = newInstance(context, binder);
        }
        return instance;
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
        View v = inflater.inflate(R.layout.fragment_attachments, container, false);
        animateScreen(false);

        return v;
    }

    private void animateScreen(boolean hasFocus){
        Guideline guideline = binder.mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = hasFocus? 0 : .5f;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = (ConstraintLayout)binder.mainActivityFilterRoot;
        ConstraintSet constraintSetHeight = new ConstraintSet();
        constraintSetHeight.clone(context, R.layout.search_filter_screen);
        constraintSetHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1

        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSetHeight.applyTo(constraintLayout);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        animateScreen(isVisibleToUser);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
