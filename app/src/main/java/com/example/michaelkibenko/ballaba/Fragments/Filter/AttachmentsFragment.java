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

import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

public class AttachmentsFragment extends Fragment {

    private static AttachmentsFragment instance;
    private static Context context;
    private static ActivityMainLayoutBinding binder;
    private BallabaFragmentListener listener;    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;

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
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attachments, container, false);
        UiUtils.instance(true, context).setFilterBarVisibility(UiUtils.ScreenStates.HALF);
        //animateScreen(false);

        return v;
    }

    /*private void animateScreen(boolean hasFocus){
        Guideline guideline = binder.mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = hasFocus? 0 : .5f;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = ((ConstraintLayout)binder.mainActivityFilterIncluded);
        ConstraintSet constraintSetHeight = new ConstraintSet();
        constraintSetHeight.clone(context, R.layout.search_filter_screen);
        constraintSetHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1

        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSetHeight.applyTo(constraintLayout);
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        float screenState = isVisibleToUser ? UiUtils.ScreenStates.FULL : UiUtils.ScreenStates.HALF;
        UiUtils.instance(true, context).setFilterBarVisibility(screenState);
        //animateScreen(isVisibleToUser);
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
    public void onDetach() {
        super.onDetach();
        listener = null;
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
