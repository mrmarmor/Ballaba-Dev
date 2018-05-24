package com.example.michaelkibenko.ballaba.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.Presenters.PropertyManagementPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyManagementBinding;

import org.json.JSONObject;

public class PropertyManagementActivity extends BaseActivityWithActionBar {
    private final static String TAG = PropertyManagementActivity.class.getSimpleName();

    private ActivityPropertyManagementBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_property_management);
        binder.setPresenter(new PropertyManagementPresenter(this, binder));

        getSupportActionBar().setElevation(0);
        //ViewCompat.setElevation(binder.propertyManagementTabsRoot, 8);
    }

    //Here we add a back button at the right corner of the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                super.onBackPressed();
        }

        return true;
    }

}