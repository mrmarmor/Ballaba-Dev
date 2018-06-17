package com.example.michaelkibenko.ballaba.Activities.Guarantor;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.UserManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.BaseActivityWithActionBar;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.EnterPhoneNumberPresenter;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.GeneralUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityGuarantorDeclinedBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertiesRecyclerBinding;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;

public class GuarantorDeclinedActivity extends BaseActivityWithActionBar implements TextWatcher {
    private final String TAG = GuarantorDeclinedActivity.class.getSimpleName();

    private PropertiesRecyclerAdapter rvAdapter;
    private ArrayList<BallabaPropertyResult> properties = new ArrayList<>();

    private ActivityGuarantorDeclinedBinding binder;
    private UiUtils uiUtils = UiUtils.instance(true, this);
    private BallabaUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_guarantor_declined);

        initViews();
        initRecycler();
    }

    private void initViews() {
        user = BallabaUserManager.getInstance().getUser();
        binder.guarantorDeclinedUserName.setText(String.format("%s %s,", user.getFirst_name(), user.getLast_name()));

        binder.guarantorDeclinedPhone.addTextChangedListener(this);
        binder.guarantorDeclinedPhone.requestFocus();
    }

    private void initRecycler() {
        properties.add(BallabaSearchPropertiesManager.getInstance(this).getResultsById("1"));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvAdapter = new PropertiesRecyclerAdapter(this, getSupportFragmentManager(), properties, false);
        binder.guarantorDeclinedProperty.setLayoutManager(manager);
        binder.guarantorDeclinedProperty.setAdapter(rvAdapter);
        binder.guarantorDeclinedProperty.setClickable(false);
        binder.guarantorDeclinedProperty.setEnabled(false);
        binder.guarantorDeclinedProperty.setOnClickListener(null);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String phoneNumber = charSequence.toString().trim();
        PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
        boolean isValid = false;
        try {
            Phonenumber.PhoneNumber targetPN = pnu.parse(phoneNumber, "IL");
            isValid = pnu.isValidNumber(targetPN);
        } catch (NumberParseException ex) {
            ex.printStackTrace();
        }
        if (isValid && phoneNumber.length() > 9)
            uiUtils.buttonChanger(binder.guarantorDeclinedNextButton, true);
    }
    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void afterTextChanged(Editable editable) {}


    public void sendPhoneNumber(View view) {
        if (BallabaConnectivityAnnouncer.getInstance(this).isConnected()) {

            getDefaultSnackBar(binder.getRoot(), getString(R.string.enter_phone_number_snackBar_text), true);

            circleProgressBarChanger(true);
            uiUtils.buttonChanger(binder.guarantorDeclinedNextButton, false);

            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(Intent.ACTION_SEND_MULTIPLE), 0));

            SmsManager smsManager = SmsManager.getDefault();
            String fullNumber = binder.guarantorDeclinedPhone.getText().toString().trim();
            String deepLink = "http://yb.net/dvfhe45";
            String message = String.format("%s %s מבקש שתערוב לו\n\nהכנס לקישור ועזור ל%s לקבל את דירת חלומותיו\n%s"
                    , user.getFirst_name(), user.getLast_name(), user.getFirst_name(), deepLink);

            ArrayList<String> dividedMessage = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(fullNumber, null, dividedMessage, sentIntents, null);

            //TODO send push notification to via server and add listener

            circleProgressBarChanger(false);
            uiUtils.buttonChanger(binder.guarantorDeclinedNextButton, true);

        } else {
            showNetworkError(binder.getRoot());
        }
    }

    private void circleProgressBarChanger(boolean isShow) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)binder.guarantorDeclinedNextButton.getLayoutParams();

        if (isShow) {
            params.width = (int)getResources().getDimension(R.dimen.enterPhoneNumber_progressButton_progress_width);
            binder.guarantorDeclinedNextButton.setLayoutParams(params);
            binder.guarantorDeclinedNextButton.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            binder.guarantorDeclinedNextButton.setPaddingRelative((int) getResources().getDimension(R.dimen.small_margin), 0, 0, 0);

            binder.guarantorDeclinedNextButtonProgress.setVisibility(View.VISIBLE);
            binder.guarantorDeclinedNextButtonProgress.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

        } else {
            params.width = (int)getResources().getDimension(R.dimen.enterPhoneNumber_progressButton_no_progress_width);
            binder.guarantorDeclinedNextButton.setLayoutParams(params);
            binder.guarantorDeclinedNextButton.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            binder.guarantorDeclinedNextButtonProgress.setVisibility(View.GONE);
        }
    }
}
