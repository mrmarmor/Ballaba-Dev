package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.app.Activity;
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
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.HashMap;

public class AttachmentsFragment extends Fragment implements View.OnClickListener{

    private static AttachmentsFragment instance;
    private static Activity activity;
    private static ActivityMainLayoutBinding binder;
    private BallabaFragmentListener listener;    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private Attachment[] attachments = new Attachment[5];

    private class Attachment{
        private View rootView;
        private TextView textView;
        private boolean isChipChecked;
    }

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance(Context mContext, ActivityMainLayoutBinding binding/*String param1, String param2*/) {
        AttachmentsFragment fragment = new AttachmentsFragment();
        activity = (Activity)mContext;
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
        FlowLayout flowLayout = v.findViewById(R.id.mainActivity_filter_attachments_flowLayout);
        UiUtils.instance(true, activity).setFilterBarVisibility(UiUtils.ScreenStates.HALF);
        for (int i=0; i<5; i++)
            addAttahmentToFlowLayout(flowLayout, "Furniture", i);

        return v;
    }

    private void addAttahmentToFlowLayout(FlowLayout flowLayout, String text, int num){
        final View view = activity.getLayoutInflater().inflate(R.layout.chip_with_x, null);
        attachments[num] = new Attachment();
        attachments[num].rootView = view;
        //final TextView tvAttachment = view.findViewById(R.id.chip_textView);
        attachments[num].textView = view.findViewById(R.id.chip_textView);
        attachments[num].textView.setText(text + num);
        //tvAttachment.setText(text);
        view.findViewById(R.id.chip_x_button).setVisibility(View.GONE);
        view.setOnClickListener(this);
        flowLayout.addView(view);
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
        UiUtils.instance(true, activity).setFilterBarVisibility(screenState);

        if (!isVisibleToUser){
            if (listener != null) {
                HashMap<String, String> results = new HashMap<>(20);
                for (int i = 0; i < 5; i++) {
                    results.put("filter attachment furniture" + i
                            , attachments[i].isChipChecked + "");
                }
                listener.onFragmentInteraction(results);
            }
        }
        //animateScreen(isVisibleToUser);
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

        //activity = (Activity)context;
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

    @Override
    public void onClick(View v) {
        for (Attachment attachment : attachments)
            if (v.getId() == attachment.rootView.getId()) {
                v.setBackgroundColor(attachment.isChipChecked ?
                        getResources().getColor(R.color.colorPrimary, activity.getTheme())
                      : getResources().getColor(R.color.colorAccent, activity.getTheme()));
                attachment.isChipChecked = !attachment.isChipChecked;
            }
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
