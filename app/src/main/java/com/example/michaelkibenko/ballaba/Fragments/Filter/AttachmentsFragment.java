package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.ChipsButtonsRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttachmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttachmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttachmentsFragment extends Fragment {
    private static AttachmentsFragment instance;
    private OnFragmentInteractionListener mListener;
    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private Context context;
    private ActivityMainLayoutBinding binder;
    private RecyclerView attachmentsRecyclerView;
    private ChipsButtonsRecyclerViewAdapter chipsAdapter;
    private UiUtils uiUtils;

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance(Context mContext, ActivityMainLayoutBinding binding) {
        AttachmentsFragment fragment = new AttachmentsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attachments, container, false);
        attachmentsRecyclerView = (RecyclerView)v.findViewById(R.id.attachmentsRV);
        uiUtils = UiUtils.instance(false, context);
        ArrayList<PropertyAttachmentAddonEntity> attachmentAddonEntities = PropertyAttachmentsAddonsHolder.getInstance().getAttachments();
        chipsAdapter = new ChipsButtonsRecyclerViewAdapter(context, attachmentAddonEntities, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = (String)v.getTag();
                if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("furnished")
                        && chipsAdapter.getHolderByOriginalTitle("not_furnished").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    //switch between furnished and not furnished where furnished is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("not_furnished");
                    ((MainActivity)context).presenter.filterResult.deleteAttachmentId(holder.id);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                }else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("not_furnished")
                        && chipsAdapter.getHolderByOriginalTitle("furnished").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                    //switch between furnished and not furnished where not_furnished is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("furnished");
                    ((MainActivity)context).presenter.filterResult.deleteAttachmentId(holder.id);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                } else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("electronics")
                        && chipsAdapter.getHolderByOriginalTitle("no_electronics").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    //switch between electronics and not no_electronics where electronics is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("no_electronics");
                    ((MainActivity)context).presenter.filterResult.deleteAttachmentId(holder.id);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());

                }else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("no_electronics")
                        && chipsAdapter.getHolderByOriginalTitle("electronics").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                    //switch between electronics and not no_electronics where no_electronics is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("electronics");
                    ((MainActivity)context).presenter.filterResult.deleteAttachmentId(holder.id);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                }
                String text = ((Button) v).getText()+"";
                if(state.equals(UiUtils.ChipsButtonStates.PRESSED)){
                    ((MainActivity)context).presenter.filterResult.deleteAttachmentId(chipsAdapter.getHolderByFormattedTitle(text).id);
                }else if(state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)){
                    ((MainActivity)context).presenter.filterResult.appendAttachmentId(chipsAdapter.getHolderByFormattedTitle(text).id);
                }
                uiUtils.onChipsButtonClick((Button)v, state);
            }
        });
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        attachmentsRecyclerView.setLayoutManager(gridLayoutManager);
        attachmentsRecyclerView.setAdapter(chipsAdapter);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        float screenState = isVisibleToUser ? UiUtils.ScreenStates.FULL : UiUtils.ScreenStates.HALF;
        //UiUtils.instance(true, context).setFilterBarVisibility(screenState);
        //animateScreen(isVisibleToUser);
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
        this.context = context;
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
