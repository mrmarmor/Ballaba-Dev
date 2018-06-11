package com.example.michaelkibenko.ballaba.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

/**
 * Created by User on 23/04/2018.
 */

public class ContinueAddPropertyActivity extends BaseActivity {
    private final String TAG = ContinueAddPropertyActivity.class.getSimpleName();

    private boolean isFetchingDataEndedSuccessfully = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_add_property);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchPropertyDataFromServer();
    }

    public void onClickOk(View view){
        if (isFetchingDataEndedSuccessfully)
            startActivity(new Intent(this, AddPropertyActivityNew.class));
        else
            Toast.makeText(this, getString(R.string.error_property_download), Toast.LENGTH_LONG).show();
    }

    public void onClickCancel(View view){
        finish();
    }

    private void fetchPropertyDataFromServer(){
        String propertyId = getIntent().getStringExtra(PropertyDescriptionActivity.PROPERTY);
        ConnectionsManager.getInstance(this).getPropertyById(propertyId, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    final Activity THIS = ContinueAddPropertyActivity.this;
                    BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager.getInstance(THIS)
                            .parsePropertiesFull(((BallabaOkResponse)entity).body);
                    BallabaSearchPropertiesManager.getInstance(THIS).setPropertyFull(propertyFull);

                    Log.d(TAG, "properties: " + propertyFull.formattedAddress+":"+propertyFull.street);
                    isFetchingDataEndedSuccessfully = true;
                }else {
                    Log.e(TAG, "error: Response is not an instance of BallabaOkResponse");
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d(TAG, "properties: error" );
                //callback.reject(entity);
            }
        });
    }
}