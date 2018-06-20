package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.Views.MultiLanguagesDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddPropAssetFrag extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = AddPropAssetFrag.class.getSimpleName();

    private Context context;
    private View v;
    private EditText houseET, aptET, floorET, maxFloorET, roomET, toiletET, bathroomET, sizeET, rentalPeriodET;
    private AutoCompleteTextView cityET, streetET;
    private TextInputLayout cityInputLayout, streetInputLayout, aptNoInputLayout, floorInputLayout,
            maxFloorInputLayout, roomInputLayout, toiletInputLayout, bathroomInputLayout, sizeInputLayout,
            rentalPeriodInputLayout, houseInputLayout;
    private CheckBox rentalCB;
    private Switch entryDateSwitch, rentalPeriodSwitch;
    private TextView entryDateTV, rentalPeriodTV;
    private MultiLanguagesDatePicker datePicker;
    private LinearLayout root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_prop_asset_new, container, false);
        ((AddPropertyActivityNew) getActivity()).changePageIndicatorText(2);

        root = v.findViewById(R.id.addProperty_location_root);
        v.findViewById(R.id.addProperty_asset_button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish(ConnectionsManager.getInstance(context));
            }
        });

        String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, null);
        //if (propertyId != null) getPreviewsPropertyData(propertyId);

        rentalCB = v.findViewById(R.id.addProperty_rentalPeriod_option_check_box);

        rentalPeriodTV = v.findViewById(R.id.addProp_asset_rentalPeriod_text_view);
        rentalPeriodSwitch = v.findViewById(R.id.addProp_asset_rentalPeriod_switch);
        rentalPeriodSwitch.setOnCheckedChangeListener(this);

        entryDateTV = v.findViewById(R.id.addProp_asset_entry_date_text_view);
        entryDateSwitch = v.findViewById(R.id.addProp_asset_entry_date_switch);
        entryDateSwitch.setOnCheckedChangeListener(this);

        initEditTexts(BallabaSearchPropertiesManager.getInstance(context).getPropertyFull());

        datePicker = v.findViewById(R.id.addProp_asset_rentalPeriod_datePicker);
        datePicker.setTitle(getString(R.string.addProperty_asset_dateOfEntrance)).setTextSize(14);

        return v;
    }

    private void getPreviewsPropertyData(String propertyId) {
        ConnectionsManager.getInstance(context).getPropertyById(propertyId, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d(TAG, ((BallabaOkResponse) entity).body);
                initEditTexts((BallabaPropertyFull) entity);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, entity.toString());
            }
        });
    }

    private void initEditTexts(BallabaPropertyFull property) {
        cityET = v.findViewById(R.id.addProp_city_actv);
        cityInputLayout = v.findViewById(R.id.addProp_city_input_layout);

        streetET = v.findViewById(R.id.addProp_street_actv);
        streetInputLayout = v.findViewById(R.id.addProp_street_input_layout_name);

        aptET = v.findViewById(R.id.addProp_aptNo_editText);
        aptNoInputLayout = v.findViewById(R.id.addProp_aptNo_input_layout);

        floorET = v.findViewById(R.id.addProp_floor_editText);
        floorInputLayout = v.findViewById(R.id.addProp_floor_input_layout);

        maxFloorET = v.findViewById(R.id.addProp_maxFloor_editText);
        maxFloorInputLayout = v.findViewById(R.id.addProp_maxFloor_input_layout);

        roomET = v.findViewById(R.id.addProp_rooms_editText);
        roomInputLayout = v.findViewById(R.id.addProp_rooms_input_layout);

        toiletET = v.findViewById(R.id.addProp_toilets_editText);
        toiletInputLayout = v.findViewById(R.id.addProp_toilets_input_layout);

        bathroomET = v.findViewById(R.id.addProp_bathrooms_editText);
        bathroomInputLayout = v.findViewById(R.id.addProp_bathrooms_input_layout);

        sizeET = v.findViewById(R.id.addProp_size_editText);
        sizeInputLayout = v.findViewById(R.id.addProp_size_input_layout);

        rentalPeriodET = v.findViewById(R.id.addProperty_rentalPeriod_months_editText);
        rentalPeriodInputLayout = v.findViewById(R.id.addProperty_rentalPeriod_input_layout);

        houseET = v.findViewById(R.id.addProp_house_number_editText);
        houseInputLayout = v.findViewById(R.id.addProp_house_number_input_layout);

        cityET.addTextChangedListener(new MyTextWatcher(cityET));
        streetET.addTextChangedListener(new MyTextWatcher(streetET));
        houseET.addTextChangedListener(new MyTextWatcher(houseET));
        aptET.addTextChangedListener(new MyTextWatcher(aptET));
        floorET.addTextChangedListener(new MyTextWatcher(floorET));
        maxFloorET.addTextChangedListener(new MyTextWatcher(maxFloorET));
        roomET.addTextChangedListener(new MyTextWatcher(roomET));
        toiletET.addTextChangedListener(new MyTextWatcher(toiletET));
        bathroomET.addTextChangedListener(new MyTextWatcher(bathroomET));
        sizeET.addTextChangedListener(new MyTextWatcher(sizeET));
        rentalPeriodET.addTextChangedListener(new MyTextWatcher(rentalPeriodET));


        if (property != null) {
            cityET.setText(property.city);
            streetET.setText(property.street);
            aptET.setText(property.city);
            floorET.setText(property.floor);
            maxFloorET.setText(property.max_floor);
            roomET.setText(property.roomsNumber);
            toiletET.setText(property.toilets);
            bathroomET.setText(property.bathrooms);
            sizeET.setText(property.size);
            rentalPeriodET.setText(property.rentPeriod);
        }

        UiUtils.instance(true, context).initAutoCompleteCity(cityET);
        UiUtils.instance(true, context).initAutoCompleteAddressInCity(streetET, cityET);
    }


    private void onFinish(ConnectionsManager conn) {
        final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
        //((AddPropertyActivityNew) getActivity()).changeFragment(new AddPropAddonsFrag(), true);

        if (!isDataEqual(data)) {
            boolean validate = validateInputs();
            if (!validate) return;
            JSONObject obj = createJsonFromUserInput();
            conn.newUploadProperty(1, null, obj, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    try {
                        JSONObject propertyDescription = new JSONObject(((BallabaOkResponse) entity).body);
                        String propertyID = propertyDescription.getString("id");
                        SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_ID, propertyID);
                        Toast.makeText(context, propertyID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((AddPropertyActivityNew) getActivity()).changeFragment(new AddPropAddonsFrag(), true);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    String err = ((BallabaErrorResponse) entity).message;
                    Log.d(TAG, "reject: " + err);
                }
            });
        } else {
            // continue to next page without sending data to server
            ((AddPropertyActivityNew) getActivity()).changeFragment(new AddPropAddonsFrag(), true);
        }
    }

    private boolean validateInputs() {
        EditText[] editTexts = new EditText[]{
                cityET, streetET, houseET, aptET, floorET, maxFloorET, roomET, bathroomET, toiletET, sizeET, rentalPeriodET
        };

        for (int i = 0; i < editTexts.length; i++) {
            EditText currentEditText = editTexts[i];
            if (currentEditText.getText().toString().trim().isEmpty()) {
                currentEditText.requestFocus();
                return false;
            }
        }
        return true;
    }

    @NonNull
    private JSONObject createJsonFromUserInput() {
        JSONObject obj = new JSONObject();
        JSONObject entry_date = new JSONObject();
        JSONObject rent_period = new JSONObject();
        try {
            obj.put(cityET.getTag() + "", cityET.getText().toString().trim());
            obj.put(streetET.getTag() + "", streetET.getText().toString().trim());
            obj.put(houseET.getTag() + "", houseET.getText().toString().trim());
            obj.put(aptET.getTag() + "", aptET.getText().toString().trim());
            obj.put(floorET.getTag() + "", Integer.parseInt(floorET.getText().toString().trim()));
            obj.put(maxFloorET.getTag() + "", Integer.parseInt(maxFloorET.getText().toString().trim()));
            obj.put(roomET.getTag() + "", Integer.parseInt(roomET.getText().toString().trim()));
            obj.put(bathroomET.getTag() + "", Integer.parseInt(bathroomET.getText().toString().trim()));
            obj.put(toiletET.getTag() + "", Integer.parseInt(toiletET.getText().toString().trim()));
            obj.put(sizeET.getTag() + "", Integer.parseInt(sizeET.getText().toString().trim()));

            entry_date.put("data", String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
            entry_date.put("is_flexible", entryDateSwitch.isChecked());
            obj.put("entry_date", entry_date);

            rent_period.put("data", Integer.parseInt(rentalPeriodET.getText().toString().trim()));
            rent_period.put(rentalPeriodSwitch.getTag() + "", rentalPeriodSwitch.isChecked());
            obj.put("rent_period", rent_period);

            obj.put(rentalCB.getTag() + " ", rentalCB.isChecked());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> map) {

        for (int i = root.getChildCount() - 1; i >= 0; i--) {
            try {
                for (int j = ((ViewGroup) root.getChildAt(i)).getChildCount() - 1; j >= 0; j--) {
                    View v = ((ViewGroup) root.getChildAt(i)).getChildAt(j);
                    if (v instanceof EditText | v instanceof AutoCompleteTextView) {
                        String input = ((EditText) v).getText().toString();
                        if (input.equals("")) {
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
        map.put("is_extendable", rentalCB.isChecked() + "");

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
                map.get("appartment").equals(property.street_number) &&
                map.get("floor").equals(property.floor) &&
                map.get("max_floor").equals(property.max_floor) &&
                map.get("rooms").equals(property.roomsNumber) &&
                map.get("toilets").equals(property.toilets) &&
                map.get("bathrooms").equals(property.bathrooms) &&
                map.get("size").equals(property.size) &&
                map.get("entry_date").equals(property.entry_date) &&
                map.get("rent_period").equals(property.rentPeriod));
    }

    private void validateET(TextInputLayout v) {
        if (v.equals(maxFloorInputLayout)) {
            String maxFloorsStr = maxFloorET.getText() + "";
            String floorStr = floorET.getText() + "";
            if (!maxFloorsStr.equals("") && !floorStr.equals("")) {
                int maxFloors = Integer.parseInt(maxFloorsStr);
                int floors = Integer.parseInt(floorStr);
                if (floors > maxFloors) {
                    floorET.requestFocus();
                    floorInputLayout.setError("Floor is greater than max floor");
                    return;
                }
            }
        }
        EditText child = v.getEditText();
        if (child.getText().toString().trim().isEmpty()) {
            v.setError("שגיאה");
            v.requestFocus();
        } else {
            v.setErrorEnabled(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.addProp_asset_rentalPeriod_switch:
                rentalPeriodTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
            case R.id.addProp_asset_entry_date_switch:
                entryDateTV.setText(isChecked ? "גמיש" : "לא גמיש");
                break;
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
                case R.id.addProp_city_actv:
                    validateET(cityInputLayout);
                    break;
                case R.id.addProp_street_actv:
                    validateET(streetInputLayout);
                    break;
                case R.id.addProp_house_number_editText:
                    validateET(houseInputLayout);
                    break;
                case R.id.addProp_aptNo_editText:
                    validateET(aptNoInputLayout);
                    break;
                case R.id.addProp_floor_editText:
                    validateET(floorInputLayout);
                    break;
                case R.id.addProp_maxFloor_editText:
                    validateET(maxFloorInputLayout);
                    break;
                case R.id.addProp_rooms_editText:
                    validateET(roomInputLayout);
                    break;
                case R.id.addProp_toilets_editText:
                    validateET(toiletInputLayout);
                    break;
                case R.id.addProp_bathrooms_editText:
                    validateET(bathroomInputLayout);
                    break;
                case R.id.addProp_size_editText:
                    validateET(sizeInputLayout);
                    break;
                case R.id.addProperty_rentalPeriod_months_editText:
                    validateET(rentalPeriodInputLayout);
                    break;
            }
        }
    }
}