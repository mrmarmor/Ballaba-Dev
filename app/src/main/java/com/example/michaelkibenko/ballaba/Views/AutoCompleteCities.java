package com.example.michaelkibenko.ballaba.Views;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
//import android.widget.AutoCompleteTextView;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.widget.AutoCompleteTextView;

import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;

/**
 * Created by User on 22/05/2018.
 */

public class AutoCompleteCities extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    private Context context;
    public AutoCompleteCities(Context context) {
        super(context);
        this.context = context;
    }

    public AutoCompleteCities(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public AutoCompleteCities(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);

        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(context
                , android.R.layout.simple_list_item_1, GooglePlacesAdapter.GooglePlacesFilter.CITIES);

        addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });
    }

    public void initAutoCompleteCity(Context ctx, final AutoCompleteTextView autoCompleteTextView){
        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(ctx
                , android.R.layout.simple_list_item_1, GooglePlacesAdapter.GooglePlacesFilter.CITIES);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                autoCompleteTextView.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });
        // binderLandLord.addPropCityActv.setOnFocusChangeListener(this);
    }

}
