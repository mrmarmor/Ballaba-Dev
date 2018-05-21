package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
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
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;
import com.google.gson.Gson;
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
    private ActivityAddPropertyBinding binderMain;
    private FragmentAddPropPaymentsBinding binderPay;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout paymentsRoot, paymentMethodsRoot;
    private ArrayList<PropertyAttachmentAddonEntity> payments, paymentMethods;
    private BallabaPropertyFull property;
    private String noOfPayments = "12";
    private boolean wasPaymentsChanged = false;

    public AddPropPaymentsFrag() {}
    public static AddPropPaymentsFrag newInstance() {
        AddPropPaymentsFrag fragment = new AddPropPaymentsFrag();

        return fragment;
    }

    public AddPropPaymentsFrag setMainBinder(ActivityAddPropertyBinding binder){
        this.binderMain = binder;
        return this;
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
        initSpinner(binderPay.addPropertyPaymentsTimeSpinner);
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
        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);
            highlightChipsUserSelectedLastTime(property, chipsItem, attachment);

            //TODO set 1 payment_method chip to be selected at initialization

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

    private void highlightChipsUserSelectedLastTime(BallabaPropertyFull property, Button chipsItem, PropertyAttachmentAddonEntity attachment) {
        //these 2 loops highlight chips that were selected last upload
        if (property != null) {
            for (HashMap<String, String> map : property.payments)
                if (attachment.id.equals(map.get("payment_types")))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String) chipsItem.getTag());

            for (HashMap<String, String> map : property.paymentMethods)
                if (attachment.id.equals(map.get("payment_methods")))
                    chipsItem = UiUtils.instance(false, context).onChipsButtonClick(chipsItem, (String) chipsItem.getTag());
        }
    }

    //TODO on viewPager state of screen kept only for current screen and next screen. so if i scroll 2 screens state will not be kept. maybe i shall saveInstance
    private void initEditTexts(){
        if (property != null) {

            //id "4" = "arnona"; id "5" = "house committee"; "6" = "management fee"
            for (HashMap<String, String> payment : property.payments){
                if (payment.get("payment_type").equals(payments.get(0).id)){
                    binderPay.addPropPaymentsMunicipalityEditText.setText(payment.get("price"));
                } else if (payment.get("payment_type").equals(payments.get(1).id)){
                    binderPay.addPropPaymentsHouseCommitteeEditText.setText(payment.get("price"));
                } else if (payment.get("payment_type").equals(payments.get(2).id)){
                    binderPay.addPropPaymentsManagementEditText.setText(payment.get("price"));
                }
            }

            binderPay.addPropertyPaymentsParkingNoEditText.setText(property.no_of_parking);
            binderPay.addPropertyPaymentsParkingPriceEditText.setText(property.parking_price);
            binderPay.addPropPaymentsRentalFeeEditText.setText(StringUtils.getInstance(true, context)
                    .formattedNumberWithComma(property.price)+"₪");
            binderPay.addPropPaymentsFreeTextEditText.setText(property.description);
        }
    }

    private void initSpinner(Spinner spinner){
        String[] values = context.getResources().getStringArray(R.array.addProperty_no_of_payments);
        final String [][] spinnerItemsMap = {{"12", values[0]}, {"6", values[1]}, {"4", values[2]}
                , {"3", values[3]}, {"2", values[4]}, {"1", values[5]}};

        for (int i = 0; i < spinnerItemsMap.length; i++)
            if (property != null && property.numberOfPayments.equals(spinnerItemsMap[i][0])){
                spinner.setSelection(i);
            }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfPayments = spinnerItemsMap[position][0];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
            PropertyAttachmentAddonEntity attachment = getHolderByFormattedTitle(btn.getText()+"");
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

            if (btn.getText().equals(getString(R.string.attach_all_included)))
                onAllIncludedButtonClick(binderPay.addPropertyPaymentsIncludedFlowLayout, btn, state);

            if (attachment != null && Integer.parseInt(attachment.id) >= 4 && Integer.parseInt(attachment.id) <= 9)
                onAllIncludedGroupMemberButtonClick(attachment.id);

        }
    }

    private void onFinish(){
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, "-1");
        final Data data = getDataFromEditTexts(new Data());

        getPayment(data, binderPay.addPropPaymentsMunicipalityEditText, 4);//Integer.parseInt(binderPay.addPropPaymentsMunicipalityEditText.getText()+""));
        getPayment(data, binderPay.addPropPaymentsHouseCommitteeEditText, 5);
        getPayment(data, binderPay.addPropPaymentsManagementEditText, 6);

        data.payment_methods.addAll(getDataFromChipsSection(new ArrayList<Integer>()
                , binderPay.addPropertyPaymentsMethodsFlowLayout, paymentMethods));
        data.property_id = Integer.parseInt(propertyId);

        if (wasPaymentsChanged || !areEditTextsEqual(data)) {
            final ProgressDialog pd = ((BaseActivity)context).getDefaultProgressDialog(context, "Uploading...");
            pd.show();
            ConnectionsManager.getInstance(context).uploadProperty(jsonParse(data, "payments"), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    pd.dismiss();
                    //TODO update property updating date on SharedPrefs??
                    //SharedPreferencesManager.getInstance(context).removeString(SharedPreferencesKeysHolder.PROPERTY_ID);
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "4");
                    AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).onNextViewPagerItem(3);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    pd.dismiss();
                    UiUtils.instance(true, context).showSnackBar(
                            binderPay.addPropertyPaymentsRoot, "השמירה נכשלה נסה שנית מאוחר יותר");

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).onNextViewPagerItem(3);
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                }
            });
        } else {
            AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).onNextViewPagerItem(3);
        }
    }

    private Data getDataFromEditTexts(Data data){
        /*String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, NULL);
        StringBuilder sb = new StringBuilder();
        sb.append("\"step\" : \"" + "payments"+"\"")
          .append(",\"data\" : {\"property_id\" : "+propertyId)
          .append(",\"payments\" : [");*/

       /* if (binderPay.addPropPaymentsMunicipalityEditText.isEnabled())
            data.details.put("arnona", Integer.parseInt(binderPay.addPropPaymentsMunicipalityEditText.getText()+""));
        if (binderPay.addPropPaymentsHouseCommitteeEditText.isEnabled())
            data.details.put("house_committee", Integer.parseInt(binderPay.addPropPaymentsHouseCommitteeEditText.getText()+""));
        if (binderPay.addPropPaymentsManagementEditText.isEnabled())
            data.details.put("managment", Integer.parseInt(binderPay.addPropPaymentsManagementEditText.getText()+""));
*/
        /*data.put("no_of_payments", noOfPayments);
        data.put("no_of_parking", binderPay.addPropertyPaymentsParkingNoEditText.getText());
        data.put("parking_price", binderPay.addPropertyPaymentsParkingPriceEditText.getText());
        data.put("rent_price", binderPay.addPropPaymentsRentalFeeEditText.getText());
        data.put("description", binderPay.addPropPaymentsFreeTextEditText.getText());*/
        try {
            data.details.put("no_of_payments", Integer.parseInt(noOfPayments));
            data.details.put("no_of_parking", Integer.parseInt(binderPay.addPropertyPaymentsParkingNoEditText.getText() + ""));
            data.details.put("parking_price", Integer.parseInt(binderPay.addPropertyPaymentsParkingPriceEditText
                    .getText().toString().replace("₪,.", "")));
            data.details.put("rent_price", Integer.parseInt(binderPay.addPropPaymentsRentalFeeEditText
                    .getText().toString().replace("₪,.", "")));
            data.details.put("description", binderPay.addPropPaymentsFreeTextEditText.getText());

            return data;
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage());
            return data;
        }
    }

    private void getPayment(Data data, EditText paymentEditText, int typeNumber){
        String price = paymentEditText.getText().toString();
        if (paymentEditText.isEnabled() && !paymentEditText.getText().toString().equals("")) {
            Payment payment = new Payment();
            payment.type = typeNumber;
            payment.price = Integer.parseInt(price.replace("₪,.", ""));
            data.payments.add(payment);
        }
        //return payment;
    }

    /*private HashMap<String, Object> getDataFromChips(HashMap<String, Object> data){
        data.put("payments", getDataFromChipsSection(new ArrayList<Integer>()
                , binderPay.addPropertyPaymentsIncludedFlowLayout, payments));
        data.put("payment_methods", getDataFromChipsSection(new ArrayList<Integer>()
                , binderPay.addPropertyPaymentsMethodsFlowLayout, paymentMethods));
        return data;
    }*/

    private ArrayList<Integer> getDataFromChipsSection(ArrayList<Integer> chipsIds, FlowLayout layout
            , ArrayList<PropertyAttachmentAddonEntity> chips){

        for (int i = 0; i < layout.getChildCount(); i++){
            if (layout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED)){
                chipsIds.add(Integer.parseInt(chips.get(i).id));
            }
        }

        return chipsIds;
    }

    private boolean areEditTextsEqual(final Data data){
        //TODO this function needs a deep testing to check if all data fields are correct
        PropertyAttachmentsAddonsHolder entity = PropertyAttachmentsAddonsHolder.getInstance();
        return (property != null &&
                data.details.get("no_of_payments").equals(property.numberOfPayments) &&
                data.details.get("arnona").equals(entity.getFormattedTitleById(payments, payments.get(0).id)) &&
                data.details.get("house_committee").equals(entity.getFormattedTitleById(payments, payments.get(1).id)) &&//TODO marik wrote "appartment" which is typo error
                data.details.get("managment").equals(entity.getFormattedTitleById(payments, payments.get(2).id)) &&
                data.details.get("no_of_parking").equals(property.no_of_parking) &&
                data.details.get("parking_price").equals(property.parking_price) &&
                data.details.get("rent_price").equals(property.price) &&
                data.details.get("description").equals(property.description));
    }

    private JSONObject jsonParse(Data propertyData, String step){

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject innerObject = new JsonObject();
            JsonObject detailsObj = new JsonObject();

            //innerObject.add("details", new Gson().toJsonTree(propertyData.details));
            if (propertyData.details.containsKey("arnona"))
                detailsObj.add("arnona", new Gson().toJsonTree(propertyData.details.get("arnona")));
            if (propertyData.details.containsKey("house_committee"))
                detailsObj.add("house_committee", new Gson().toJsonTree(propertyData.details.get("house_committee")));
            if (propertyData.details.containsKey("managment"))
                detailsObj.add("managment", new Gson().toJsonTree(propertyData.details.get("managment")));
            detailsObj.add("no_of_payments", new Gson().toJsonTree(propertyData.details.get("no_of_payments")));
            detailsObj.add("no_of_parking", new Gson().toJsonTree(propertyData.details.get("no_of_parking")));
            detailsObj.add("parking_price", new Gson().toJsonTree(propertyData.details.get("parking_price")));
            detailsObj.add("rent_price", new Gson().toJsonTree(propertyData.details.get("rent_price")));
            detailsObj.addProperty("description", propertyData.details.get("description")+"");

            innerObject.addProperty("property_id", propertyData.property_id);
            innerObject.add("payment_methods", new Gson().toJsonTree(propertyData.payment_methods));
            innerObject.add("payments", new Gson().toJsonTree(propertyData.payments));
            innerObject.add("details", detailsObj);

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
        else if (entity.title.equals("parking"))
            binderPay.addPropertyPaymentsParkingPriceEditText.setEnabled(!state.equals(UiUtils.ChipsButtonStates.PRESSED));
    }

    private void onAllIncludedButtonClick(FlowLayout flowLayout, Button btn, String state){
        UiUtils uiUtils = UiUtils.instance(false, context);
        if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)) {
            for (int i = 1; i < 7 && i < flowLayout.getChildCount(); i++) {
                uiUtils.onChipsButtonClick((Button)flowLayout.getChildAt(i), UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        } else if (state.equals(UiUtils.ChipsButtonStates.PRESSED)) {
            uiUtils.onChipsButtonClick(btn, UiUtils.ChipsButtonStates.NOT_PRESSED);
        }
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

    /*private void showSnackBar(){
        final View snackBarView = binderPay.addPropertyPaymentsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public class Data {
        public Integer property_id;
        public ArrayList<Payment> payments;
        public ArrayList<Integer> payment_methods;
        public HashMap<String, Object> details;

        private Data(){
            payments = new ArrayList<>();
            payment_methods = new ArrayList<>();
            details = new HashMap<>();
        }
    }

    public class Payment {
        public int type;// = new HashMap<>();
        public /*HashMap<String, Integer>*/int price;// = new HashMap<>();

        //private Payment(){}
        /*private Payment(HashMap<String, Integer> type, HashMap<String, Integer> price) {
            this.type = new HashMap<>();
            this.price = new HashMap<>();
        }*/
    }

}