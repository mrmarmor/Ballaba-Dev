package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SelectCityPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivitySelectCityBinding;
import com.google.android.gms.maps.GoogleMap;

public class SelectCitySubActivity extends BaseActivity
        implements BallabaLocationManager.OnGoogleMapListener{

    private final String TAG = SelectCitySubActivity.class.getSimpleName();

    private ActivitySelectCityBinding binder;
    private SelectCityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        presenter = new SelectCityPresenter(this, binder);
        binder.setPresenter(presenter);
    }

    @Override
    public void OnGoogleMap(GoogleMap googleMap) {
        Log.d(TAG, "data from fragment: "+googleMap);
        presenter.OnGoogleMap(googleMap);
    }
}
