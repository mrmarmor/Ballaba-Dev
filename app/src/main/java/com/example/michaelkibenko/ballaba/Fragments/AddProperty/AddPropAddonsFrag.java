package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Adapters.ChipsButtonsRecyclerViewAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.Filter.AttachmentsFragment;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAddonsBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzbgp.NULL;

public class AddPropAddonsFrag extends Fragment implements Button.OnClickListener{
    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAddonsBinding binderAddons;
    public ChipsButtonsRecyclerViewAdapter adapter;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout furnitureRoot, electronicsRoot, extrasRoot;
    private ArrayList<PropertyAttachmentAddonEntity> furnitures, electronics, extras;

    public AddPropAddonsFrag() {}
    public static AddPropAddonsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAddonsFrag fragment = new AddPropAddonsFrag();
        binderMain = binding;
        /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = getLayoutInflater().inflate(R.layout.fragment_attachments)
        //Fragment v = getChildFragmentManager().findFragmentById(R.id.addProperty_addons_attachments_fragment);
        /*AttachmentsFragment fragment = new AttachmentsFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.addProperty_addons_root, fragment)
                .commit();*/

        binderAddons = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_addons, container, false);

        final View view = binderAddons.getRoot();

        //String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, null);
        //if (propertyId != null) {
        //    final ProgressDialog progressDialog = new ProgressDialog(context);
        //    progressDialog.setTitle("Fetching data from server...");
        //    progressDialog.show();

        //    ConnectionsManager.getInstance(context).getPropertyById(propertyId, new BallabaResponseListener() {
        //        @Override
        //        public void resolve(BallabaBaseEntity entity) {
                    initView(view);
        //            progressDialog.dismiss();
        //        }

        //        @Override
        //        public void reject(BallabaBaseEntity entity) {
        //            progressDialog.dismiss();
        //            showSnackBar();
        //        }
        //    });
        //}

        //final TagFlowLayout tagFlowLayout = getChildFragmentManager().findFragmentByTag("electronics").getView().findViewById(R.id.attachments_flowLayout);
        //AttachmentsFragment.newInstance().initChipButtons(, electronics);
        /*for (PropertyAttachmentAddonEntity item : electronics){
            ((FlowLayout)fragment.getView()).addView();
        }*/
        /*for (PropertyAttachmentAddonEntity attachment : attachmentAddonEntities) {
            final View chipsItem = getLayoutInflater().inflate(R.layout.chips_item, null);
            ((Button)chipsItem).setText(attachment.formattedTitle);
            //binderAddons.addPropertyAddonsFurnitureRV.addView(chipsItem);
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.addProperty_addons_attachments_fragment);
            FlowLayout flowLayout = fragment.getView().findViewById(R.id.attachments_furniture_flowLayout);
            flowLayout.addView(chipsItem);
            //fragment.getView().findViewById(R.id.)
            //binderAddons.addPropertyAddonsAttachmentsFragment;//.addView(chipsItem);
        }*/
        //initButtons(view);
        return view;
    }

    private void initView(View view){
        extras = PropertyAttachmentsAddonsHolder.getInstance().getAttachments();
        electronics = PropertyAttachmentsAddonsHolder.getInstance().getElectronics();
        furnitures = PropertyAttachmentsAddonsHolder.getInstance().getFurniture();

        furnitureRoot = view.findViewById(R.id.addProperty_addons_furniture_flowLayout);
        electronicsRoot = view.findViewById(R.id.addProperty_addons_electronics_flowLayout);
        extrasRoot = view.findViewById(R.id.addProperty_addons_extras_flowLayout);

        initButtons(furnitureRoot, furnitures);
        initButtons(electronicsRoot, electronics);
        initButtons(extrasRoot, extras);
        binderAddons.addPropertyAddonsButtonNext.setOnClickListener(this);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items){
        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chips_item, null);
            initAttachment(chipsItem, attachment);
            chipsItem.setWidth(attachment.formattedTitle.length()*5);
            Log.e("tagg", attachment.formattedTitle.length()+"");
            flowLayout.addView(chipsItem);
        }
    }

    private Button initAttachment(Button chipsItem, PropertyAttachmentAddonEntity attachment){
        if(chipsItem.getTag() != null) {
            if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        }else{
            chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
        }

        chipsItem.setText(attachment.formattedTitle);
        //UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());
        chipsItem.setOnClickListener(this);

        return chipsItem;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    private String getOriginalTitleByFormatted(String formatted){
        for (PropertyAttachmentAddonEntity item : items) {
            if(item.formattedTitle.equals(formatted)){
                return item.title;
            }
        }
        return "";
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
    public void onClick(View v) {
        if (v.getId() == R.id.addProperty_addons_button_next)
            onFinish();
        else {
            PropertyAttachmentsAddonsHolder attachments = PropertyAttachmentsAddonsHolder.getInstance();
            String state = (String) v.getTag();
            String itemParentTag = ((FlowLayout) v.getParent()).getTag() + "";
            if (itemParentTag.equals("furniture")) {
                items = attachments.getFurniture();
            } else if (itemParentTag.equals("electronics")) {
                items = attachments.getElectronics();
            } else if (itemParentTag.equals("extras")) {
                items = attachments.getAttachments();
            }

            UiUtils uiUtils = UiUtils.instance(false, context);
            FilterResultEntity filterResult = new FilterResultEntity();

            if (getOriginalTitleByFormatted(((Button) v).getText() + "").equals("furnished")
                    && v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                //switch between furnished and not furnished where furnished is checked
                PropertyAttachmentAddonEntity attachment = getHolderByOriginalTitle("not_furnished");
                filterResult.deleteAttachmentId(attachment.id);
                filterResult.setFurnished(true);
            } else if (getOriginalTitleByFormatted(((Button) v).getText() + "").equals("not_furnished")
                    && v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                //switch between furnished and not furnished where not_furnished is checked
                PropertyAttachmentAddonEntity attachment = getHolderByOriginalTitle("furnished");
                filterResult.deleteAttachmentId(attachment.id);
                filterResult.setFurnished(false);
            } else if (getOriginalTitleByFormatted(((Button) v).getText() + "").equals("electronics")
                    && v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                //switch between electronics and not no_electronics where electronics is checked
                PropertyAttachmentAddonEntity attachment = getHolderByOriginalTitle("no_electronics");
                filterResult.deleteAttachmentId(attachment.id);
                filterResult.setElectronics(true);

            } else if (getOriginalTitleByFormatted(((Button) v).getText() + "").equals("no_electronics")
                    && v.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                //switch between electronics and not no_electronics where no_electronics is checked
                PropertyAttachmentAddonEntity attachment = getHolderByOriginalTitle("electronics");
                filterResult.deleteAttachmentId(attachment.id);
                filterResult.setElectronics(false);
            }
            uiUtils.onChipsButtonClick((Button) v, (String) v.getTag());

            String text = ((Button) v).getText() + "";
            if (state.equals(UiUtils.ChipsButtonStates.PRESSED)) {
                filterResult.deleteAttachmentId(getHolderByFormattedTitle(text).id);
            } else if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)) {
                String id = getHolderByFormattedTitle(text).id;
                if (!id.equals("1") && !id.equals("2") && !id.equals("11") && !id.equals("22")) {
                    filterResult.appendAttachmentId(id);
                }
            }
            uiUtils.onChipsButtonClick((Button) v, state);
        }
    }

    private void onFinish(){
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, NULL);
        final Data data = getDataFromChips(new Data());
        data.property_id = Integer.parseInt(propertyId);

        //if (!isDataEqual(data))
            ConnectionsManager.getInstance(context).uploadPropertyIntegers(data,  "addons", new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_ID, ((BallabaPropertyFull)entity).id);
                    //TODO update property updating date on SharedPrefs??
                    new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    showSnackBar();

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                }
            });

    }

    private Data getDataFromChips(Data data){
        ArrayList<Integer> chipsIds = getDataFromChipsSection(new ArrayList<Integer>(), furnitureRoot, furnitures);
        chipsIds.addAll(getDataFromChipsSection(new ArrayList<Integer>(), electronicsRoot, electronics));
        data.attachments.addAll(chipsIds);

        data.addons.addAll(getDataFromChipsSection(new ArrayList<Integer>(), extrasRoot, extras));

        return data;
    }

    private ArrayList<Integer> getDataFromChipsSection(ArrayList<Integer> chipsIds, FlowLayout layout
            , ArrayList<PropertyAttachmentAddonEntity> chips){

        for (int i = 0; i < layout.getChildCount(); i++){
            if (layout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                chipsIds.add(Integer.parseInt(chips.get(i).id));
            }
        }

        return chipsIds;
    }

    private void showSnackBar(){
        final View snackBarView = binderAddons.addPropertyAddonsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    /*private boolean isDataEqual(HashMap<String, ArrayList<String>> map){
        boolean isEqual = true;

        //BallabaPropertyFull property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();
        ArrayList<String> furnituresNew = map.get("furniture");//[3,5,8] from chips
        ArrayList<String> electronicsNew = map.get("electronics");
        ArrayList<String> extrasNew = map.get("extras");

        if (furnituresNew.contains(furnitures))
        *//*for (String value : furnituresNew){
            if (!value.equals(furnitures))
        }*//*
        return (property != null &&
                .equals(property.addons.) &&
                map.get("street").equals(property.street) &&
                map.get("apartment").equals(property.street_number) &&//TODO marik wrote "appartment" which is typo error
                map.get("floor").equals(property.floor) &&
                map.get("max_floor").equals(property.max_floor) &&
                map.get("rooms").equals(property.roomsNumber) &&
                map.get("toilets").equals(property.toilets) &&
                map.get("bathrooms").equals(property.bathrooms) &&
                map.get("size").equals(property.size) &&
                map.get("entry_date").equals(property.entry_date) &&
                map.get("rent_period").equals(property.rentPeriod));
        //TODO missing value in property: map.get("is_extendable").equals(property.));
    }*/

    public class Data {
        public Integer property_id;
        public ArrayList<Integer> attachments;
        public ArrayList<Integer> addons;

        private Data(){
            attachments = new ArrayList<>();
            addons = new ArrayList<>();
        }

        /*public void setProperty_id(HashMap<String, Integer> property_id) {
            this.property_id = property_id;
        }
        public void setAttachments(HashMap<String, ArrayList<Integer>> attachments) {
            this.attachments = attachments;
        }
        public void setAddons(HashMap<String, ArrayList<Integer>> addons) {
            this.addons = addons;
        }*/

        /*public Data(HashMap<String, Integer> property_id, HashMap<String, ArrayList<Integer>> attachments, HashMap<String, ArrayList<Integer>> addons) {
            this.property_id = property_id;
            this.attachments = attachments;
            this.addons = addons;
        }*/
    }
}
