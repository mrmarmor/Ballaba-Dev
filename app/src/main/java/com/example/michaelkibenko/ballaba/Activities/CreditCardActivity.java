package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityCreditCardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

    @IntDef({ISRACARD, MASTERCARD, VISA, DINERS, AMERICAN_EXPRESS, NULL})
    @interface CREDIT_CARD_ICON {
        int ISRACARD = R.drawable.bathtub_blue_36;
        int MASTERCARD = R.drawable.bed_blue_36;
        int VISA = R.drawable.calender_blue_24_copy;
        int DINERS = R.drawable.date_blue_36;
        int AMERICAN_EXPRESS = R.drawable.exclusive;
    }

    private ActivityCreditCardBinding binder;
    private @CREDIT_CARD_ICON int cardType;

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
        onFinish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            cardType = getCardType(binder.creditCardCardNumber.getText().toString().trim());

            //TESTING:
            //Log.d(TAG, "credit number: " + s + " ,card type: " + cardType);

            binder.creditCardCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, cardType, 0);
        }
    }

    private @CREDIT_CARD_ICON int getCardType(String s) {
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

        return cardType;
    }

    private void onFinish() {
        if (isValidCardType() && isValidCVV() && isValidUserName() && isValidIdNumber() && isValidEmail()){
            JSONObject data = getCreditCardData(new JSONObject());
            ConnectionsManager.newInstance(this).uploadCreditCard(data, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Toast.makeText(CreditCardActivity.this, "go to next page", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    getDefaultSnackBar(binder.getRoot(), ((BallabaErrorResponse)entity).message, false).show();
                }
            });
        }
    }

    private boolean isValidCardType() {
        if (getCardType(binder.creditCardCardNumber.getText().toString()) == NULL) {
            onError(binder.creditCardCardNumber, "מספר כרטיס אשראי לא תקין");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidCVV() {
        if (binder.creditCardCardCVV.getText().toString().length() != 3) {
            onError(binder.creditCardCardCVV, "מספר קוד אבטחה אינו תקין");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidUserName() {
        if (binder.creditCardPersonName.getText().toString().equals("")) {
            onError(binder.creditCardPersonName, "שם משתמש חסר");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidIdNumber() {
        int idNumberLength = binder.creditCardPersonID.getText().toString().length();
        if (idNumberLength > 9 || idNumberLength < 8) {
            onError(binder.creditCardPersonID, "מספר זהות לא תקין");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail() {
        String email = binder.creditCardUserMail.getText().toString();
        boolean isValid = !TextUtils.isEmpty(email) && (Patterns.EMAIL_ADDRESS.matcher(email).matches());

        if (!isValid)
            onError(binder.creditCardUserMail, "כתובת אימייל לא נכונה");

        return isValid;
    }

    private void onError(EditText editText, String errorMessage) {
        editText.setTextColor(getResources().getColor(R.color.red_error_phone, getTheme()));
        editText.setText(errorMessage);
        ((ScrollView)binder.getRoot()).smoothScrollTo(0, editText.getTop() - 30);
        editText.requestFocus();
    }

    private JSONObject getCreditCardData(JSONObject jsonObject) {
        try {
            for (int i = binder.creditCardEditTextsRoot.getChildCount() - 1; i >= 0; i--) {
                View v = binder.creditCardEditTextsRoot.getChildAt(i);
                if (v instanceof EditText) {
                    jsonObject.put(v.getTag() + "", ((EditText) v).getText() + "");
                }
            }

            Log.d(TAG, binder.creditCardCardYear.getSelectedItem()+"");
            Log.d(TAG, binder.creditCardCardMonth.getSelectedItem()+"");
            jsonObject.put("cvv", binder.creditCardCardCVV.getText());
            jsonObject.put("expiration_year", binder.creditCardCardYear.getSelectedItem().toString().substring(2, 4));
            jsonObject.put("expiration_month", binder.creditCardCardMonth.getSelectedItem().toString());

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return jsonObject;
    }

}