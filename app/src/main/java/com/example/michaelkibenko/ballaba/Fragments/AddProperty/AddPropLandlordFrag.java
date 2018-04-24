package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropLandlordBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AddPropLandlordFrag extends Fragment implements View.OnClickListener/*, BallabaFragmentListener*/ {
    public final static int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_GALLERY = 2;
    private static final String TAG = AddPropLandlordFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropLandlordBinding binderLandLord;
    private BottomSheetDialog bottomSheetDialog;
    public BallabaUser user = BallabaUserManager.getInstance().getUser();

    public AddPropLandlordFrag() {}

    public static AddPropLandlordFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropLandlordFrag fragment = new AddPropLandlordFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binderLandLord = FragmentAddPropLandlordBinding.inflate(getLayoutInflater());
        View view = binderLandLord.getRoot();

        //TODO i tried to setText automatically from layout by dataBinding. for some reason it is not working.
        //TODO we need to fill all editTexts in this way and not programmatically as below:
        //binderLandLord.addPropPhoneEditText.setText(user.getPhone());

        initEditTexts();
        initButtons(view);
        return view;
    }

    private void initEditTexts(){
        binderLandLord.addPropFirstNameEditText.setText(user.getFirst_name());
        binderLandLord.addPropLastNameEditText.setText(user.getLast_name());
        binderLandLord.addPropEmailEditText.setText(user.getEmail());
        binderLandLord.addPropPhoneEditText.setText(user.getPhone());
        binderLandLord.addPropCityEditText.setText(user.getCity());
        binderLandLord.addPropAddressEditText.setText(user.getAddress());
        binderLandLord.addPropAptNoEditText.setText(user.getApt_no());
        //TODO aboutYourself field is missing. i do not receive it from server and it is not is class BallabaUser
    }

    private void initButtons(View view){
        view.findViewById(R.id.addProperty_landlord_button_next).setOnClickListener(this);
        view.findViewById(R.id.addProp_profile_imageButton).setOnClickListener(this);
    }

    public void onClickProfileImage(){
        View sheetView = getLayoutInflater().inflate(R.layout.take_pic_switch, null);
        sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
        sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (resultCode == RESULT_OK && imageIntent != null){
            Uri selectedImage = imageIntent.getData();

            switch(requestCode) {
                case REQUEST_CODE_CAMERA: case REQUEST_CODE_GALLERY:
                    binderLandLord.addPropProfileImageButton.setImageURI(selectedImage);
                    break;
            }
        }
    }

    private HashMap<String, String> storeDataOnFinish(HashMap<String, String> map){
        for (int i = 0; i < binderLandLord.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binderLandLord.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                map.put(v.getTag()+"", ((EditText)v).getText()+"");
            }
        }

        map.put(binderLandLord.addPropAboutYourselfEditText.getTag()+""
                , binderLandLord.addPropAboutYourselfEditText.getText()+"");
        map.put(binderLandLord.addPropProfileImageButton.getTag()+""
                , StringUtils.getInstance(true, context).DrawableToString(binderLandLord.addPropProfileImageButton.getDrawable()));

        Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));

        return map;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProp_profile_imageButton:
                onClickProfileImage();
                break;

            case R.id.takePic_button_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_CODE_CAMERA);
                bottomSheetDialog.dismiss();
                break;

            case R.id.takePic_button_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_CODE_GALLERY);
                bottomSheetDialog.dismiss();
                break;

            case R.id.addProperty_landlord_button_next:
                //Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();
                HashMap<String, String> data = storeDataOnFinish(new HashMap<String, String>());
                ConnectionsManager.getInstance(context).uploadUser(user.getId(), data);
                new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(data, 0);
        }
    }

}