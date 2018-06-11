package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Views.MultiLanguagesDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddPropAssetFrag extends Fragment implements EditText.OnFocusChangeListener, TextWatcher {
    private static final String TAG = AddPropAssetFrag.class.getSimpleName();

    private Context context;
    //private ActivityAddPropertyBinding binderMain;
    //private FragmentAddPropAssetBinding binderAsset;
    private boolean areAllDataFieldsFilledUp = true;
    private View v;
    private EditText cityET, addressET, aptET, floorET, maxFloorET, roomET, toiletET, bathroomET, sizeET, rentalPeriodET;
    private CheckBox rentalTV;
    private MultiLanguagesDatePicker datePicker;
    private LinearLayout root;


    public AddPropAssetFrag() {
    }

   /* public static AddPropAssetFrag newInstance() {
        AddPropAssetFrag fragment = new AddPropAssetFrag();

        return fragment;
    }

    public AddPropAssetFrag setMainBinder(ActivityAddPropertyBinding binder) {
        this.binderMain = binder;
        return this;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_prop_asset_new, container, false);
        ((AddPropertyActivityNew)getActivity()).changePageIndicatorText(2);
            /*binderAsset = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_add_prop_asset, container, false);

        binderAsset.addPropertyAssetButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish(ConnectionsManager.getInstance(context));
            }
        });*/

        v.findViewById(R.id.addProperty_asset_button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish(ConnectionsManager.getInstance(context));
            }
        });

        //String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, null);
        rentalTV = v.findViewById(R.id.addProperty_rentalPeriod_option_textView);
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

        datePicker = v.findViewById(R.id.addProp_asset_rentalPeriod_datePicker);
        datePicker.setTitle(getString(R.string.addProperty_asset_dateOfEntrance)).setTextSize(14);

        return v;
    }

    private void initEditTexts(BallabaPropertyFull property) {
        cityET = v.findViewById(R.id.addProp_city_actv);
        addressET = v.findViewById(R.id.addProp_address_actv);
        aptET = v.findViewById(R.id.addProp_aptNo_editText);
        floorET = v.findViewById(R.id.addProp_floor_editText);
        maxFloorET = v.findViewById(R.id.addProp_maxFloor_editText);
        roomET = v.findViewById(R.id.addProp_rooms_editText);
        toiletET = v.findViewById(R.id.addProp_toilets_editText);
        bathroomET = v.findViewById(R.id.addProp_bathrooms_editText);
        sizeET = v.findViewById(R.id.addProp_size_editText);
        rentalPeriodET = v.findViewById(R.id.addProperty_rentalPeriod_months_editText);

        if (property != null) {
            /*binderAsset.addPropCityActv.setText(property.city);
            binderAsset.addPropAddressActv.setText(property.formattedAddress);
            binderAsset.addPropAptNoEditText.setText(property.street_number);
            binderAsset.addPropFloorEditText.setText(property.floor);
            binderAsset.addPropMaxFloorEditText.setText(property.max_floor);
            binderAsset.addPropRoomsEditText.setText(property.roomsNumber);
            binderAsset.addPropToiletsEditText.setText(property.toilets);
            binderAsset.addPropBathroomsEditText.setText(property.bathrooms);
            binderAsset.addPropSizeEditText.setText(property.size);
            binderAsset.addPropertyRentalPeriodMonthsEditText.setText(property.rentPeriod);*/

            cityET.setText(property.city);
            addressET.setText(property.city);
            aptET.setText(property.city);
            floorET.setText(property.city);
            maxFloorET.setText(property.city);
            roomET.setText(property.city);
            toiletET.setText(property.city);
            bathroomET.setText(property.city);
            sizeET.setText(property.city);
            rentalPeriodET.setText(property.city);

        }
        //binderAsset.addPropCityActv.requestFocus();

        //UiUtils.instance(true, context).initAutoCompleteCity(binderAsset.addPropCityActv);
        //UiUtils.instance(true, context).initAutoCompleteAddressInCity(binderAsset.addPropAddressActv, binderAsset.addPropCityActv);

        //binderAsset.addPropAddressEditText.addTextChangedListener(this);

        validateFloors();
    }

    private void validateFloors() {
        //binderAsset.addPropFloorEditText.setOnFocusChangeListener(this);
        //binderAsset.addPropMaxFloorEditText.setOnFocusChangeListener(this);
        floorET.setOnFocusChangeListener(this);
        maxFloorET.setOnFocusChangeListener(this);
    }

    /*private void showSnackBar(){
        final View snackBarView = binderAsset.addPropertyLocationRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(
                context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }*/

    private void onFinish(ConnectionsManager conn) {
        final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
        //((AddPropertyActivityNew)getActivity()).changeFragment(new AddPropAddonsFrag() , true);
        if (!areAllDataFieldsFilledUp) return;

        if (!isDataEqual(data)) {
            /*conn.uploadProperty(jsonParse(data, "create"), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "2");
                    //new AddPropertyPresenter((AppCompatActivity) context, binderMain).setViewPagerItem(2);
                    ((AddPropertyActivityNew)getActivity()).changeFragment(new AddPropAddonsFrag() , true);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    *//*((BaseActivity) context).getDefaultSnackBar(binderAsset.addPropertyLocationRoot
                            , "השמירה נכשלה נסה שנית מאוחר יותר", false);*//*
                    Log.e(TAG, "error: " + ((BallabaErrorResponse) entity).message);

                    //TODO NEXT LINE IS ONLY FOR TESTING:
                    //new AddPropertyPresenter((AppCompatActivity)context, binderMain).onNextViewPagerItem(1);
                }
            });*/
            conn.newUploadProperty(1, null, jsonParse(data , null), new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    ((AddPropertyActivityNew)getActivity()).changeFragment(new AddPropAddonsFrag() , true);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    String err = ((BallabaErrorResponse)entity).message;
                    Log.d(TAG, "reject: " + err);
                }
            });
        } else {
            //TODO continue to next page without sending data to server
            //AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).setViewPagerItem(2);
        }
        //((AddPropertyActivityNew)getActivity()).changeFragment(new AddPropAddonsFrag() , true);
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> map) {
        areAllDataFieldsFilledUp = true;
        root = v.findViewById(R.id.addProperty_location_root);
        for (int i = root.getChildCount() - 1; i >= 0; i--) {
            try {
                for (int j = ((ViewGroup) root.getChildAt(i)).getChildCount() - 1; j >= 0; j--) {
                    View v = ((ViewGroup) root.getChildAt(i)).getChildAt(j);
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
            } catch (ClassCastException e) {
                Log.e(TAG, e.getMessage() + "\n class is:" + root.getChildAt(i).getClass());
            }
        }


        map.put("entry_date", String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
        map.put("is_extendable", rentalTV.isChecked() + "");

        //TODO maybe marik needs a dummy zip code
        map.put("zip_code", "8060000");

        return map;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean isDataEqual(HashMap<String, String> map) {
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

    private JSONObject jsonParse(HashMap<String, String> propertyData, String step) {
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
            String maxFloorsStr = maxFloorET.getText() + "";
            String floorStr = floorET.getText() + "";
            if (!maxFloorsStr.equals("") && !floorStr.equals("")) {
                int maxFloors = Integer.parseInt(maxFloorsStr);
                int floors = Integer.parseInt(floorStr);

                if (floors > maxFloors) {
                    ((BaseActivity) context).getDefaultSnackBar(root, "Floor is greater than the max floor", false);
                    v.requestFocus();
                    floorET.setTextColor(context.getResources().getColor(R.color.red_error_phone));
                    floorET.setText("Floor is greater than the max floor");
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}