package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivitySelectCityBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by User on 20/03/2018.
 */

public class SelectCityPresenter extends BasePresenter implements ListView.OnItemClickListener
            , EditText.OnFocusChangeListener{

    public final static String TAG = SelectCityPresenter.class.getSimpleName(),
                SELECTED_CITIES_KEY = "selected_city";

    private Activity activity;
    private ActivitySelectCityBinding binder;

    private ArrayList<String> cities;
    private boolean citySelected;
    private String selectedCity;
    private GooglePlacesAdapter dataAdapter;

    public SelectCityPresenter(Context context, ActivitySelectCityBinding binder){
        this.binder = binder;
        this.activity = (Activity)context;

        cities = new ArrayList<>();
        //initGoogleMapListener();
        //initAutoCompleteTextView(binder.selectCityAutoCompleteTextView);//TODO with autoCompleteTextView

        initListView(binder.selectCityListView, binder.selectCityEditText);//TODO with listView

        binder.inMyArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByLocationFlow();
            }
        });
    }

    private void searchByLocationFlow() {
        activity.setResult(MainActivity.SEARCH_BY_LOCATION_REQUEST_CODE);
        activity.finish();
    }

    //private void initAutoCompleteTextView(AutoCompleteTextView autoComplete){//TODO with autoCompleteTextView
    private void initListView(final ListView listView, final EditText editText){//TODO with listView

        //TODO if you want actvSearchPlace display device current address:
            //setListAdapterToDeviceAddress(listView);

        dataAdapter = new GooglePlacesAdapter(
                activity, android.R.layout.simple_list_item_1,
                GooglePlacesAdapter.GooglePlacesFilter.CITIES);
        TextView textView = new TextView(activity);
        textView.setText(activity.getString(R.string.selectCity_autoCompleteTextView_hint_defaultItem));
        listView.setEmptyView(textView);//hint
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {//TODO with listView
        //view.addTextChangedListener(new TextWatcher() {//TODO with autoCompleteTextView
            public void afterTextChanged(Editable s) {listView.setAdapter(dataAdapter); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>0){
                    binder.inMyArea.setVisibility(View.GONE);
                }else if(!citySelected && cities.size() == 0){
                    binder.inMyArea.setVisibility(View.VISIBLE);
                }
                dataAdapter.getFilter().filter(citySelected ? selectedCity+" "+s.toString() : s.toString());
            }
        });
        editText.setOnFocusChangeListener(this);

        UiUtils.instance(true, activity).showSoftKeyboard();

    }

    private void addCityToFlowLayout(String text){
        final View view = activity.getLayoutInflater().inflate(R.layout.chip_with_x, null);
        final TextView textViewCity = ((TextView)view.findViewById(R.id.chip_textView));
        textViewCity.setText(text);
        view.findViewById(R.id.chip_x_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCityFromFlowLayout(view, textViewCity);
            }
        });
        binder.selectCityFlowLayout.addView(view);
    }

    private void removeCityFromFlowLayout(@NonNull View view, @NonNull TextView textViewCity) {
        binder.selectCityFlowLayout.removeView(view);
        String cityStr = textViewCity.getText().toString();
        cities.remove(cityStr);
        if(cities.size() == 0){
            dataAdapter.setGpFilter(GooglePlacesAdapter.GooglePlacesFilter.CITIES);
            citySelected = false;
        }
    }

    public void onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public void onOKButtonClick(){
        if(cities.size() == 0 && selectedCity != null){
            cities.add(selectedCity);
        }
        activity.getIntent().putExtra(SELECTED_CITIES_KEY, cities);
        activity.setResult(Activity.RESULT_OK, activity.getIntent());
        activity.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedCity = ((TextView)view).getText().toString().replace(", Israel", "");
        if (cities.contains(selectedCity)){
            Toast.makeText(activity, "TESTING: You have already typed " + selectedCity, Toast.LENGTH_LONG).show();
        } else {
            binder.selectCityEditText.setText("");//TODO with listView
            if(!citySelected){
                citySelected = true;
                this.selectedCity = selectedCity;
                dataAdapter.setGpFilter(GooglePlacesAdapter.GooglePlacesFilter.GEOCODE);
            }else {
                cities.add(selectedCity);
            }
            addCityToFlowLayout(selectedCity);//TODO do so for other countries
        }

        binder.selectCityOKButton.setVisibility(View.VISIBLE);

    }

    //when editText lose his focus he loses his GooglePlaces adapter. so i need to give him it back
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            initListView(binder.selectCityListView, binder.selectCityEditText);
        }
    }

}
