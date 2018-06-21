package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddPropAddonsFrag extends Fragment implements Button.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final String TAG = AddPropAddonsFrag.class.getSimpleName(), NOT_FURNISHED_TAG = "not_furnished_tag";
    private Context context;
    /*private ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAddonsBinding binderAddons;*/
    //public ChipsButtonsRecyclerViewAdapter adapter;
    //private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout furnitureRoot, electronicsRoot, extrasRoot;
    private ArrayList<PropertyAttachmentAddonEntity> furniture, electronics, attachments;
    private boolean wasDataChanged = false;
    private FlowLayout furnitureFlowLayout;
    private Button petBTN;
    private boolean petBtnClicked;
    private Switch furnitureSwitch, electronicsSwitch, petSwitch;
    private TextView furnitureTV, electronicsTV, petTV;


    public static AddPropAddonsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAddonsFrag fragment = new AddPropAddonsFrag();
        return fragment;
    }

    public AddPropAddonsFrag setMainBinder(ActivityAddPropertyBinding binder) {
        //this.binderMain = binder;
        return this;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_prop_addons, container, false);

        context = getActivity();
        ((AddPropertyActivityNew) context).changePageIndicatorText(3);

        furnitureFlowLayout = view.findViewById(R.id.addProperty_addons_furniture_flowLayout);
        petBTN = view.findViewById(R.id.addProperty_addons_pet_button);
        petBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petBtnClicked = !petBtnClicked;
                Resources resources = context.getResources();
                petBTN.setBackground(petBtnClicked ? resources.getDrawable(R.drawable.chips_button_pressed) : resources.getDrawable(R.drawable.chips_button));
                petBTN.setTextColor(petBtnClicked ? Color.WHITE : resources.getColor(R.color.colorPrimary));
            }
        });

        initView(view);

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

    private void initView(View view) {
        attachments = PropertyAttachmentsAddonsHolder.getInstance().getAttachments();
        electronics = PropertyAttachmentsAddonsHolder.getInstance().getElectronics();
        furniture = PropertyAttachmentsAddonsHolder.getInstance().getFurniture();

        furnitureTV = view.findViewById(R.id.addProperty_addons_furniture_text_view);
        furnitureSwitch = view.findViewById(R.id.addProperty_addons_furniture_switch);
        furnitureSwitch.setOnCheckedChangeListener(this);

        electronicsTV = view.findViewById(R.id.addProperty_addons_electronics_text_view);
        electronicsSwitch = view.findViewById(R.id.addProperty_addons_electronics_switch);
        electronicsSwitch.setOnCheckedChangeListener(this);

        petTV = view.findViewById(R.id.addProperty_addons_pet_text_view);
        petSwitch = view.findViewById(R.id.addProperty_addons_pet_switch);
        petSwitch.setOnCheckedChangeListener(this);

        view.findViewById(R.id.addProperty_addons_button_next).setOnClickListener(this);

                //removes furnished+not+electronics+not attachments. these attachments are used in filter search module.
        for (int i = 0; i < 4; i++)
            if (!attachments.isEmpty())
                attachments.remove(0);

        furnitureRoot = view.findViewById(R.id.addProperty_addons_furniture_flowLayout);
        electronicsRoot = view.findViewById(R.id.addProperty_addons_electronics_flowLayout);
        extrasRoot = view.findViewById(R.id.addProperty_addons_extras_flowLayout);

        initButtonNotFurnished((Button) getLayoutInflater().inflate(R.layout.chip_regular, null));
        initButtons(furnitureRoot, furniture);
        initButtons(electronicsRoot, electronics);
        initButtons(extrasRoot, attachments);
        furnitureFlowLayout.setOnClickListener(this);
    }

    private void initButtonNotFurnished(Button chipsItem) {
        //PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity("not_furnished_id"
        //        , "not_furnished", getString(R.string.attach_not_furnished));
        chipsItem.setText(getString(R.string.attach_not_furnished));//entity.formattedTitle);
        chipsItem.setTag(NOT_FURNISHED_TAG);
        chipsItem.setOnClickListener(this);
        UiUtils.instance(false, context).onChipsButtonClick(chipsItem, NOT_FURNISHED_TAG);
        furnitureRoot.addView(chipsItem, 0);
        //furniture.add(0, entity);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items) {
        BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();

        //TODO if user selected a property in propertyDescription, maybe propertyFull is not null although it should be!!
        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button) getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);
            highlightChipsUserSelectedLastTime(propertyFull, chipsItem, attachment);

            flowLayout.addView(chipsItem);
        }

    }

    private Button initAttachment(Button chipsItem, PropertyAttachmentAddonEntity attachment) {
        if (chipsItem.getTag() != null) {
            if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        } else {
            chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
        }

        chipsItem.setText(attachment.formattedTitle);
        chipsItem.setOnClickListener(this);

        return chipsItem;
    }

    private void highlightChipsUserSelectedLastTime(BallabaPropertyFull property, Button chipsItem, PropertyAttachmentAddonEntity attachment) {
        //these loops highlight chips that user had selected last time
        if (property != null) {
            for (HashMap<String, String> map : property.addons)
                if (attachment.id.equals(map.get("addon_type")))
                    chipsItem = UiUtils.instance(false, context)
                            .onChipsButtonClick(chipsItem, (String) chipsItem.getTag());

            for (String string : property.attachments)
                if (attachment.id.equals(string))
                    chipsItem = UiUtils.instance(false, context)
                            .onChipsButtonClick(chipsItem, (String) chipsItem.getTag());
        }
    }


    @Override
    public void onClick(View v) {
        Button buttonNotFurnished = (Button) furnitureRoot.getChildAt(0);

        if (v.getId() == R.id.addProperty_addons_button_next) {
            onFinish();
        } else if (v.getTag().equals(NOT_FURNISHED_TAG)) {
            unselectAllFurnitureButtons(furnitureFlowLayout);
            highlightNotFurnishedButton(buttonNotFurnished);
        } else {
            wasDataChanged = true;
            //PropertyAttachmentsAddonsHolder attachments = PropertyAttachmentsAddonsHolder.getInstance();
            String state = (String) v.getTag();
          /*  String itemParentTag = ((FlowLayout) v.getParent()).getTag() + "";
            if (itemParentTag.equals("furniture")) {
                items = attachments.getFurniture();
            } else if (itemParentTag.equals("electronics")) {
                items = attachments.getElectronics();
            } else if (itemParentTag.equals("attachments")) {
                items = attachments.getAttachments();
            }*/
            if (v instanceof Button)
            UiUtils.instance(false, context).onChipsButtonClick((Button) v, state);

            if (areAllFurnitureButtonsUnselected()) {
                highlightNotFurnishedButton(buttonNotFurnished);
            } else {
                buttonNotFurnished.setBackgroundResource(R.drawable.chips_button);
                buttonNotFurnished.setTextColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
            }
        }
    }

    private void onFinish() {
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, "-1");
        final Data data = getDataFromChips(new Data());
        data.property_id = Integer.parseInt(propertyId);

        JSONObject object = new JSONObject();

        try {
            JSONObject furniture = new JSONObject();
            int[] furnitureIDsArr = convertIntegers(data.furniture);
            furniture.put("data" , new JSONArray((Arrays.toString(furnitureIDsArr))));
            furniture.put("is_flexible" , furnitureSwitch.isChecked());

            JSONObject electronics = new JSONObject();
            int[] electronicsIDsArr = convertIntegers(data.electronics);
            electronics.put("data" , new JSONArray(Arrays.toString(electronicsIDsArr)));
            electronics.put("is_flexible" , electronicsSwitch.isChecked());

            JSONObject attachments = new JSONObject();
            int[] attachmentsIDsArr = convertIntegers(data.attachments);
            attachments.put("data" , new JSONArray(Arrays.toString(attachmentsIDsArr)));

            JSONObject is_pets_allowed = new JSONObject();
            is_pets_allowed.put("data" , petBtnClicked);
            is_pets_allowed.put("is_flexible" , petSwitch.isChecked());

            object.put("furniture" , furniture);
            object.put("electronics" , electronics);
            object.put("attachments" , attachments);
            object.put("is_pets_allowed" , is_pets_allowed);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String propID = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID , null);

        if (wasDataChanged) {
            ConnectionsManager.getInstance(context).newUploadProperty(2 , propID , object, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    //TODO update property updating date on SharedPrefs??
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "3");
                    //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).setViewPagerItem(3);
                    ((AddPropertyActivityNew) getActivity()).changeFragment(new AddPropPaymentsFrag(), true);

                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    /*((BaseActivity)context).getDefaultSnackBar(binderAddons.addPropertyAddonsRoot
                            , ((BallabaErrorResponse) entity).message, false);*/

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).onNextViewPagerItem(2);
                }
            });
        } else {
            //AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).setViewPagerItem(3);
        }
    }

    public int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }


    private Data getDataFromChips(Data data) {
        //ArrayList<Integer> chipsIds = getDataFromChipsSection(new ArrayList<Integer>(), furnitureRoot, furniture);
        //chipsIds.addAll(getDataFromChipsSection(new ArrayList<Integer>(), electronicsRoot, electronics));
        //data.attachments.addAll(getDataFromChipsSection(new ArrayList<Integer>(), extrasRoot, attachments));

        data.setFurniture(getDataFromChipsSection(new ArrayList<Integer>() , furnitureRoot , furniture));
        data.setElectronics(getDataFromChipsSection(new ArrayList<Integer>(), electronicsRoot, electronics));
        data.setAttachments(getDataFromChipsSection(new ArrayList<Integer>(), extrasRoot, attachments));

        return data;
    }



    private ArrayList<Integer> getDataFromChipsSection(ArrayList<Integer> chipsIds, FlowLayout layout
            , ArrayList<PropertyAttachmentAddonEntity> chips) {

        for (int i = 1; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsIds.add(Integer.parseInt(chips.get(i - 1).id));//TODO may invoke array out of bounds!
            }
        }

        return chipsIds;
    }

    private JSONObject jsonParse(Data propertyData, String step) {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject innerObject = new JsonObject();

            innerObject.addProperty("property_id", propertyData.property_id);
            innerObject.add("addons", new Gson().toJsonTree(propertyData.furniture));
            innerObject.add("attachments", new Gson().toJsonTree(propertyData.attachments));

            jsonObject.addProperty("step", step);
            jsonObject.add("data", innerObject);

            return new JSONObject(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //click on "not furnished" button will unselect all furniture buttons
    private void unselectAllFurnitureButtons(FlowLayout furnitureRoot) {
        for (int i = 1; i < furnitureRoot.getChildCount(); i++) {
            if (furnitureRoot.getChildAt(i) instanceof Button) {
                Button button = (Button) furnitureRoot.getChildAt(i);
                UiUtils.instance(false, context).onChipsButtonClick(button, UiUtils.ChipsButtonStates.PRESSED);
            }
        }
    }

    private void highlightNotFurnishedButton(Button buttonNotFurnished) {
        buttonNotFurnished.setBackgroundResource(R.drawable.chips_button_pressed);
        buttonNotFurnished.setTextColor(getResources().getColor(android.R.color.white, context.getTheme()));
    }

    private boolean areAllFurnitureButtonsUnselected() {
        boolean allButtonsUnselected = true;
        for (int i = 1; i < furnitureRoot.getChildCount(); i++) {
            if (furnitureRoot.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED))
                allButtonsUnselected = false;
        }

        return allButtonsUnselected;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.addProperty_addons_furniture_switch:
                furnitureTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_addons_electronics_switch:
                electronicsTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_addons_pet_switch:
                petTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
        }
    }

    //TODO replace this method with a similar method in BaseActivity
    /*private void showSnackBar(final String message){
        final View snackBarView = binderAddons.addPropertyAddonsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }*/

    /*private boolean isDataEqual(HashMap<String, ArrayList<String>> map){
        boolean isEqual = true;

        //BallabaPropertyFull property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();
        ArrayList<String> furnituresNew = map.get("furniture");//[3,5,8] from chips
        ArrayList<String> electronicsNew = map.get("electronics");
        ArrayList<String> extrasNew = map.get("attachments");

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

        private ArrayList<Integer> furniture;
        private ArrayList<Integer> electronics;
        private ArrayList<Integer> attachments;

        private Data() {
            furniture = new ArrayList<>();
            electronics = new ArrayList<>();
            attachments = new ArrayList<>();
        }

        public void setFurniture(ArrayList<Integer> furniture) {
            this.furniture = furniture;
        }

        public void setElectronics(ArrayList<Integer> electronics) {
            this.electronics = electronics;
        }

        public void setAttachments(ArrayList<Integer> attachments) {
            this.attachments = attachments;
        }
    }

}
