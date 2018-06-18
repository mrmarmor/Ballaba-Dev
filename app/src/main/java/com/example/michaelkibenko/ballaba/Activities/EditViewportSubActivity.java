package com.example.michaelkibenko.ballaba.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.google.android.gms.maps.model.LatLng;

import static java.sql.Types.NULL;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EditViewportSubActivity extends BaseActivity {
    private static final String TAG = EditViewportSubActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_viewport);

        double latitude = getIntent().getDoubleExtra("lat", NULL);
        double longitude = getIntent().getDoubleExtra("lng", NULL);

        BallabaMapFragment mapFragment = (BallabaMapFragment)getSupportFragmentManager().findFragmentById(R.id.editViewport_mapFragment);
        //BallabaMapFragment.newInstance();
        mapFragment.setLocation(new LatLng(latitude, longitude));
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }*/
}