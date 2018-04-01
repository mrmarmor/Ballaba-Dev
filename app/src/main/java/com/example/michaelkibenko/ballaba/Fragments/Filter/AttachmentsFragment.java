package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

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
    private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private View v;
    private static Context context;
    private static ActivityMainLayoutBinding binder;
    private boolean hasFocus;

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
        v = inflater.inflate(R.layout.fragment_attachments, container, false);
        //rootLayoutFullHeight = v.findViewById(R.id.filter_rootLayout_full_height);
        animateScreen(hasFocus);

        rootLayoutFullHeight = v.findViewById(R.id.fragmentAttachments_rootLayout);
        rootLayoutHalfHeight = v.findViewById(R.id.filter_rootLayout_half_height);
        return v;
    }

    private void animateScreen(boolean hasFocus){
        Guideline guideline = binder.mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = hasFocus? 0 : -50;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = binder.mainActivityFilterRoot;
        ConstraintSet constraintSetFullHeight = new ConstraintSet();
        constraintSetFullHeight.clone(context, R.layout.filter_screen_full_height);
        constraintSetFullHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1



   /*     Guideline guideline = new Guideline(context);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        lp.guidePercent = hasFocus? 0 : 50;
        lp.orientation = LinearLayout.VERTICAL;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = v.findViewById(R.id.fragmentAttachments_rootLayout);
        ConstraintSet constraintSetFullHeight = new ConstraintSet();
        constraintSetFullHeight.clone(getContext(), R.layout.filter_screen_full_height);
        constraintSetFullHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1
*/
        /*ConstraintSet constraintSetHalfHeight = new ConstraintSet();
        constraintSetHalfHeight.clone(rootLayoutHalfHeight);
        ConstraintSet constraintSet = hasFocus? constraintSetFullHeight : constraintSetHalfHeight;
        ConstraintLayout constraintLayout = hasFocus? rootLayoutFullHeight : rootLayoutHalfHeight;
      */  //constraintSet.centerVertically(R.id.tv, 0);
        //constraintSetFullHeight.connect(R.id.fragmentAttachments_rootLayout, ConstraintSet.TOP, R.id.mainActivity_rootLayout, ConstraintSet.TOP,0);

        //constraintSet.setVerticalBias(R.id.tv, 1.5f);
        //constraintSet.setHorizontalBias(R.id.tv, 0.5f);

        AutoTransition transition = new AutoTransition();
        transition.setDuration(100);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());



        TransitionManager.beginDelayedTransition(constraintLayout, transition);
        constraintSetFullHeight.applyTo(constraintLayout);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        hasFocus = isVisibleToUser;
        if (v != null)
            animateScreen(hasFocus);
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
