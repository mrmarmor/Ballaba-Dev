package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropLandlordBinding;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AddPropLandlordFrag extends Fragment implements Button.OnClickListener
        , BallabaFragmentListener {
    public final static int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_GALLERY = 2;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddPropLandlordFrag.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    private Context context;
    private FragmentAddPropLandlordBinding binder;
    private BallabaFragmentListener mListener;
    private BottomSheetDialog bottomSheetDialog;

    public AddPropLandlordFrag() {}

    public static AddPropLandlordFrag newInstance() {
        AddPropLandlordFrag fragment = new AddPropLandlordFrag();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_prop_landlord, container, false);
        //binder = DataBindingUtil.inflate(inflater, R.layout.fragment_add_prop_landlord, container, false);
        return v;//binder.getRoot();
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
                    binder.addPropProfileImageButton.setImageURI(selectedImage);
                    break;
            }
        }
    }

    private void storeDataOnFinish(HashMap<String, String> map){
        //Intent intent = new Intent(context, AddPropertyActivity.class);
        for (int i = 0; i < binder.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binder.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                map.put(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        map.put(binder.addPropAboutYourselfEditText.getTag()+""
                , binder.addPropAboutYourselfEditText.getText()+"");

        Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));
    }
    public HashMap<String, String> getFieldsData(){
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < binder.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binder.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                map.put(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        map.put(binder.addPropAboutYourselfEditText.getTag()+""
                , binder.addPropAboutYourselfEditText.getText()+"");

        Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));

        return map;
    }


    public void onButtonPressed(HashMap<String, String> map) {
        if (mListener != null) {
            mListener.onFragmentInteraction(map);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        if (context instanceof BallabaFragmentListener) {
            mListener = (BallabaFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    @Override
    public void onFragmentInteraction(HashMap<String, String> map) {
        for (int i = 0; i < binder.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binder.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                map.put(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        map.put(binder.addPropAboutYourselfEditText.getTag()+""
                , binder.addPropAboutYourselfEditText.getText()+"");

        Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));

    }
}
