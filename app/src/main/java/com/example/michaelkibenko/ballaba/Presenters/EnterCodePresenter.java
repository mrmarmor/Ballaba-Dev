package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter implements TextWatcher {
    private Context context;
    private EnterCodeLayoutBinding binder;
    public String phoneNumber;
    private StringBuilder sbCode = new StringBuilder(4);
    private EditText[] editTexts;

    public EnterCodePresenter(Context context, EnterCodeLayoutBinding binding, String phoneNumber) {
        this.context = context;
        this.binder = binding;
        this.phoneNumber = phoneNumber;
        editTexts = new EditText[]{binder.enterCodeFirstLeftEditText, binder.enterCodeSecondLeftEditText, binder.enterCodeThirdLeftEditText, binder.enterCodeFourthLeftEditText};
        for (EditText et : editTexts)
            et.addTextChangedListener(this);
    }

    public void cancelButtonClicked(){
        ((EnterCodeActivity)context).onBackPressed();
    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        editTexts[sbCode.length()].clearFocus();
        editTexts[sbCode.length() + 1].requestFocus();
        editTexts[sbCode.length() + 1].setCursorVisible(true);
        sbCode.append(c);
        Log.e("tag", editTexts[sbCode.length()].getText()+"");
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}
}
