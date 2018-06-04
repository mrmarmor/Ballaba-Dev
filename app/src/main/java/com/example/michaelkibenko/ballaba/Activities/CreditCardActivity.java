package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityCreditCardBinding;

import static com.example.michaelkibenko.ballaba.Activities.CreditCardActivity.CREDIT_CARD_ICON.AMERICAN_EXPRESS;
import static com.example.michaelkibenko.ballaba.Activities.CreditCardActivity.CREDIT_CARD_ICON.DINERS;
import static com.example.michaelkibenko.ballaba.Activities.CreditCardActivity.CREDIT_CARD_ICON.ISRACARD;
import static com.example.michaelkibenko.ballaba.Activities.CreditCardActivity.CREDIT_CARD_ICON.MASTERCARD;
import static com.example.michaelkibenko.ballaba.Activities.CreditCardActivity.CREDIT_CARD_ICON.VISA;
import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.AFTER_20;
import static com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.FIRST_20;
import static java.sql.Types.NULL;

/**
 * Created by User on 03/06/2018.
 */

public class CreditCardActivity extends BaseActivityWithActionBar implements View.OnFocusChangeListener{
    private static final String TAG = CreditCardActivity.class.getSimpleName();

    ActivityCreditCardBinding binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_credit_card);

        binder.creditCardCardNumber.setOnFocusChangeListener(this);
    }

    public void onClickInfo(View view){
        final BallabaDialogBuilder infoBuilder = new BallabaDialogBuilder(this, R.layout.dialog_regular);
        final AlertDialog infoDialog = infoBuilder.create();

        infoDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        infoDialog.getWindow().setGravity(Gravity.RIGHT);
        infoBuilder.setContentInView("מה זה CVV?", "3 המספרים בגב הכרטיס", null);
        infoBuilder.setButtonsInView("הבנתי"
                , null
                , R.color.colorPrimary, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        infoDialog.dismiss();
                    }
                }, null);

        infoDialog.show();
    }

    public void onClickNext(View view){
        Toast.makeText(this, "go to next page", Toast.LENGTH_SHORT).show();
    }

    @IntDef({ISRACARD, MASTERCARD, VISA, DINERS, AMERICAN_EXPRESS, NULL})
    @interface CREDIT_CARD_ICON {
        int ISRACARD = R.drawable.bathtub_blue_36;
        int MASTERCARD = R.drawable.bed_blue_36;
        int VISA = R.drawable.calender_blue_24_copy;
        int DINERS = R.drawable.date_blue_36;
        int AMERICAN_EXPRESS = R.drawable.exclusive;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String s = binder.creditCardCardNumber.getText().toString().trim();
            int cardType;
            if (s.length() == 8 || s.length() == 9){
                cardType = ISRACARD;
            } else if (s.length() == 16 && (s.startsWith("51") || s.startsWith("52") || s.startsWith("53") || s.startsWith("54") || s.startsWith("55"))){
                cardType = MASTERCARD;
            } else if (s.length() == 16 && s.startsWith("4")) {
                cardType = VISA;
            } else if (s.length() == 14 && (s.startsWith("30") || s.startsWith("36") || s.startsWith("38"))) {
                cardType = DINERS;
            } else if (s.length() == 15 && (s.startsWith("27") || s.startsWith("37"))) {
                cardType = AMERICAN_EXPRESS;
            } else {
                cardType = NULL;
            }

            //TESTING:
            //Log.d(TAG, "credit number: " + s + " ,card type: " + cardType);

            binder.creditCardCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, cardType, 0);
        }
    }
}
