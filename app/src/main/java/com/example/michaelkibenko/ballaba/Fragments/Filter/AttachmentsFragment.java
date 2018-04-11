package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.ChipsButtonsRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.ArrayList;

public class AttachmentsFragment extends Fragment {
    private static AttachmentsFragment instance;
    //private OnFragmentInteractionListener mListener;
    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private static Context context;
    private RecyclerView attachmentsRecyclerView;
    private ChipsButtonsRecyclerViewAdapter chipsAdapter;
    private UiUtils uiUtils;

    private static ActivityMainLayoutBinding binder;
    private BallabaFragmentListener listener;
    private Attachment[] attachments = new Attachment[5];

    private class Attachment{
        private View rootView;
        private TextView textView;
        private boolean isChipChecked;
    }

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance(Context mContext, ActivityMainLayoutBinding binding) {
        AttachmentsFragment fragment = new AttachmentsFragment();
        context = mContext;
        binder = binding;

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

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

}
