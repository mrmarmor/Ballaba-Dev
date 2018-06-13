package com.example.michaelkibenko.ballaba.Activities.Guarantor;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivityWithActionBar;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityGuaranteeRequestBinding;

public class GuaranteeRequestActivity extends BaseActivityWithActionBar {
    private ActivityGuaranteeRequestBinding binder;

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
        Toast.makeText(this, "confirmed", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onClickDecline(View view) {
        Toast.makeText(this, "declined", Toast.LENGTH_SHORT).show();
        finish();
    }
}
