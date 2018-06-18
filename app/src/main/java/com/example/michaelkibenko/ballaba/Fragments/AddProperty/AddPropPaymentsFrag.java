package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
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
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPropPaymentsFrag extends Fragment implements Button.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
    private final String TAG = AddPropPaymentsFrag.class.getSimpleName(), ALL_INCLUDED = "all_included";

    private Context context;
    private ActivityAddPropertyBinding binderMain;
    //private FragmentAddPropPaymentsBinding binderPay;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout paymentsRoot, paymentMethodsRoot;
    private ArrayList<PropertyAttachmentAddonEntity> payments, paymentMethods;
    private BallabaPropertyFull property;
    private String noOfPayments = "12";
    private boolean wasPaymentsChanged = false;
    private boolean isPaymentMethodSelected = true;

    private TextView paymentMethodTV, paymentTimeTV, paymentDateTV, parkingTV, totalPaymentTV;
    private Switch paymentMethodSwitch, paymentTimeSwitch, paymentDateSwitch, parkingSwitch, totalPaymentSwitch;
    private EditText taxET, houseCommitteeET, parkingAmountET, parkingPriceET, totalPaymentET, freeTextET;
    private TextInputLayout taxIL, houseCommitteeIL, parkingNumberIL, parkingPriceIL, totalPaymentIL, freeTextIL;
    private RadioGroup radioGroup;
    private String numberOfPayments;
    private Spinner spinner;

    private String[] arraySpinner = new String[]{
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"
    };

    public static AddPropPaymentsFrag newInstance() {
        AddPropPaymentsFrag fragment = new AddPropPaymentsFrag();
        return fragment;
    }

    public AddPropPaymentsFrag setMainBinder(ActivityAddPropertyBinding binder) {
        this.binderMain = binder;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_prop_payments, container, false);
        context = getActivity();
        ((AddPropertyActivityNew) context).changePageIndicatorText(4);

        property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();

        initView(view);
        return view;
    }

    private void initView(View view) {

        payments = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();
        paymentMethods = PropertyAttachmentsAddonsHolder.getInstance().getPaymentMethods();

        paymentsRoot = view.findViewById(R.id.addProperty_payments_included_flowLayout);
        paymentMethodsRoot = view.findViewById(R.id.addProperty_payments_methods_flowLayout);

        view.findViewById(R.id.addProperty_payments_button_next).setOnClickListener(this);

        paymentMethodTV = view.findViewById(R.id.addProperty_payments_methods_text_view);
        paymentMethodSwitch = view.findViewById(R.id.addProperty_payments_methods_switch);
        paymentMethodSwitch.setOnCheckedChangeListener(this);

        paymentTimeTV = view.findViewById(R.id.addProperty_payments_time_text_view);
        paymentTimeSwitch = view.findViewById(R.id.addProperty_payments_time_switch);
        paymentTimeSwitch.setOnCheckedChangeListener(this);


        paymentDateTV = view.findViewById(R.id.addProperty_payments_date_text_view);
        paymentDateSwitch = view.findViewById(R.id.addProperty_payments_date_switch);
        paymentDateSwitch.setOnCheckedChangeListener(this);


        parkingTV = view.findViewById(R.id.addProperty_payments_parking_price_text_view);
        parkingSwitch = view.findViewById(R.id.addProperty_payments_parking_price_switch);
        parkingSwitch.setOnCheckedChangeListener(this);


        totalPaymentTV = view.findViewById(R.id.addProperty_payments_rentalFee_text_view);
        totalPaymentSwitch = view.findViewById(R.id.addProperty_payments_rentalFee_switch);
        totalPaymentSwitch.setOnCheckedChangeListener(this);


        taxET = view.findViewById(R.id.addProp_payments_municipality_editText);
        taxIL = view.findViewById(R.id.addProperty_payments_municipality_input_layout);
        taxET.addTextChangedListener(new MyTextWatcher(taxET));

        houseCommitteeET = view.findViewById(R.id.addProp_payments_houseCommittee_editText);
        houseCommitteeIL = view.findViewById(R.id.addProperty_houseCommittee_input_layout);
        houseCommitteeET.addTextChangedListener(new MyTextWatcher(houseCommitteeET));

        parkingAmountET = view.findViewById(R.id.addProperty_payments_parking_no_editText);
        parkingNumberIL = view.findViewById(R.id.addProperty_payments_parking_no_input_layout);
        parkingAmountET.addTextChangedListener(new MyTextWatcher(parkingAmountET));

        parkingPriceET = view.findViewById(R.id.addProperty_payments_parking_price_editText);
        parkingPriceIL = view.findViewById(R.id.addProperty_payments_parking_price_input_layout);
        parkingPriceET.addTextChangedListener(new MyTextWatcher(parkingPriceET));

        totalPaymentET = view.findViewById(R.id.addProp_payments_rentalFee_edit_text);
        totalPaymentIL = view.findViewById(R.id.addProperty_payments_rentalFee_input_layout);
        totalPaymentET.addTextChangedListener(new MyTextWatcher(totalPaymentET));

        freeTextET = view.findViewById(R.id.addProp_payments_freeText_editText);
        freeTextIL = view.findViewById(R.id.addProperty_freeText_input_layout);
        freeTextET.addTextChangedListener(new MyTextWatcher(freeTextET));

        radioGroup = view.findViewById(R.id.addProperty_payments_time_radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        spinner = view.findViewById(R.id.addProp_payments_payment_date_spinner);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        initButtonAllIncluded((Button) getLayoutInflater().inflate(R.layout.chip_regular, null));
        initButtons(paymentsRoot, payments);
        initButtons(paymentMethodsRoot, paymentMethods);
        initEditTexts();
    }

    private void initButtonAllIncluded(Button chipsItem) {
        PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity("all_included_id"
                , ALL_INCLUDED, getString(R.string.attach_all_included));
        chipsItem.setText(entity.formattedTitle);
        chipsItem.setOnClickListener(this);
        UiUtils.instance(false, context).onChipsButtonClick(chipsItem, UiUtils.ChipsButtonStates.PRESSED);
        paymentsRoot.addView(chipsItem, 0);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items) {
        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button) getLayoutInflater().inflate(R.layout.chip_regular, null);
            initAttachment(chipsItem, attachment);
            highlightChipsUserSelectedLastTime(property, chipsItem, attachment);

            //TODO set 1 payment_method chip to be selected at initialization
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
    private void initEditTexts() {
        if (property != null) {

            //id "4" = "arnona"; id "5" = "house committee"; "6" = "management fee"
            for (HashMap<String, String> payment : property.payments) {
                if (payment.get("payment_type").equals(payments.get(0).id)) {
                    //binderPay.addPropPaymentsMunicipalityEditText.setText(payment.get("price"));
                } else if (payment.get("payment_type").equals(payments.get(1).id)) {
                    //binderPay.addPropPaymentsHouseCommitteeEditText.setText(payment.get("price"));
                } else if (payment.get("payment_type").equals(payments.get(2).id)) {
                    //binderPay.addPropPaymentsManagementEditText.setText(payment.get("price"));
                }
            }

            //binderPay.addPropertyPaymentsParkingNoEditText.setText(property.no_of_parking);
            //binderPay.addPropertyPaymentsParkingPriceEditText.setText(property.parking_price);
            //binderPay.addPropPaymentsRentalFeeEditText.setText(StringUtils.getInstance(true)//1. adds comma to 4+ digits number
            //.formattedNumberWithComma(property.price)+(property.price != null? "₪":""));//2. adds ₪ if price have been set
            //binderPay.addPropPaymentsFreeTextEditText.setText(property.description);
        }
    }

    private String getOriginalTitleByFormatted(String formatted) {
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.formattedTitle.equals(formatted)) {
                    return item.title;
                }
            }
        }
        return "";
    }

    private PropertyAttachmentAddonEntity getHolderByOriginalTitle(String title) {
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.title.equals(title)) {
                    return item;
                }
            }
        }
        return null;
    }

    private PropertyAttachmentAddonEntity getHolderByFormattedTitle(String formattedTitle) {
        if (items != null && items.size() > 0) {
            for (PropertyAttachmentAddonEntity item : items) {
                if (item.formattedTitle.equals(formattedTitle)) {
                    return item;
                }
            }
        }

        /*if (formattedTitle.equals(getString(R.string.attach_all_included)))//user clicked "all_included" button
            return binderPay.addPropertyPaymentsIncludedFlowLayout.getChildAt(0);

    */
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addProperty_payments_button_next)
            onFinish();
        else { //user pressed a chip/attachment
            wasPaymentsChanged = true;
            Button btn = (Button) v;
            PropertyAttachmentAddonEntity attachment = getHolderByFormattedTitle(btn.getText() + "");
            PropertyAttachmentsAddonsHolder attachments = PropertyAttachmentsAddonsHolder.getInstance();
            String state = (String) btn.getTag();

            //initialize arrays for "getHolderByOriginalTitle" according to user click
            String itemParentTag = ((FlowLayout) btn.getParent()).getTag() + "";
            if (itemParentTag.equals("payments")) {
                items = attachments.getPaymentTypes();
            } else if (itemParentTag.equals("payment_methods")) {
                items = attachments.getPaymentMethods();
            }

            UiUtils.instance(false, context).onChipsButtonClick(btn, state);

            //arnona + house committee + management fee:
            paymentsEditTextsSetEnabled(getHolderByFormattedTitle(btn.getText() + ""), btn.getTag() + "");

            if (btn.getText().equals(getString(R.string.attach_all_included)))
                onAllIncludedButtonClick(paymentsRoot, btn, state);

            if (attachment != null && Integer.parseInt(attachment.id) >= 4 && Integer.parseInt(attachment.id) <= 9)
                onAllIncludedGroupMemberButtonClick(attachment.id);

        }
    }

    private void onFinish() {
        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, "-1");
        //final Data data = getDataFromEditTexts(new Data());

        /*getPayment(data, binderPay.addPropPaymentsMunicipalityEditText, 4);//Integer.parseInt(binderPay.addPropPaymentsMunicipalityEditText.getText()+""));
        getPayment(data, binderPay.addPropPaymentsHouseCommitteeEditText, 5);
        getPayment(data, binderPay.addPropPaymentsManagementEditText, 6);*/

       /* data.payment_methods.addAll(getDataFromChipsSection(new ArrayList<Integer>()
                , binderPay.addPropertyPaymentsMethodsFlowLayout, paymentMethods));*/

        //if (!isPaymentMethodSelected)return;

        //data.property_id = Integer.parseInt(propertyId);
        ((AddPropertyActivityNew)context).changeFragment(new AddPropTakePhotoFrag() , true);

        JSONObject obj = null;
        try {
            obj = createDataFromUserInput();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (wasPaymentsChanged /*|| !areEditTextsEqual(data)*/) {
            final ProgressDialog pd = ((BaseActivity) context).getDefaultProgressDialog(context, "Uploading...");
            pd.show();
            ConnectionsManager.getInstance(context).newUploadProperty(3, propertyId, obj, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    pd.dismiss();
                    //TODO update property updating date on SharedPrefs??
                    //SharedPreferencesManager.getInstance(context).removeString(SharedPreferencesKeysHolder.PROPERTY_ID);
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "4");
                    //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).setViewPagerItem(4);
                    ((AddPropertyActivityNew)context).changeFragment(new AddPropTakePhotoFrag() , true);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    pd.dismiss();
                    //UiUtils.instance(true, context).showSnackBar()
                    //binderPay.addPropertyPaymentsRoot, "השמירה נכשלה נסה שנית מאוחר יותר");

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).setViewPagerItem(4);
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
                    Toast.makeText(context, "error occured", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).setViewPagerItem(4);
        }
    }

    private JSONObject createDataFromUserInput() throws JSONException {

        // get all chips objects -> (to get id's)
        ArrayList<PropertyAttachmentAddonEntity> paymentTypesChips = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();

        JSONObject obj = new JSONObject();

        JSONArray payments = new JSONArray();
        for (int i = 1; i < paymentsRoot.getChildCount(); i++) {
            Button chipsBtn = (Button) paymentsRoot.getChildAt(i);
            PropertyAttachmentAddonEntity currentChips = paymentTypesChips.get(i - 1);
            JSONObject paymentInnerObj = new JSONObject();

            if (chipsBtn.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                paymentInnerObj.put("type", Integer.parseInt(currentChips.id));
            } else if (chipsBtn.getTag().equals(UiUtils.ChipsButtonStates.NOT_PRESSED) && currentChips.id.equals("4")) {
                paymentInnerObj.put("type", Integer.parseInt(currentChips.id));
                paymentInnerObj.put("price", taxET.getText().toString().trim());
            } else if (chipsBtn.getTag().equals(UiUtils.ChipsButtonStates.NOT_PRESSED) && currentChips.id.equals("5")) {
                paymentInnerObj.put("type", Integer.parseInt(currentChips.id));
                paymentInnerObj.put("price", houseCommitteeET.getText().toString().trim());
            }
            if (paymentInnerObj.length() != 0) payments.put(paymentInnerObj);
        }

        ArrayList<PropertyAttachmentAddonEntity> paymentMethodsChips = PropertyAttachmentsAddonsHolder.getInstance().getPaymentMethods();

        JSONObject paymentMethods = new JSONObject();

        ArrayList<Integer> chipsSelectedIds = new ArrayList<>();
        for (int i = 0; i < paymentMethodsRoot.getChildCount(); i++) {
            Button paymentTypeChips = (Button) paymentMethodsRoot.getChildAt(i);
            if (paymentTypeChips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsSelectedIds.add(Integer.parseInt(paymentMethodsChips.get(i).id));
            }
        }
        paymentMethods.put("data", new JSONArray(chipsSelectedIds));
        paymentMethods.put("is_flexible", paymentMethodSwitch.isChecked());

        JSONObject noOfPayments = new JSONObject();
        noOfPayments.put("data", numberOfPayments);
        noOfPayments.put("is_flexible", paymentMethodSwitch.isChecked());

        JSONObject paymentDate = new JSONObject();
        paymentDate.put("data", arraySpinner[spinner.getSelectedItemPosition()]);
        paymentDate.put("is_flexible", paymentDateSwitch.isChecked());

        JSONObject parking = new JSONObject();
        String str = parkingAmountET.getText().toString().trim();
        if (str != null) {
            parking.put("no_of_parking", Integer.parseInt(str));
            parking.put("price", Integer.parseInt(parkingPriceET.getText().toString().trim()));
        }
        parking.put("is_flexible", parkingSwitch.isChecked());

        JSONObject price = new JSONObject();
        price.put("data", Integer.parseInt(totalPaymentET.getText().toString().trim()));
        price.put("is_flexible", totalPaymentSwitch.isChecked());

        obj.put("payments", payments);
        obj.put("payment_methods", paymentMethods);
        obj.put("no_of_payments", noOfPayments);
        obj.put("payment_date", paymentDate);
        obj.put("parking", parking);
        obj.put("price", price);
        obj.put("description", freeTextET.getText().toString().trim());

        return obj;
    }


    private void getPayment(Data data, EditText paymentEditText, int typeNumber) {
        String price = paymentEditText.getText().toString();
        if (paymentEditText.isEnabled() && !paymentEditText.getText().toString().equals("")) {
            Payment payment = new Payment();
            payment.type = typeNumber;
            payment.price = Integer.parseInt(price.replaceAll("\u20AA", ""));//removes "₪"
            data.payments.add(payment);
        }
        //return payment;
    }


    private ArrayList<Integer> getDataFromChipsSection(ArrayList<Integer> chipsIds, FlowLayout layout
            , ArrayList<PropertyAttachmentAddonEntity> chips) {

        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                chipsIds.add(Integer.parseInt(chips.get(i).id));
            }
        }
        if (chipsIds.isEmpty())
            isPaymentMethodSelected = false;

        return chipsIds;
    }

    private boolean areEditTextsEqual(final Data data) {
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

    //set some payments editTexts enabled\disabled
    private void paymentsEditTextsSetEnabled(PropertyAttachmentAddonEntity entity, String state) {
        if (entity == null)
            return;

        switch (entity.title) {
            /*case "arnona":
                //binderPay.addPropPaymentsMunicipalityEditText.setEnabled(!state.equals(UiUtils.ChipsButtonStates.PRESSED));
                taxesEditTextsStateChanger(binderPay.addPropPaymentsMunicipalityTitle, state);
                taxesEditTextsStateChanger(binderPay.addPropPaymentsMunicipalityEditText, state);
                break;

            case "house_committee":
                taxesEditTextsStateChanger(binderPay.addPropPaymentsHouseCommitteeRoot, state);
                taxesEditTextsStateChanger(binderPay.addPropPaymentsHouseCommitteeEditText, state);
                break;

            case "managment_fee":
                taxesEditTextsStateChanger(binderPay.addPropPaymentsManagementRoot, state);
                taxesEditTextsStateChanger(binderPay.addPropPaymentsManagementEditText, state);
                break;

            case "parking":
                taxesEditTextsStateChanger(binderPay.addPropertyPaymentsParkingPrice, state);
                taxesEditTextsStateChanger(binderPay.addPropertyPaymentsParkingPriceEditText, state);*/
        }
    }

    private void taxesEditTextsStateChanger(View view, String state) {
        view.setVisibility(state.equals(UiUtils.ChipsButtonStates.PRESSED) ? View.GONE : View.VISIBLE);
    }

    private void onAllIncludedButtonClick(FlowLayout flowLayout, Button btn, String state) {
        UiUtils uiUtils = UiUtils.instance(false, context);
        if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED)) {
            for (int i = 1; i < 7 && i < flowLayout.getChildCount(); i++) {
                uiUtils.onChipsButtonClick((Button) flowLayout.getChildAt(i), UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        } else if (state.equals(UiUtils.ChipsButtonStates.PRESSED)) {
            for (int i = 0; i < 7 && i < flowLayout.getChildCount(); i++) {
                uiUtils.onChipsButtonClick((Button) flowLayout.getChildAt(i), UiUtils.ChipsButtonStates.PRESSED);
            }
        }
    }

    private void onAllIncludedGroupMemberButtonClick(String id) {
        boolean allButtonsSelected = true;
        int buttonId = Integer.parseInt(id);
        Button allIncludedBTN = (Button) paymentsRoot.getChildAt(0);
        if (buttonId >= 4 && buttonId <= 9) {// => "all included" group member button was clicked
            for (int i = 1; i < 7 && i < paymentsRoot.getChildCount(); i++) {
                if (paymentsRoot.getChildAt(i).getTag()
                        .equals(UiUtils.ChipsButtonStates.NOT_PRESSED)) {
                    allButtonsSelected = false;
                    break;
                }
            }
        }
        if (allButtonsSelected) {
            UiUtils.instance(false, context).onChipsButtonClick(allIncludedBTN, UiUtils.ChipsButtonStates.NOT_PRESSED);
        } else {
            UiUtils.instance(false, context).onChipsButtonClick(allIncludedBTN, UiUtils.ChipsButtonStates.PRESSED);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.addProperty_payments_methods_switch:
                paymentMethodTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_payments_time_switch:
                paymentTimeTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_payments_date_switch:
                paymentDateTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_payments_parking_price_switch:
                parkingTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProperty_payments_rentalFee_switch:
                totalPaymentTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = group.findViewById(group.getCheckedRadioButtonId());
        numberOfPayments = button.getTag().toString();
    }

    private class Data {
        private Integer property_id;
        private ArrayList<Payment> payments;
        private ArrayList<Integer> payment_methods;
        private HashMap<String, Object> details;

        private Data() {
            payments = new ArrayList<>();
            payment_methods = new ArrayList<>();
            details = new HashMap<>();
        }
    }

    public class Payment {
        public int type;// = new HashMap<>();
        public /*HashMap<String, Integer>*/ int price;// = new HashMap<>();
    }

    private void validateET(TextInputLayout v) {
        EditText child = v.getEditText();
        if (child.getText().toString().trim().isEmpty()) {
            v.setError("שגיאה");
            v.requestFocus();
        } else {
            v.setErrorEnabled(false);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.addProp_payments_municipality_editText:
                    validateET(taxIL);
                    break;
                case R.id.addProp_payments_houseCommittee_editText:
                    validateET(houseCommitteeIL);
                    break;
                case R.id.addProperty_payments_parking_no_editText:
                    validateET(parkingNumberIL);
                    break;
                case R.id.addProperty_payments_parking_price_editText:
                    validateET(parkingPriceIL);
                    break;
                case R.id.addProp_payments_rentalFee_edit_text:
                    validateET(totalPaymentIL);
                    break;
                case R.id.addProp_payments_freeText_editText:
                    validateET(freeTextIL);
                    break;
            }
        }
    }
}