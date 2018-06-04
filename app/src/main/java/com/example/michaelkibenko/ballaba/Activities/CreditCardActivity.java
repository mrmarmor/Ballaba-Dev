package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityCreditCardBinding;

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {

        }
    }
}
