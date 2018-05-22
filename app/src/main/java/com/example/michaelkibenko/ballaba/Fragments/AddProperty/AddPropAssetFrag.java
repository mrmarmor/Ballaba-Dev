package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddPropAssetFrag extends Fragment implements EditText.OnFocusChangeListener{
    private static final String TAG = AddPropAssetFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAssetBinding binderAsset;
    private boolean areAllDataFieldsFilledUp = true;
    //private TextView yearTextView;

    public AddPropAssetFrag() {}
    public static AddPropAssetFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAssetFrag fragment = new AddPropAssetFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binderAsset = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_asset, container, false);

        UiUtils.instance(true, context).initAutoCompleteCity(binderAsset.addPropCityActv);

        binderAsset.addPropertyAssetButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish(ConnectionsManager.getInstance(context));
            }
        });

        //String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, null);
        initEditTexts(BallabaSearchPropertiesManager.getInstance(context).getPropertyFull());

        /*conn.getPropertyById(propertyId, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d(TAG, ((BallabaOkResponse)entity).body);
                initEditTexts((BallabaPropertyFull)entity);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, entity.toString());
            }
        });*/

        binderAsset.addPropAssetRentalPeriodDatePicker
                .setTitle(getString(R.string.addProperty_asset_dateOfEntrance))
                .setTextSize(14);

        return binderAsset.getRoot();
    }

    private void initEditTexts(BallabaPropertyFull property){
        if (property != null) {
            binderAsset.addPropCityActv.setText(property.city);
            binderAsset.addPropAddressEditText.setText(property.formattedAddress);
            binderAsset.addPropAptNoEditText.setText(property.street_number);
            binderAsset.addPropFloorEditText.setText(property.floor);
            binderAsset.addPropMaxFloorEditText.setText(property.max_floor);
            binderAsset.addPropRoomsEditText.setText(property.roomsNumber);
            binderAsset.addPropToiletsEditText.setText(property.toilets);
            binderAsset.addPropBathroomsEditText.setText(property.bathrooms);
            binderAsset.addPropSizeEditText.setText(property.size);
            binderAsset.addPropertyRentalPeriodMonthsEditText.setText(property.rentPeriod);
        }

        validateFloors();
    }

    private void validateFloors(){
        binderAsset.addPropFloorEditText.setOnFocusChangeListener(this);
        binderAsset.addPropMaxFloorEditText.setOnFocusChangeListener(this);
    }

    /*private void showSnackBar(){
        final View snackBarView = binderAsset.addPropertyLocationRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(
                context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }*/

    private void onFinish(ConnectionsManager conn){
        final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());

        if (!areAllDataFieldsFilledUp) return;

        if (!isDataEqual(data)) {
            conn.uploadProperty(jsonParse(data, "create"), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "2");
                    new AddPropertyPresenter((AppCompatActivity) context, binderMain).onNextViewPagerItem(1);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    ((BaseActivity) context).getDefaultSnackBar(binderAsset.addPropertyLocationRoot
                            , "השמירה נכשלה נסה שנית מאוחר יותר", false);
                    Log.e(TAG, ((BallabaErrorResponse) entity).message);

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).onNextViewPagerItem(1);
                }
            });
        } else {
            //TODO continue to next page without sending data to server
            AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).onNextViewPagerItem(1);
        }
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> map){
        areAllDataFieldsFilledUp = true;
        LinearLayout root = binderAsset.addPropertyLocationRoot;
        for (int i = root.getChildCount() - 1; i >= 0 ; i--) {
            try {
                for (int j = ((ViewGroup)root.getChildAt(i)).getChildCount() - 1; j >= 0; j--) {
                    View v = ((ViewGroup)root.getChildAt(i)).getChildAt(j);
                    if (v instanceof EditText | v instanceof AutoCompleteTextView) {
                        String input = ((EditText) v).getText().toString();
                        if (input.equals("")) {
                            areAllDataFieldsFilledUp = false;
                            v.requestFocus();
                        } else {
                            map.put(v.getTag() + "", input);
                        }
                    }
                }
            } catch (ClassCastException e){
                Log.e(TAG, e.getMessage()+"\n class is:" + root.getChildAt(i).getClass());
            }
        }

        DatePicker picker = binderAsset.addPropAssetRentalPeriodDatePicker;
        map.put("entry_date", String.format("%d-%02d-%02d", picker.getYear(), picker.getMonth()+1, picker.getDayOfMonth()));
        map.put("is_extendable", binderAsset.addPropertyRentalPeriodOptionTextView.isChecked()+"");

        //TODO maybe marik needs a dummy zip code
        map.put("zip_code", "8060000");

        return map;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean isDataEqual(HashMap<String, String> map){
        BallabaPropertyFull property = BallabaSearchPropertiesManager.getInstance(context).getPropertyFull();
        return (property != null &&
                map.get("city").equals(property.city) &&
                map.get("street").equals(property.street) &&
                map.get("appartment").equals(property.street_number) &&//TODO marik wrote "appartment" which is typo error
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

    private JSONObject jsonParse(HashMap<String, String> propertyData, String step){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("step", step);
            JSONObject innerObject = new JSONObject();

            //TODO here is a loop. if it is too complicated for you, use a simple:
            // innerObject.put(specificKey, (casting)propertyData.get(specificKey));
            // on each 13 data rows
            for (String key : propertyData.keySet()) {
                if (key.equals("city") || key.equals("street") || key.equals("appartment") || key.equals("entry_date"))
                    innerObject.put(key, propertyData.get(key));
                else if (key.equals("is_extendable"))
                    innerObject.put(key, Boolean.valueOf(propertyData.get(key)));
                else
                    innerObject.put(key, Integer.valueOf(propertyData.get(key)));
            }
            jsonObject.put("data", innerObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        try {
            String maxFloorsStr = binderAsset.addPropMaxFloorEditText.getText()+"";
            String floorStr = binderAsset.addPropFloorEditText.getText()+"";
            if (!maxFloorsStr.equals("") && !floorStr.equals("")) {
                int maxFloors = Integer.parseInt(maxFloorsStr);
                int floors = Integer.parseInt(floorStr);

                if (floors > maxFloors){
                    ((BaseActivity)context).getDefaultSnackBar(binderAsset.getRoot(), "Floor is greater than the max floor",false);
                    v.requestFocus();
                    binderAsset.addPropFloorEditText.setTextColor(context.getResources().getColor(R.color.red_error_phone));
                    binderAsset.addPropFloorEditText.setText("Floor is greater than the max floor");
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}