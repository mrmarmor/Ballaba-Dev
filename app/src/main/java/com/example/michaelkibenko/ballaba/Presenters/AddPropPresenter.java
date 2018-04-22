package com.example.michaelkibenko.ballaba.Presenters;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.AddProperty.AddPropLandlordActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWorkActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropLandlordBinding;
import com.example.michaelkibenko.ballaba.databinding.ActivityScoringPersonalBinding;

import java.util.ArrayList;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropPresenter implements Button.OnClickListener{
    private static String TAG = AddPropPresenter.class.getSimpleName();

    private AppCompatActivity activity;
    private ActivityAddPropLandlordBinding binder;
    private ViewGroup root;
    BottomSheetDialog bottomSheetDialog;

    public AddPropPresenter(AppCompatActivity activity, ActivityAddPropLandlordBinding binding) {
        this.activity = activity;
        this.binder = binding;
        this.root = binder.addPropertyRoot;

        binder.addPropertyLandlordButtonNext.setOnClickListener(this);
    }

    public void onClickProfileImage(){
        View sheetView = activity.getLayoutInflater().inflate(R.layout.take_pic_switch, null);
        sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
        sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.takePic_button_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(takePicture, AddPropLandlordActivity.REQUEST_CODE_CAMERA);
                bottomSheetDialog.dismiss();
                break;

            case R.id.takePic_button_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(pickPhoto , AddPropLandlordActivity.REQUEST_CODE_GALLERY);
                bottomSheetDialog.dismiss();
                break;

            case R.id.addProperty_landlord_button_next:
                Toast.makeText(activity, "next", Toast.LENGTH_SHORT).show();
                storeDataOnFinish();
        }
    }

    private void storeDataOnFinish(){
        Intent intent = new Intent(activity, AddPropLandlordActivity.class);
        for (int i = 0; i < binder.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binder.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                intent.putExtra(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        intent.putExtra(binder.addPropAboutYourselfEditText.getTag()+""
                , binder.addPropAboutYourselfEditText.getText()+"");

        Log.d(TAG, intent.getStringExtra("aboutYourself")+":"+intent.getStringExtra("firstName"));
    }

}
