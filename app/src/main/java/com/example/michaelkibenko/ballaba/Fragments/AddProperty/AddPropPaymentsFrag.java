package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAddonsBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzbgp.NULL;

public class AddPropPaymentsFrag extends Fragment implements Button.OnClickListener{
    private final String TAG = AddPropPaymentsFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropPaymentsBinding binderPay;
    private ArrayList<PropertyAttachmentAddonEntity> items;
    private FlowLayout paymentsRoot, paymentMethodsRoot;
    private ArrayList<PropertyAttachmentAddonEntity> payments, paymentMethods;
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

        //initButtons(view);
        return view;
    }

    private void initView(View view){
        payments = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();
        paymentMethods = PropertyAttachmentsAddonsHolder.getInstance().getPaymentMethods();

        paymentsRoot = view.findViewById(R.id.addProperty_payments_included_flowLayout);
        paymentMethodsRoot = view.findViewById(R.id.addProperty_payments_methods_flowLayout);

        initButtons(paymentsRoot, payments);
        initButtons(paymentMethodsRoot, paymentMethods);
        binderPay.addPropertyPaymentsButtonNext.setOnClickListener(this);
    }

    private void initButtons(FlowLayout flowLayout, ArrayList<PropertyAttachmentAddonEntity> items){
        for (PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)getLayoutInflater().inflate(R.layout.chips_item, null);
            initAttachment(chipsItem, attachment);
            //chipsItem.setWidth(attachment.formattedTitle.length()*5);
            //Log.e("tagg", attachment.formattedTitle.length()+"");
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
            wasPaymentsChanged = true;
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
                //String id = ;
                //if (!id.equals("1") && !id.equals("2") && !id.equals("11") && !id.equals("22")) {
                filterResult.appendAttachmentId(getHolderByFormattedTitle(text).id);
                //}
            }
            uiUtils.onChipsButtonClick((Button) v, state);
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
                    //SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_ID, ((BallabaPropertyFull)entity).id);
                    AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).getDataFromFragment(3);
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
                    } else if (v instanceof Spinner) {
                        data.put(v.getTag() + "", ((Spinner) v).getPrompt() + "");
                    }
                }
            } catch (ClassCastException e){
                Log.e(TAG, e.getMessage()+"\n class is:" + root.getChildAt(i).getClass());
            }
        }

        Spinner spinner = binderPay.addPropertyPaymentsTimeSpinner;
        data.put("no_of_payments", );
        data.put("is_extendable", binderAsset.addPropertyRentalPeriodOptionTextView.isChecked()+"");

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
        return (property != null &&
                map.get("city").equals(property.city) &&
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

    private void showSnackBar(){
        final View snackBarView = binderPay.addPropertyPaymentsRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
    }

    public class Data {
        public Integer property_id;
        public ArrayList<Integer> attachments;
        public ArrayList<Integer> addons;

        private Data(){
            attachments = new ArrayList<>();
            addons = new ArrayList<>();
        }
    }

}