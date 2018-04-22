package com.example.michaelkibenko.ballaba.Activities.AddProperty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Presenters.AddPropPresenter;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropLandlordBinding;

public class AddPropLandlordActivity extends AppCompatActivity {
    private final static String TAG = AddPropLandlordActivity.class.getSimpleName();
    public final static int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_GALLERY = 2;

    private AddPropPresenter presenter;
    private ActivityAddPropLandlordBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_add_prop_landlord);
        binder.setPresenter(new AddPropPresenter(this, binder));

        UiUtils.instance(true, this).hideSoftKeyboard(binder.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorPrimary, getTheme())));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (resultCode == RESULT_OK && imageIntent != null){
            Uri selectedImage = imageIntent.getData();

            switch(requestCode) {
                case REQUEST_CODE_CAMERA: case REQUEST_CODE_GALLERY:
                    binder.addPropProfileImageButton.setImageURI(selectedImage);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
