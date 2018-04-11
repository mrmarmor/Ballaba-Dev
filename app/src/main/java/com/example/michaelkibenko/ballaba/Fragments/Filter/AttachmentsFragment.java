package com.example.michaelkibenko.ballaba.Fragments.Filter;

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

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.ChipsButtonsRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.ArrayList;

public class AttachmentsFragment extends Fragment {
    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private Context context;
    private RecyclerView attachmentsRecyclerView;
    private ChipsButtonsRecyclerViewAdapter chipsAdapter;
    private UiUtils uiUtils;
    private FilterResultEntity filterResult;

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance() {
        AttachmentsFragment fragment = new AttachmentsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterResult = ((MainActivity)context).presenter.filterResult;
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
                    filterResult.deleteAttachmentId(holder.id);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                    filterResult.setFurnished(true);
                }else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("not_furnished")
                        && chipsAdapter.getHolderByOriginalTitle("furnished").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                    //switch between furnished and not furnished where not_furnished is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("furnished");
                    filterResult.deleteAttachmentId(holder.id);
                    filterResult.setFurnished(false);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                } else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("electronics")
                        && chipsAdapter.getHolderByOriginalTitle("no_electronics").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    //switch between electronics and not no_electronics where electronics is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("no_electronics");
                    filterResult.deleteAttachmentId(holder.id);
                    filterResult.setElectronics(true);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());

                }else if(chipsAdapter.getOriginalTitleByFormatted(((Button)v).getText()+"").equals("no_electronics")
                        && chipsAdapter.getHolderByOriginalTitle("electronics").chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                    //switch between electronics and not no_electronics where no_electronics is checked
                    ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder holder = chipsAdapter.getHolderByOriginalTitle("electronics");
                   filterResult.deleteAttachmentId(holder.id);
                    filterResult.setElectronics(false);
                    uiUtils.onChipsButtonClick(holder.chips, (String)holder.chips.getTag());
                }
                String text = ((Button) v).getText()+"";
                if(state.equals(UiUtils.ChipsButtonStates.PRESSED)){
                    filterResult.deleteAttachmentId(chipsAdapter.getHolderByFormattedTitle(text).id);
                }else if(state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)){
                    String id =  chipsAdapter.getHolderByFormattedTitle(text).id;
                    if(!id.equals("1") && !id.equals("2") && !id.equals("11") && !id.equals("22")){
                        filterResult.appendAttachmentId(id);
                    }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
