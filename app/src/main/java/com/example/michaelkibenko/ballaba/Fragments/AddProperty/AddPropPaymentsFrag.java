package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
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
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzbgp.NULL;

public class AddPropPaymentsFrag extends Fragment implements Button.OnClickListener{
    private final String TAG = AddPropPaymentsFrag.class.getSimpleName(), ALL_INCLUDED = "all_included";

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropPaymentsBinding binderPay;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout paymentsRoot, paymentMethodsRoot;
    private ArrayList<PropertyAttachmentAddonEntity> payments, paymentMethods;
    private BallabaPropertyFull property;
    private boolean wasPaymentsChanged = false;

    public AddPropPaymentsFrag() {}
    public static AddPropPaymentsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropPaymentsFrag fragment = new AddPropPaymentsFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binderPay = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_payments, container, false);
        View view = binderPay.getRoot();
        property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();

        initView(view);
        return view;
    }

    private void initView(View view){
        payments = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();
        paymentMethods = PropertyAttachmentsAddonsHolder.getInstance().getPaymentMethods();

        paymentsRoot = view.findViewById(R.id.addProperty_payments_included_flowLayout);
        paymentMethodsRoot = view.findViewById(R.id.addProperty_payments_methods_flowLayout);

        initButtonAllIncluded((Button)getLayoutInflater().inflate(R.layout.chip_regular, null));
        initButtons(paymentsRoot, payments);
        initButtons(paymentMethodsRoot, paymentMethods);
        initEditTexts();
        binderPay.addPropertyPaymentsButtonNext.setOnClickListener(this);
    }

    private void initButtonAllIncluded(Button chipsItem){
        PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity("all_included_id"
                , ALL_INCLUDED, getString(R.string.attach_all_included));
        chipsItem.setText(entity.formattedTitle);
        chipsItem.setOnClickListener(this);
        UiUtils.instance(false, context).onChipsButtonClick(chipsItem, UiUtils.ChipsButtonStates.PRESSED);
        paymentsRoot.addView(chipsItem, 0);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items){
        /*TypedArray ids = context.getResources().obtainTypedArray(R.array.buttons_ids);
        int[] resIds = new int[ids.length()];
        for (int i = 0; i < ids.length(); i++)
            resIds[i] = ids.getResourceId(i, 0);
        ids.recycle();*/

        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);

            //TODO set 1 payment_method chip to be selected at initialization

            //these 2 loops highlight chips that were selected last upload
            for (HashMap<String, String> map : property.payments)
                if (attachment.id.equals(map.get("payment_types")))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());

            for (HashMap<String, String> map : property.paymentMethods)
                if (attachment.id.equals(map.get("payment_methods")))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String)chipsItem.getTag());

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

    //TODO on viewPager state of screen kept only for current screen and next screen. so if i scroll 2 screens state will not be kept. maybe i shall saveInstance
    private void initEditTexts(){
        if (property != null) {
            PropertyAttachmentsAddonsHolder entity = PropertyAttachmentsAddonsHolder.getInstance();

            binderPay.addPropPaymentsMunicipalityEditText.setText(
                    entity.getFormattedTitleById(payments, payments.get(0).id));
            binderPay.addPropPaymentsHouseCommitteeEditText.setText(
                    entity.getFormattedTitleById(payments, payments.get(1).id));
            binderPay.addPropPaymentsManagementEditText.setText(
                    entity.getFormattedTitleById(payments, payments.get(2).id));
            binderPay.addPropertyPaymentsParkingNoEditText.setText(property.no_of_parking);
            binderPay.addPropertyPaymentsParkingPriceEditText.setText(property.parking_price);
            binderPay.addPropPaymentsRentalFeeEditText.setText(property.price);
            binderPay.addPropPaymentsFreeTextEditText.setText(property.description);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private String getOriginalTitleByFormatted(String formatted){
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.formattedTitle.equals(formatted)) {
                    return item.title;
                }
            }
        }
        return "";
    }

    private PropertyAttachmentAddonEntity getHolderByOriginalTitle(String title){
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.title.equals(title)) {
                    return item;
                }
            }
        }
        return null;
    }

    private PropertyAttachmentAddonEntity getHolderByFormattedTitle(String formattedTitle){
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.formattedTitle.equals(formattedTitle)) {
                    return item;
                }
            }
        }

        /*if (formattedTitle.equals(getString(R.string.attach_all_included)))//user clicked "all_included" button
            return binderPay.addPropertyPaymentsIncludedFlowLayout.getChildAt(0);

    */  return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addProperty_payments_button_next)
            onFinish();
        else {
            wasPaymentsChanged = true;
            Button btn = (Button)v;
            PropertyAttachmentsAddonsHolder attachments = PropertyAttachmentsAddonsHolder.getInstance();
            String state = (String)btn.getTag();

            //initialize arrays for "getHolderByOriginalTitle" according to user click
            String itemParentTag = ((FlowLayout) btn.getParent()).getTag() + "";
            if (itemParentTag.equals("payments")) {
                items = attachments.getPaymentTypes();
            } else if (itemParentTag.equals("payment_methods")) {
                items = attachments.getPaymentMethods();
            }

            UiUtils.instance(false, context).onChipsButtonClick(btn, state);

            //arnona + house committee + management fee:
            paymentsEditTextsSetEnabled(getHolderByFormattedTitle(btn.getText()+""), btn.getTag()+"");

            onAllIncludedButtonClick(binderPay.addPropertyPaymentsIncludedFlowLayout, btn, state);
            PropertyAttachmentAddonEntity entity = getHolderByFormattedTitle(btn.getText()+"");
            if (entity != null)
                onAllIncludedGroupMemberButtonClick(entity.id);

        }
    }

    private void onFinish(){
        //String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, NULL);
        final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
        //data.property_id = Integer.parseInt(propertyId);

        if (wasPaymentsChanged || !areEditTextsEqual(data)) {
            ConnectionsManager.getInstance(context).uploadProperty(jsonParse(data, "payments"), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    //TODO update property updating date on SharedPrefs??
                    //SharedPreferencesManager.getInstance(context).removeString(SharedPreferencesKeysHolder.PROPERTY_ID);
                    AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).getDataFromFragment(4);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    showSnackBar();

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                }
            });
        } else {
            AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).getDataFromFragment(3);
        }
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> data){
        LinearLayout root = binderPay.addPropertyPaymentsRoot;
        for (int i = 0; i < root.getChildCount(); i++) {
            try {
                for (int j = 0; j < ((ViewGroup)root.getChildAt(i)).getChildCount(); j++) {
                    View v = ((ViewGroup)root.getChildAt(i)).getChildAt(j);
                    if (v instanceof EditText) {
                        data.put(v.getTag() + "", ((EditText) v).getText() + "");
                    }
                }
            } catch (ClassCastException e){
                Log.e(TAG, e.getMessage()+"\n class is:" + root.getChildAt(i).getClass());
            }
        }

        Spinner spinner = binderPay.addPropertyPaymentsTimeSpinner;
        PropertyAttachmentAddonEntity entity = getHolderByFormattedTitle(spinner.getPrompt()+"");
        if (entity != null)
            data.put(spinner.getTag() + "", entity.id);

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

    private boolean areEditTextsEqual(HashMap<String, String> map){
        BallabaPropertyFull property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();
        PropertyAttachmentsAddonsHolder entity = PropertyAttachmentsAddonsHolder.getInstance();
        return (property != null &&
                map.get("no_of_payments").equals(property.numberOfPayments) &&
                map.get("arnona").equals(entity.getFormattedTitleById(payments, payments.get(0).id)) &&
                map.get("house_committee").equals(entity.getFormattedTitleById(payments, payments.get(1).id)) &&//TODO marik wrote "appartment" which is typo error
                map.get("managment").equals(entity.getFormattedTitleById(payments, payments.get(2).id)) &&
                map.get("no_of_parking").equals(property.no_of_parking) &&
                map.get("parking_price").equals(property.parking_price) &&
                map.get("rent_price").equals(property.price) &&
                map.get("description").equals(property.description));
    }

    private JSONObject jsonParse(HashMap<String, String> propertyData, String step){
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, NULL);

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject innerObject = new JsonObject();

            innerObject.addProperty("property_id", Integer.parseInt(propertyId));
            /*
            innerObject.add("addons", new Gson().toJsonTree(propertyData.addons));
            innerObject.add("attachments", new Gson().toJsonTree(propertyData.attachments));
*/
            jsonObject.addProperty("step", step);
            jsonObject.add("data", innerObject);

            return new JSONObject(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //set some payments editTexts enabled\disabled
    private void paymentsEditTextsSetEnabled(PropertyAttachmentAddonEntity entity, String state){
        if (entity == null)
            return;

        if (entity.title.equals("arnona"))
            binderPay.addPropPaymentsMunicipalityEditText.setEnabled(!state.equals(UiUtils.ChipsButtonStates.PRESSED));
        else if (entity.title.equals("house_committee"))
            binderPay.addPropPaymentsHouseCommitteeEditText.setEnabled(!state.equals(UiUtils.ChipsButtonStates.PRESSED));
        else if (entity.title.equals("managment_fee"))
            binderPay.addPropPaymentsManagementEditText.setEnabled(!state.equals(UiUtils.ChipsButtonStates.PRESSED));
    }

    private void onAllIncludedButtonClick(FlowLayout flowLayout, Button btn, String state){
        if (btn.getText().equals(getString(R.string.attach_all_included))
                && state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED))

            for (int i = 1; i < 7 && i < flowLayout.getChildCount(); i++){
                Button button = (Button)flowLayout.getChildAt(i);
                UiUtils.instance(false, context).onChipsButtonClick(button, UiUtils.ChipsButtonStates.NOT_PRESSED);

                //Log.d(TAG, getHolderByFormattedTitle(button.getText()+"").id+":");
            } else if (btn.getText().equals(getString(R.string.attach_all_included))
                && state.equals(UiUtils.ChipsButtonStates.PRESSED))
            UiUtils.instance(false, context).onChipsButtonClick(btn, UiUtils.ChipsButtonStates.NOT_PRESSED);
    }

    private void onAllIncludedGroupMemberButtonClick(String id){
        boolean allButtonsSelected = true;
        int buttonId = Integer.parseInt(id);
        Button allIncludedBTN = (Button)binderPay.addPropertyPaymentsIncludedFlowLayout.getChildAt(0);
        if (buttonId >= 4 && buttonId <= 9) {// => "all included" group member button was clicked
            for (int i = 1; i < 7 && i < binderPay.addPropertyPaymentsIncludedFlowLayout.getChildCount(); i++) {
                if (binderPay.addPropertyPaymentsIncludedFlowLayout.getChildAt(i).getTag()
                        .equals(UiUtils.ChipsButtonStates.NOT_PRESSED)) {
                    allButtonsSelected = false;
                    break;
                }
            }
        }
        if (allButtonsSelected)
            UiUtils.instance(false, context).onChipsButtonClick(allIncludedBTN, UiUtils.ChipsButtonStates.NOT_PRESSED);
        else
            UiUtils.instance(false, context).onChipsButtonClick(allIncludedBTN, UiUtils.ChipsButtonStates.PRESSED);
    }

    private void showSnackBar(){
        final View snackBarView = binderPay.addPropertyPaymentsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
    }

    /*public class Data {
        public Integer property_id;
        public ArrayList<Integer> attachments;
        public ArrayList<Integer> addons;

        private Data(){
            attachments = new ArrayList<>();
            addons = new ArrayList<>();
        }
    }*/

}