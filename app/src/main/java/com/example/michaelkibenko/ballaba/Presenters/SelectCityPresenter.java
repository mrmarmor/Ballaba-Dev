package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SelectCitySubActivity;
import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
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

import static com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter.GooglePlacesFilter.CITIES;

/**
 * Created by User on 20/03/2018.
 */

public class SelectCityPresenter extends BasePresenter implements
        BallabaLocationManager.OnGoogleMapListener, ListView.OnItemClickListener,
        EditText.OnFocusChangeListener{
    public final static String TAG = SelectCityPresenter.class.getSimpleName(),
                SELECTED_CITIES_KEY = "selected_city";

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
        //initAutoCompleteTextView(binder.selectCityAutoCompleteTextView);//TODO with autoCompleteTextView

        initListView(binder.selectCityListView, binder.selectCityEditText);//TODO with listView
    }

    //private void initAutoCompleteTextView(AutoCompleteTextView autoComplete){//TODO with autoCompleteTextView
    private void initListView(final ListView listView, final EditText editText){//TODO with listView

        //TODO if you want actvSearchPlace display device current address:
            //setListAdapterToDeviceAddress(listView);

        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(
                activity, android.R.layout.simple_list_item_1,
                //GooglePlacesAdapter.GooglePlacesFilter.REGION+"haifa");
                GooglePlacesAdapter.GooglePlacesFilter.CITIES);
        listView.setAdapter(dataAdapter);
        TextView textView = new TextView(activity);
        textView.setText(activity.getString(R.string.selectCity_autoCompleteTextView_hint_defaultItem));
        listView.setEmptyView(textView);
        listView.setOnItemClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {//TODO with listView
        //view.addTextChangedListener(new TextWatcher() {//TODO with autoCompleteTextView
            public void afterTextChanged(Editable s) {listView.setAdapter(dataAdapter); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
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
        Toast.makeText(activity, "chips left: " + cities.size(), Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public void onOKButtonClick(){
        for (String city : cities)
            Log.d(TAG, city+"\n");

        activity.getIntent().putExtra(SELECTED_CITIES_KEY, cities);
        activity.setResult(Activity.RESULT_OK, activity.getIntent());
        activity.finish();
    }

    @Override
    public void OnGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedCity = ((TextView)view).getText().toString();
        //actvSelectCity.setText(selectedCity);
        if (cities.contains(selectedCity)){
            Toast.makeText(activity, "TESTING: You have already typed " + selectedCity, Toast.LENGTH_LONG).show();
        } else {
            binder.selectCityEditText.setText("");//TODO with listView
            //((TextView) view).setText("");//TODO with autoCompleteTextView
            cities.add(selectedCity);
            addCityToFlowLayout(selectedCity);
        }

        //BallabaMapFragment.newInstance().onItemSelected(googleMap, selectedPlace);
    }

    //when editText lose his focus he loses his GooglePlaces adapter. so i need to give him it back
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            initListView(binder.selectCityListView, binder.selectCityEditText);
        }
    }

  /*  @Override
    public void onItemSelected(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }*/
}
