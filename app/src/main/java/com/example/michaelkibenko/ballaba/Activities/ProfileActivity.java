package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivityWithActionBar {
    private final String TAG = ProfileActivity.class.getSimpleName();

    private ActivityProfileBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initViews();
    }

    private void initViews(){
        BallabaUser user = BallabaUserManager.getInstance().getUser();
        binder.profileActivityDetailsFullName.setText(user.getFirst_name() + " " + user.getLast_name());
        binder.profileActivityDetailsPhone.setText(user.getPhone());
        binder.profileActivityDetailsDateOfBirth.setText(user.getBirth_date());
        binder.profileActivityDetailsProfession.setText(null/*TODO get from server*/);
        binder.profileActivityStatusSpinner.setPrompt(null/*TODO get from server*/);
        binder.profileActivityChildrenSpinner.setPrompt(null/*TODO get from server*/);
        binder.profileActivityDetailsAddress.setText(user.getAddress());
        binder.profileActivityDetailsEmail.setText(user.getEmail());
        binder.profileActivityDetailsAbout.setText(user.getAbout());
        binder.profileActivityDetailsCreditCard.setText(/*TODO get from server last 4 digits*/"**** - **** - **** - " + null);
    }
}
