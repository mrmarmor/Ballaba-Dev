package com.example.michaelkibenko.ballaba.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityProfileBinding;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag.REQUEST_CODE_CAMERA;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag.REQUEST_CODE_GALLERY;

public class ProfileActivity extends BaseActivityWithActionBar implements View.OnClickListener{
    private final int REQUEST_CODE_CREDIT_CARD = 3;
    private final String TAG = ProfileActivity.class.getSimpleName();

    private ActivityProfileBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initViews();
        /*binder.profileActivityFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.profileActivityImageAvatar.setEnabled(true);
                binder.profileActivityDetailsFullName.setEnabled(true);
                binder.profileActivityDetailsDateOfBirth.setEnabled(true);
                binder.profileActivityDetailsProfession.setEnabled(true);
                binder.profileActivityStatusSpinner.setEnabled(true);
                binder.profileActivityChildrenSpinner.setEnabled(true);
                binder.profileActivityDetailsAddress.setEnabled(true);
                binder.profileActivityDetailsEmail.setEnabled(true);
                binder.profileActivityDetailsAbout.setEnabled(true);
                binder.profileActivityDetailsCreditCard.setEnabled(true);
            }
        });*/
    }

    private void initViews(){
        BallabaUser user = BallabaUserManager.getInstance().getUser();
        if (user != null) {
            Glide.with(this).load(user.getProfile_image()).into(binder.profileActivityImageAvatar);
            binder.profileActivityDetailsFullName.setText(user.getFirst_name() + " " + user.getLast_name());
            binder.profileActivityDetailsPhone.setText(user.getPhone());
            binder.profileActivityDetailsDateOfBirth.setText(user.getBirth_date());
            //binder.profileActivityDetailsProfession.setText(/*TODO get from server*/);
            binder.profileActivityStatusSpinner.setPrompt(null/*TODO get from server*/);
            binder.profileActivityChildrenSpinner.setPrompt(null/*TODO get from server*/);
            binder.profileActivityDetailsAddress.setText(user.getAddress());
            binder.profileActivityDetailsEmail.setText(user.getEmail());
            binder.profileActivityDetailsAbout.setText(user.getAbout());
            binder.profileActivityDetailsCreditCard.setText(/*TODO get from server last 4 digits*/"**** - **** - **** - " + null);
        }
        UiUtils.instance(true, this).initAutoCompleteCity(binder.profileActivityDetailsAddress);
    }

    private View sheetView;
    private BottomSheetDialog bottomSheetDialog;
    public void onClickProfileImage(View view) {
        //if (binder.profileActivityDetailsFullName.isEnabled()) {//=edit mode
            sheetView = getLayoutInflater().inflate(R.layout.take_pic_switch, null);
            sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
            sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        //}
    }

    public void onClickToCreditCard(View view) {
        startActivityForResult(new Intent(this, CreditCardActivity.class), REQUEST_CODE_CREDIT_CARD);
    }

    private boolean isEmailValid(String email) {
        boolean isValid = !TextUtils.isEmpty(email)
                && (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isValid) {
            binder.profileActivityDetailsEmail.setTextColor(getResources().getColor(R.color.red_error_phone));
            binder.profileActivityDetailsEmail.setText("invalid email address");
            ((ScrollView) binder.getRoot()).smoothScrollTo(0, binder.profileActivityDetailsEmail.getTop() - 30);
            binder.profileActivityDetailsEmail.requestFocus();
        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.addProp_profile_imageButton:
                onClickProfileImage();
                break;*/

            case R.id.takePic_button_camera:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                } else {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_CODE_CAMERA);
                    bottomSheetDialog.dismiss();
                }
                break;

            case R.id.takePic_button_gallery:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_CODE_GALLERY);
                    bottomSheetDialog.dismiss();
                }
                break;

            /*case R.id.profileActivity_button_next:
                onFinish(ConnectionsManager.newInstance(this));*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && intent != null) {
            Uri selectedImage = intent.getData();

            switch (requestCode) {
                case REQUEST_CODE_CAMERA: case REQUEST_CODE_GALLERY:
                    binder.profileActivityImageAvatar.setImageURI(selectedImage);
                    break;

                case REQUEST_CODE_CREDIT_CARD:
                    binder.profileActivityDetailsCreditCard.setText(generateHiddenCreditCardNumber(intent));
            }
        }
    }

    private String generateHiddenCreditCardNumber(Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null && !b.isEmpty()) {
            int creditCardNumberLength = b.getInt(CreditCardActivity.CREDIT_CARD_NUMBER_LENGTH);
            String creditCardLast4Digits = b.getString(CreditCardActivity.CREDIT_CARD_NUMBER_LAST_4_DIGITS);
            //String hiddenNumber = "";
            StringBuilder hiddenNumber = new StringBuilder(20);
            for (int i = 0 ; i < creditCardNumberLength - 4; i++) {
                hiddenNumber.append("*");
                if ((i+1) % 4 == 0)
                    hiddenNumber.append("-");
            }
            hiddenNumber.insert(hiddenNumber.length(), creditCardLast4Digits);

            return hiddenNumber.reverse().toString();
        }

        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sheetView.findViewById(R.id.takePic_button_camera).performClick();
            }
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sheetView.findViewById(R.id.takePic_button_gallery).performClick();
            }
        }
    }
}
