package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
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
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityProfileBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivityWithActionBar {
    private final String TAG = ProfileActivity.class.getSimpleName();

    private ActivityProfileBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initViews();
        binder.profileActivityFAB.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    private void initViews(){
        BallabaUser user = BallabaUserManager.getInstance().getUser();
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

        UiUtils.instance(true, this).initAutoCompleteCity(binder.profileActivityDetailsAddress);
    }

    private View sheetView;
    private BottomSheetDialog bottomSheetDialog;
    public void onClickProfileImage(View view) {
        if (binder.profileActivityDetailsFullName.isEnabled()) {//=edit mode
            sheetView = getLayoutInflater().inflate(R.layout.take_pic_switch, null);
            //sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
            //sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        }
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
}
