package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityGuaranteeAgreementBinding;

public class GuaranteeAgreementActivity extends BaseActivityWithActionBar {
    private final String TAG = GuaranteeAgreementActivity.class.getSimpleName();
    private ActivityGuaranteeAgreementBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_guarantee_agreement);

        showLast4Digits();
    }

    private void showLast4Digits() {
        BallabaUser user = BallabaUserManager.getInstance().getUser();
        if (user != null && user.getLast_4_digits() != null) {
            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putInt(CreditCardActivity.CREDIT_CARD_NUMBER_LENGTH, 16);//TODO get length from server
            b.putString(CreditCardActivity.CREDIT_CARD_NUMBER_LAST_4_DIGITS, user.getLast_4_digits());
            intent.putExtras(b);
            binder.guaranteeAgreementCreditCardNumber.setText(new ProfileActivity().generateHiddenCreditCardNumber(intent));
        }
    }

    public void goToCreditCardActivity(View view) {
        startActivity(new Intent(this, CreditCardActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLast4Digits();
    }
}
