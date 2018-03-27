package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaSelectedCityListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivitySelectCityBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by User on 20/03/2018.
 */

public class SelectCityPresenter extends BasePresenter implements
        BallabaLocationManager.OnGoogleMapListener, BallabaSelectedCityListener {
    public final static String TAG = SelectCityPresenter.class.getSimpleName(),
                SELECTED_CITY_KEY = "selected_city";

    private Activity activity;
    private ActivitySelectCityBinding binder;

    private AutoCompleteTextView actvSelectCity;
    private ArrayList<String> cities;

    private BallabaLocationManager.OnGoogleMapListener mListener;
    private GoogleMap googleMap;

    //public SelectCityPresenter(){}
    public SelectCityPresenter(Context context, ActivitySelectCityBinding binder){
        this.binder = binder;
        this.activity = (Activity)context;

        cities = new ArrayList<>();
        //initGoogleMapListener();
        initAutoCompleteTextView();
    }

    /*private void initGoogleMapListener(){
        if (context instanceof BallabaLocationManager.OnGoogleMapListener) {
            mListener = (BallabaLocationManager.OnGoogleMapListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGoogleMapListener");
        }
    }*/

    private void initAutoCompleteTextView(){
        //TODO if you want actvSearchPlace display device current address:
        //setListAdapterToDeviceAddress(listView);

        actvSelectCity = binder.selectCityAutoCompleteTextView;

        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(
                activity, android.R.layout.simple_list_item_1);
        actvSelectCity.setAdapter(dataAdapter);

        actvSelectCity.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {actvSelectCity.setAdapter(dataAdapter); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        actvSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = ((TextView)view).getText().toString();
                //actvSelectCity.setText(selectedCity);
                actvSelectCity.setText("");
                cities.add(selectedCity);
                addCityToFlowLayout(selectedCity);

                //BallabaMapFragment.newInstance().onItemSelected(googleMap, selectedPlace);

                ////TODO TESTING! These line should appear in Done button to close this activity/presenter and return back to MainActivity

                /*activity.getIntent().putExtra(SELECTED_CITIES_KEY, cities);
                activity.setResult(Activity.RESULT_OK, activity.getIntent());
                activity.finish();*/
                ////
            }
        });

        UiUtils.instance(true, activity).showSoftKeyboard();

    }

    private void addCityToFlowLayout(String text){
        //Inflater inflater = new Inflater().
        final View view = activity.getLayoutInflater().inflate(R.layout.chip_with_x, null);
        ((TextView)view.findViewById(R.id.chip_textView)).setText(text);
        view.findViewById(R.id.chip_x_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.selectCityFlowLayout.removeView(view);
                cities.remove(((TextView)view.findViewById(R.id.chip_textView)).getText());
                Toast.makeText(activity, "chips left: " + cities.size(), Toast.LENGTH_SHORT).show();
            }
        });
        binder.selectCityFlowLayout.addView(view);
    }
    public void removeCityFromFlowLayout(View view){
        Toast.makeText(activity, view.getClass().getCanonicalName(), Toast.LENGTH_SHORT).show();
        //binder.selectCityFlowLayout.removeView(((view.getParent());
    }

    private TextView buildLabel(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setBackgroundResource(R.drawable.chip);
        textView.setPadding(8,8,8,8);
        textView.setGravity(Gravity.START);

        return textView;
    }

    @Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, googleMap.getMaxZoomLevel()+"");
        this.googleMap = googleMap;
    }

    @Override
    public void onItemSelected(GoogleMap googleMap, LatLng location) {
        this.googleMap = googleMap;
        //this.location = location;
    }
}
