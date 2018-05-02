package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.FilterResultEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzbgp.NULL;

public class AddPropAddonsFrag extends Fragment implements Button.OnClickListener{
    private final String TAG = AddPropAddonsFrag.class.getSimpleName(), NOT_FURNISHED_TAG = "not_furnished_tag";
    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAddonsBinding binderAddons;
    //public ChipsButtonsRecyclerViewAdapter adapter;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout furnitureRoot, electronicsRoot, extrasRoot;
    private ArrayList<PropertyAttachmentAddonEntity> furniture, electronics, extras;
    private boolean wasDataChanged = false;

    public AddPropAddonsFrag() {}
    public static AddPropAddonsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAddonsFrag fragment = new AddPropAddonsFrag();
        binderMain = binding;

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
            final View chipsItem = getLayoutInflater().inflate(R.layout.chip_regular, null);
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
        furniture = PropertyAttachmentsAddonsHolder.getInstance().getFurniture();

        furnitureRoot = view.findViewById(R.id.addProperty_addons_furniture_flowLayout);
        electronicsRoot = view.findViewById(R.id.addProperty_addons_electronics_flowLayout);
        extrasRoot = view.findViewById(R.id.addProperty_addons_extras_flowLayout);

        initButtonNotFurnished((Button)getLayoutInflater().inflate(R.layout.chip_regular, null));
        initButtons(furnitureRoot, furniture);
        initButtons(electronicsRoot, electronics);
        initButtons(extrasRoot, extras);
        binderAddons.addPropertyAddonsButtonNext.setOnClickListener(this);
    }

    private void initButtonNotFurnished(Button chipsItem){
        PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity("not_furnished_id"
                , "not_furnished", getString(R.string.attach_not_furnished));
        chipsItem.setText(entity.formattedTitle);
        chipsItem.setTag(NOT_FURNISHED_TAG);
        chipsItem.setOnClickListener(this);
        UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());
        furnitureRoot.addView(chipsItem);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items){
        BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();

        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);

            for (HashMap<String, String> map : propertyFull.addons)
                if (attachment.id.equals(map.get("addon_type")))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());

            for (String string : propertyFull.attachments)
                if (attachment.id.equals(string))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());

            //highlightSavedButtons(flowLayout, attachment);

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

        chipsItem.setOnClickListener(this);

        return chipsItem;
    }

    private void highlightSavedButtons(FlowLayout flowLayout, PropertyAttachmentAddonEntity attachment){

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
        else if (v.getTag().equals(NOT_FURNISHED_TAG))
            unselectAllFurnitureButtons(binderAddons.addPropertyAddonsFurnitureFlowLayout);
        else {
            wasDataChanged = true;
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

            UiUtils.instance(false, context).onChipsButtonClick((Button) v, state);
        }
    }

    private void onFinish(){
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, NULL);
        final Data data = getDataFromChips(new Data());
        data.property_id = Integer.parseInt(propertyId);

        if (wasDataChanged) {
            ConnectionsManager.getInstance(context).uploadProperty(jsonParse(data, "addons"), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    //TODO update property updating date on SharedPrefs??
                    //SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_ID, ((BallabaPropertyFull)entity).id);
                    AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).getDataFromFragment(/*data, */2);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    showSnackBar(((BallabaErrorResponse) entity).message);

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                }
            });
        } else {
            AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).getDataFromFragment(/*data, */2);
        }
    }

    private Data getDataFromChips(Data data){
        ArrayList<Integer> chipsIds = getDataFromChipsSection(new ArrayList<Integer>(), furnitureRoot, furniture);
        chipsIds.addAll(getDataFromChipsSection(new ArrayList<Integer>(), electronicsRoot, electronics));
        data.addons.addAll(chipsIds);

        data.attachments.addAll(getDataFromChipsSection(new ArrayList<Integer>(), extrasRoot, extras));

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

    private JSONObject jsonParse(Data propertyData, String step){
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject innerObject = new JsonObject();

            innerObject.addProperty("property_id", propertyData.property_id);
            innerObject.add("addons", new Gson().toJsonTree(propertyData.addons));
            innerObject.add("attachments", new Gson().toJsonTree(propertyData.attachments));

            jsonObject.addProperty("step", step);
            jsonObject.add("data", innerObject);

            return new JSONObject(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void unselectAllFurnitureButtons(FlowLayout furnitureRoot){
        for (int i = 1; i < furnitureRoot.getChildCount(); i++){
            if (furnitureRoot.getChildAt(i) instanceof Button) {
                Button button = (Button) furnitureRoot.getChildAt(i);
                UiUtils.instance(false, context).onChipsButtonClick(button, UiUtils.ChipsButtonStates.PRESSED);
            }

        }
    }

    private void showSnackBar(final String message){
        final View snackBarView = binderAddons.addPropertyAddonsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
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
