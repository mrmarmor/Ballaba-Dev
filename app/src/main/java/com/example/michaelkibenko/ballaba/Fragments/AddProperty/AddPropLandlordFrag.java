package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
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
    private boolean isProfileImageChanged = false;

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
        if (user != null) {
            binderLandLord.addPropFirstNameEditText.setText(user.getFirst_name());
            binderLandLord.addPropLastNameEditText.setText(user.getLast_name());
            binderLandLord.addPropEmailEditText.setText(user.getEmail());
            binderLandLord.addPropPhoneEditText.setText(user.getPhone());
            binderLandLord.addPropCityEditText.setText(user.getCity());
            binderLandLord.addPropAddressEditText.setText(user.getAddress());
            binderLandLord.addPropAptNoEditText.setText(user.getApt_no());
            //Glide.with(context).load(user.getProfile_image()).into(binderLandLord.addPropProfileImageButton);

            //TODO aboutYourself field is missing. i do not receive it from server and it is not is class BallabaUser
        }
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
                    isProfileImageChanged = true;
                    break;
            }
        }
    }

    private HashMap<String, String> getDataFromEditTexts(HashMap<String, String> map){
       // boolean isUserChangedData = false;
        for (int i = 0; i < binderLandLord.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binderLandLord.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
        //        if (!map.containsValue(((EditText)v).getText()+"")){
                    map.put(v.getTag()+"", ((EditText)v).getText()+"");
         //           isUserChangedData = true;
      //          }

            }
        }

        map.put(binderLandLord.addPropAboutEditText.getTag()+""
                , binderLandLord.addPropAboutEditText.getText()+"");

        //Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));

        return map;
    }
    private HashMap<String, byte[]> getProfileImage(HashMap<String, byte[]> map){
        if (isProfileImageChanged) {
            Drawable d = binderLandLord.addPropProfileImageButton.getDrawable();
            byte[] value = StringUtils.getInstance(true, context).DrawableToBytes(d);
            map.put("profile_image", value);
        }

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
                final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
                HashMap<String, byte[]> profileImage = getProfileImage(new HashMap<String, byte[]>());

                if (user != null && !isDataEqual(data, user)) {
                    ConnectionsManager.getInstance(context).uploadUser(user.getId(), data, profileImage, new BallabaResponseListener() {
                        @Override
                        public void resolve(BallabaBaseEntity entity) {
                            SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.USER_ID, user.getId());
                            AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).getDataFromFragment(0);
                        }

                        @Override
                        public void reject(BallabaBaseEntity entity) {
                            showSnackBar(((BallabaErrorResponse) entity).message);

                            //TODO NEXT LINE IS ONLY FOR TESTING:
                            //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(0);
                        }
                    });
                } else {
                    AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).getDataFromFragment(0);
                }
        }
    }

    private void showSnackBar(String message){
        final View snackBarView = binderLandLord.addPropertyRoot;
        Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
        snackBar.show();
        //snackBarView.findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    private boolean isDataEqual(HashMap<String, String> map, BallabaUser user){
        return (map.get("first_name").equals(user.getFirst_name()) &&
                map.get("last_name").equals(user.getLast_name()) &&
                map.get("email").equals(user.getEmail()) &&
                map.get("phone").equals(user.getPhone()) &&
                map.get("city").equals(user.getCity()) &&
                map.get("address").equals(user.getAddress()) &&
                map.get("apt_no").equals(user.getApt_no()) &&
                //TODO map.get("description").equals(user.getDescription()) &&
                !isProfileImageChanged);
    }

}