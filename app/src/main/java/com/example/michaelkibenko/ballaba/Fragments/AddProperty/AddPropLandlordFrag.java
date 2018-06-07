package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropLandlordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AddPropLandlordFrag extends Fragment implements View.OnClickListener/*, EditText.OnFocusChangeListener*/ {

    public final static int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_GALLERY = 2;
    private static final String TAG = AddPropLandlordFrag.class.getSimpleName();

    private Context context;
    private ActivityAddPropertyBinding binderMain;
    private FragmentAddPropLandlordBinding binderLandLord;
    private BottomSheetDialog bottomSheetDialog;
    private BallabaUserManager userManager = BallabaUserManager.getInstance();
    public BallabaUser user;
    private boolean isProfileImageChanged = false;
    private boolean areAllDataFieldsFilledUp = true;

    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf;

    private ArrayList<String> cities;

    public AddPropLandlordFrag() {}

    public static AddPropLandlordFrag newInstance() {
        AddPropLandlordFrag fragment = new AddPropLandlordFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binderLandLord = FragmentAddPropLandlordBinding.inflate(getLayoutInflater());
        View view = binderLandLord.getRoot();

        user = userManager.getUser();
        initEditTexts();
        initButtons(view);



        binderLandLord.addPropBirthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.instance(true , context).hideSoftKeyboard(v);
                showDatePicker();
            }
        });

        return view;
    }

    public AddPropLandlordFrag setMainBinder(ActivityAddPropertyBinding binder){
        this.binderMain = binder;
        return this;
    }

    private void showDatePicker() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
                Log.d(TAG, myCalendar.toString());
            }

        };
        if (user == null){//show default date
            myCalendar.set(1995, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        } else {//show user date of birth
            String birthDate = user.getBirth_date();
            myCalendar.set(Integer.parseInt(birthDate.split("-")[0])//year
                         , Integer.parseInt(birthDate.split("-")[2].substring(0, 2)) - 1//month
                         , Integer.parseInt(birthDate.split("-")[1]));//day
        }

        new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
            .show();

    }

    private void updateDate() {
        String myFormat = "dd / MM / yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        String formattedDate = sdf.format(myCalendar.getTime());
        String[] date = formattedDate.split(" / ");

        String year = date[0];
        String month = date[1];
        String day = date[2];

        Calendar minimumYearsCheckCalendar = Calendar.getInstance();
        minimumYearsCheckCalendar.setTime(new Date());
        minimumYearsCheckCalendar.add(Calendar.YEAR, -18);

        if(myCalendar.before(minimumYearsCheckCalendar)){ // user above 18 years old

            Log.d(TAG, "updateDate: " + year + " " + month + " " + day);
            /*String[] monthArr = context.getResources().getStringArray(R.array.months);
            for (int i = 0; i < monthArr.length; i++) {
                if (Integer.parseInt(month) == i) {
                    month = monthArr[i - 1];
                    break;
                }
            }*/
            //Toast.makeText(context, "" + day + " " + month + " " + year, Toast.LENGTH_SHORT).show();
            binderLandLord.addPropBirthDateEditText.setText(year + " / " + month + " / " + day);
        }else { // user under 18 years old

        }

        //binderLandLord.addPropBirthDateEditText.setText();
    }

    private void initEditTexts(){
        if (user != null) {
            Log.d(TAG, "User phone received from server: "+ user.getPhone());
            binderLandLord.addPropFirstNameEditText.setText(user.getFirst_name());
            binderLandLord.addPropLastNameEditText.setText(user.getLast_name());
            binderLandLord.addPropIdNumberEditText.setText(user.getId_number());
            binderLandLord.addPropBirthDateEditText.setText(user.getBirth_date().substring(0, 10));
            binderLandLord.addPropEmailEditText.setText(user.getEmail());
            binderLandLord.addPropPhoneEditText.setText(user.getPhone());
            binderLandLord.addPropCityActv.setText(user.getCity());
            binderLandLord.addPropAddressActv.setText(user.getAddress());
            binderLandLord.addPropHouseNoEditText.setText(user.getStreet_number());
            binderLandLord.addPropAptNoEditText.setText(user.getApt_no());
            binderLandLord.addPropAboutEditText.setText(user.getAbout());
            //binderLandLord.addPropBirthDateEditText.setText(user.getBirth_date());
            Glide.with(context).load(user.getProfile_image()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binderLandLord.addPropProfileImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.add_user_b_lue_60));
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    binderLandLord.addPropProfileImageButton.setImageDrawable(resource);
                    return true;
                }
            }).into(binderLandLord.addPropProfileImageButton);
        }
        binderLandLord.addPropFirstNameEditText.requestFocus();

        UiUtils.instance(true, context).initAutoCompleteCity(binderLandLord.addPropCityActv);
        UiUtils.instance(true, context).initAutoCompleteAddressInCity(binderLandLord.addPropAddressActv, binderLandLord.addPropCityActv);
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
        areAllDataFieldsFilledUp = true;
        for (int i = binderLandLord.addPropertyEditTextsRoot.getChildCount() - 1; i >= 0 ; i--){//root.getChildCount(); i++) {
            View v = binderLandLord.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText | v instanceof AutoCompleteTextView) {
                String input = ((EditText)v).getText()+"";
                if (input.equals("") && !v.getTag().equals("phone")) {
                    areAllDataFieldsFilledUp = false;
                    v.requestFocus();//move focus to empty field
                } else {
                    map.put(v.getTag() + "", input);
                }
            }
        }

        map.put(binderLandLord.addPropHouseNoEditText.getTag()+""
                , binderLandLord.addPropHouseNoEditText.getText()+"");
        map.put(binderLandLord.addPropAptNoEditText.getTag()+""
                , binderLandLord.addPropAptNoEditText.getText()+"");
        map.put(binderLandLord.addPropAboutEditText.getTag()+""
                , binderLandLord.addPropAboutEditText.getText()+"");

        //Log.d(TAG, map.get("aboutYourself")+":"+map.get("firstName"));

        return map;
    }

    private String getProfileImage(){
        if (isProfileImageChanged) {
            Drawable d = binderLandLord.addPropProfileImageButton.getDrawable();
            byte[] bytes = StringUtils.getInstance(true).DrawableToBytes(d);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }

        return null;
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
                onFinish(ConnectionsManager.newInstance(context));

        }
    }

    private void onFinish(ConnectionsManager conn){
        final HashMap<String, String> data = getDataFromEditTexts(new HashMap<String, String>());
        data.put("profile_image", getProfileImage());
        Log.d(TAG, "last name sent to server: "+data.get("last_name"));

        try {
            if (!areAllDataFieldsFilledUp ||/*!isPhoneValid(data.get("phone")) ||*/ !isEmailValid(data.get("email")))
                return;

            Log.d(TAG, "is data equal: " + isDataEqual(data, user));
            if (user != null && !isDataEqual(data, user)) {
                conn.uploadUser(binderMain, jsonParse(data), new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                     /*   SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.USER_ID, user.getId());
                        SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "1");
                        userManager.setUser((BallabaUser)entity);
                        Log.d(TAG, "last name received from server: "+userManager.getUser().getLast_name());
                        AddPropertyPresenter.getInstance((AppCompatActivity) context, binderMain).setViewPagerItem(1);*/
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        Toast.makeText(context, ((BallabaErrorResponse) entity).message, Toast.LENGTH_LONG);
                        ((BaseActivity)context).getDefaultSnackBar(binderLandLord.getRoot()
                                , ((BallabaErrorResponse) entity).message, false);

                        //TODO NEXT LINE IS ONLY FOR TESTING:
                        //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(0);
                    }
                });
            } else {
                AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).setViewPagerItem(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject jsonParse(HashMap<String, String> userData) throws JSONException{
        JSONObject jsonObject = new JSONObject();
            for (String key : userData.keySet()) {
                jsonObject.put(key, userData.get(key));
            }

        return jsonObject;
    }

    //TODO phone field is inactive, and user cannot change it. so, i don't need to check it. if it will be active, use this method to check value
    /*private boolean isPhoneValid(String phone){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        boolean isValid = GeneralUtils.instance(true, context).validatePhoneNumber(
                new BallabaPhoneNumber(phone, null), phoneUtil);
        if (!isValid){
            binderLandLord.addPropPhoneEditText.setTextColor(context.getResources().getColor(R.color.red_error_phone));
            binderLandLord.addPropPhoneEditText.setText("invalid phone number");
            ((ScrollView)binderLandLord.getRoot()).smoothScrollTo(0, binderLandLord.addPropPhoneEditText.getTop() - 30);
            binderLandLord.addPropPhoneEditText.requestFocus();
        }

        return isValid;
    }*/
    private boolean isEmailValid(String email){
        boolean isValid = !TextUtils.isEmpty(email)
                && (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isValid){
            binderLandLord.addPropEmailEditText.setTextColor(context.getResources().getColor(R.color.red_error_phone));
            binderLandLord.addPropEmailEditText.setText("invalid email address");
            ((ScrollView)binderLandLord.getRoot()).smoothScrollTo(0, binderLandLord.addPropEmailEditText.getTop() - 30);
            binderLandLord.addPropEmailEditText.requestFocus();
        }

        return isValid;
    }

    private boolean isDataEqual(HashMap<String, String> map, BallabaUser user){
        return (map.get("first_name").equals(user.getFirst_name()) &&
                map.get("last_name").equals(user.getLast_name()) &&
                map.get("email").equals(user.getEmail()) &&
                map.get("phone").equals(user.getPhone()) &&
                map.get("city").equals(user.getCity()) &&
                map.get("address").equals(user.getAddress()) &&
                map.get("apt_no").equals(user.getApt_no()) &&
                map.get("street_number").equals(user.getStreet_number()) &&
                map.get("about").equals(user.getAbout()) &&
                !isProfileImageChanged);
    }

    //when editText lose his focus he loses his GooglePlaces adapter. so i need to give him it back
    /*@Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            initAutoCompleteCity();
        }
    }*/

}