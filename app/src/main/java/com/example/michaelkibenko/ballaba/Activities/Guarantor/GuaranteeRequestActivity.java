package com.example.michaelkibenko.ballaba.Activities.Guarantor;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivityWithActionBar;
import com.example.michaelkibenko.ballaba.Common.GuaranteeFcmService;
import com.example.michaelkibenko.ballaba.Common.GuaranteeFirebaseInstanceIdService;
import com.example.michaelkibenko.ballaba.Common.GuaranteeReceiver;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityGuaranteeRequestBinding;
import com.google.firebase.iid.FirebaseInstanceId;

public class GuaranteeRequestActivity extends BaseActivityWithActionBar {
    private ActivityGuaranteeRequestBinding binder;

    private GuaranteeReceiver guaranteeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_guarantee_request);

        initViews();
    }

    private void initViews() {
        //TODO guarantor device should fetch tenant user name and profile image from server or from notification.
        //TODO for now, static data will be set.
        Drawable tenantProfileImage = getResources().getDrawable(R.drawable.add_user_b_lue_60);
        String tenantFullName = "צחי קורן";
        String tenantExplanation = "אחי, תעזור לחבר בקטנה...";
        String guaranteePrice = "₪" + StringUtils.getInstance(true).formattedNumberWithComma("30000");

        binder.guaranteeRequestTenantProfileImage.setImageDrawable(tenantProfileImage);
        binder.guaranteeRequestTenantNameTextView.setText(tenantFullName);
        binder.guaranteeRequestTenantExplanation.setText(tenantExplanation);
        binder.guaranteeRequestGuaranteePrice.setText(guaranteePrice);
    }

    public void onClickConfirm(View view) {
        //Toast.makeText(this, "confirmed", Toast.LENGTH_SHORT).show();
        guaranteeReceiver = new GuaranteeReceiver();
        registerReceiver(guaranteeReceiver, new IntentFilter("ACTION"));
        startService(new Intent(this, GuaranteeFcmService.class));
        startService(new Intent(this, GuaranteeFirebaseInstanceIdService.class));
        Log.d("GuaranteeRequest", FirebaseInstanceId.getInstance().getToken());
        new GuaranteeReceiver().sendNotification(FirebaseInstanceId.getInstance().getToken(), "user name", "my message");
        //new GuaranteeFcmService().showNotification(this, "try2");
        finish();
    }

    public void onClickDecline(View view) {
        Toast.makeText(this, "declined", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (guaranteeReceiver != null)
            unregisterReceiver(guaranteeReceiver);
    }
}
