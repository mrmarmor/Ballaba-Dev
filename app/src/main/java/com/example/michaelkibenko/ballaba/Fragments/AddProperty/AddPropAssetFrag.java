package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.icu.util.HebrewCalendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddPropAssetFrag extends Fragment {
    private static final String TAG = AddPropAssetFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAssetBinding binderAsset;
    //private TextView yearTextView;

    public AddPropAssetFrag() {}
    public static AddPropAssetFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAssetFrag fragment = new AddPropAssetFrag();
        binderMain = binding;
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binderAsset = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_asset, container, false);
        View view = binderAsset.getRoot();
        binderAsset.addPropertyAssetButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
                if (!isDataEqual(data))
                    ConnectionsManager.getInstance(context).uploadProperty(data, "create", new BallabaResponseListener() {
                        @Override
                        public void resolve(BallabaBaseEntity entity) {
                            SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.USER_ID, ((BallabaUser)entity).getId());
                            new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(data, 1);
                        }

                        @Override
                        public void reject(BallabaBaseEntity entity) {
                            showSnackBar();

                            //TODO NEXT LINE IS ONLY FOR TESTING:
                            new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(data, 1);
                        }
                    });
            }
        });

        initEditTexts(BallabaSearchPropertiesManager.getInstance(context).getPropertyFull());
        customizeDatePicker("he");

        return view;
    }

    private void initEditTexts(BallabaPropertyFull property){
        if (property != null) {
            binderAsset.addPropCityEditText.setText(property.city);
            binderAsset.addPropAddressEditText.setText(property.formattedAddress);
            binderAsset.addPropAptNoEditText.setText(property.street_number);
            binderAsset.addPropFloorEditText.setText(property.floor);
            binderAsset.addPropMaxFloorEditText.setText(property.max_floor);
            binderAsset.addPropRoomsEditText.setText(property.roomsNumber);
            binderAsset.addPropToiletsEditText.setText(property.toilets);
            binderAsset.addPropBathroomsEditText.setText(property.bathrooms);
            binderAsset.addPropSizeEditText.setText(property.size);
        }
    }

    private void customizeDatePicker(String locale){
        Resources res = context. getResources();

        //1. change datePicker language to hebrew. TODO IF WE WANT OTHER LANGUAGES, USE OTHER LOCALES!
        //1.a. change datePicker frame to hebrew:
        Locale.setDefault(new Locale("he"));

        //1.b. change calendar frame to hebrew:
        Configuration hebrewConfiguration = res.getConfiguration();
        hebrewConfiguration.setLocale(new Locale(locale));
        context.createConfigurationContext(hebrewConfiguration);

        //2. replace "2018" field from datePicker header with "תאריך כניסה" field
        ViewGroup group = binderAsset.addPropAssetRentalPeriodDatePicker;
        try {
            group = (ViewGroup) group.getChildAt(0);//=layoutManager
            group = (ViewGroup)group.getChildAt(0);//=child layoutManager
            final TextView titleTextView = (TextView)group.getChildAt(0);//==textView "2018"
            titleTextView.setText(getString(R.string.addProperty_asset_dateOfEntrance));
            titleTextView.setPaddingRelative(0, 8, 0, 8);
            titleTextView.setTextSize(14);

            //to prevent OS from writing "2018" when user changes date
            titleTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals(Calendar.getInstance().get(Calendar.YEAR)+""))
                        titleTextView.setText(getString(R.string.addProperty_asset_dateOfEntrance));
                }
            });

            TextView formattedDateTextView = (TextView)group.getChildAt(1);
            formattedDateTextView.setTextSize(22);
            formattedDateTextView.setTypeface(null, Typeface.BOLD);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void showSnackBar(){
        final View snackBarView = binderAsset.addPropertyLocationRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, "השמירה נכשלה נסה שנית מאוחר יותר", Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> map){
        LinearLayout root = binderAsset.addPropertyLocationRoot;
        for (int i = 0; i < root.getChildCount(); i++) {
            try {
                for (int j = 0; j < ((ViewGroup)root.getChildAt(i)).getChildCount(); j++) {
                    View v = ((ViewGroup)root.getChildAt(i)).getChildAt(j);
                    if (v instanceof EditText) {
                        map.put(v.getTag() + "", ((EditText) v).getText() + "");
                    }
                }
            } catch (ClassCastException e){
                Log.e(TAG, e.getMessage()+"\n class is:" + root.getChildAt(i).getClass());
            }
        }

        DatePicker picker = binderAsset.addPropAssetRentalPeriodDatePicker;
        map.put("entry_date", String.format("%d-%02d-%02d", picker.getYear(), picker.getMonth()+1, picker.getDayOfMonth()));
        map.put("is_extendable", binderAsset.addPropertyRentalPeriodOptionTextView.isChecked()+"");

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

}