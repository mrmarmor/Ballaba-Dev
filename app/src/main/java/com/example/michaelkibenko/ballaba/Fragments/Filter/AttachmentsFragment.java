package com.example.michaelkibenko.ballaba.Fragments.Filter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Adapters.ChipsButtonsRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;

public class AttachmentsFragment extends Fragment implements Button.OnClickListener{
    public final static String ARG_PARAMS = "arg params";
    //private ConstraintLayout rootLayoutFullHeight, rootLayoutHalfHeight;
    private Context context;
    private RecyclerView attachmentsRecyclerView;
    //private View chipsItem;
    private ChipsButtonsRecyclerViewAdapter chipsAdapter;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private UiUtils uiUtils;
    private FilterResultEntity filterResult;

    public AttachmentsFragment() {}
    public static AttachmentsFragment newInstance(ArrayList<PropertyAttachmentAddonEntity> mItems) {
        AttachmentsFragment fragment = new AttachmentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAMS, mItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //filterResult = ((MainActivity)context).presenter.filterResult;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attachments, container, false);

        if (getArguments() != null && getArguments().getSerializable(ARG_PARAMS) instanceof ArrayList)
            items = (ArrayList<PropertyAttachmentAddonEntity>)getArguments().getSerializable(ARG_PARAMS);

        uiUtils = UiUtils.instance(false, context);

        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);
            ((FlowLayout)v.findViewById(R.id.attachments_flowLayout)).addView(chipsItem);
        }

        return v;
    }

    private Button initAttachment(Button chipsItem, PropertyAttachmentAddonEntity attachment){
        chipsItem.setId(Integer.parseInt(attachment.id));
        chipsItem.setText(attachment.formattedTitle);
        chipsItem.setOnClickListener(this);

        if(chipsItem.getTag() != null) {
            if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        } else {
            chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
        }

        return chipsItem;
    }

    private String getOriginalTitleByFormatted(String formatted){
        for (PropertyAttachmentAddonEntity item : items) {
            if(item.formattedTitle.equals(formatted)){
                return item.title;
            }
        }
        return null;
    }

    private PropertyAttachmentAddonEntity getHolderByOriginalTitle(String title){
        for (PropertyAttachmentAddonEntity item : items) {
            if(item.title.equals(title)){
                return item;
            }
        }
        return null;
    }

    private PropertyAttachmentAddonEntity getHolderByFormattedTitle(String formattedTitle){
        for (PropertyAttachmentAddonEntity item : items) {
            if(item.formattedTitle.equals(formattedTitle)){
                return item;
            }
        }
        return null;
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
        filterResult = new FilterResultEntity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private final String FURNISHED = "furnished", NOT_FURNISHED = "not_furnished", ELECTRONICS = "electronics", NO_ELECTRONICS = "no_electronics";
    @Override
    public void onClick(View v) {
        String state = (String)v.getTag();
        String text = ((Button) v).getText()+"";
        if(getOriginalTitleByFormatted(text).equals("furnished")) {
                //switch between furnished and not furnished where furnished is checked
            filterResult.setFurnished(!v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED));
            tweenButtonStatesChanger((Button)v, NOT_FURNISHED, FURNISHED);

        } else if(getOriginalTitleByFormatted(text).equals("not_furnished")){
                //switch between furnished and not furnished where not_furnished is checked
            filterResult.setFurnished(v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED));
            tweenButtonStatesChanger((Button)v, FURNISHED, FURNISHED);

        } else if(getOriginalTitleByFormatted(text).equals("electronics")) {
                //switch between electronics and not no_electronics where electronics is checked
            filterResult.setElectronics(!v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED));
            tweenButtonStatesChanger((Button)v, NO_ELECTRONICS, ELECTRONICS);

        } else if(getOriginalTitleByFormatted(text).equals("no_electronics")){
                //switch between electronics and not no_electronics where no_electronics is checked
            filterResult.setElectronics(v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED));
            tweenButtonStatesChanger((Button)v, ELECTRONICS, ELECTRONICS);
        }

        if (state.equals(UiUtils.ChipsButtonStates.PRESSED)){
            filterResult.deleteAttachmentId(getHolderByFormattedTitle(text).id);
        } else if(state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)){
            String id =  getHolderByFormattedTitle(text).id;
            if(!id.equals("1") && !id.equals("2") && !id.equals("11") && !id.equals("22")){
                filterResult.appendAttachmentId(id);
            }
        }
        uiUtils.onChipsButtonClick((Button)v, state);
    }

    private void tweenButtonStatesChanger(Button btn, String tweenButtonTitle, final String TYPE){
        PropertyAttachmentAddonEntity attachment = getHolderByOriginalTitle(tweenButtonTitle);
        Button tweenButton = ((ViewGroup)btn.getParent()).findViewById(Integer.parseInt(attachment.id));
        if (tweenButton != null) {
            checkIfBothButtonsUnselected(btn, tweenButton, TYPE);
            filterResult.deleteAttachmentId(attachment.id);
            tweenButton.setTag(UiUtils.ChipsButtonStates.PRESSED);
            uiUtils.onChipsButtonClick(tweenButton, (String)tweenButton.getTag());

        }
    }

    private boolean checkIfBothButtonsUnselected(Button btn, Button tweenButton, final String TYPE){
        if (btn.getTag().equals(UiUtils.ChipsButtonStates.PRESSED) &&
                !tweenButton.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
            if (TYPE.equals(FURNISHED))
                filterResult.setFurnished(null);
            else if (TYPE.equals(ELECTRONICS))
                filterResult.setElectronics(null);

            return true;
        }

        return false;
    }
}
