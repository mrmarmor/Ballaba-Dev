package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPhoneNumber;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.EnterCodeLayoutBinding;

import java.util.ArrayList;

import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.CODE_EXPIRED;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.INTERNAL_ERROR;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.NOT_A_VALID_PHONE_NUMBER;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.OK;
import static com.example.michaelkibenko.ballaba.Presenters.EnterCodePresenter.Flows.USER_IS_BLOCKED;

/**
 * Created by michaelkibenko on 22/02/2018.
 */

public class EnterCodePresenter extends BasePresenter implements TextWatcher, EditText.OnKeyListener, EditText.OnTouchListener {
    private static String TAG = EnterCodePresenter.class.getSimpleName();
    private final int SEND_AGAIN_DELAY = 60;

    @IntDef({OK, NOT_A_VALID_PHONE_NUMBER, CODE_EXPIRED, INTERNAL_ERROR, USER_IS_BLOCKED})
    public @interface Flows {
        int OK = 200;
        int NOT_A_VALID_PHONE_NUMBER = 400;
        int CODE_EXPIRED = 401;
        int USER_IS_BLOCKED = 403;
        int INTERNAL_ERROR = 500;
    }

    private static EnterCodePresenter instance;
    private Context context;
    private EnterCodeLayoutBinding binder;
    public BallabaPhoneNumber phoneNumber;//must be public so layout binding can see it
    private StringBuilder sbCode = new StringBuilder(4);
    private EditText[] editTexts;
    public BallabaConnectivityListener connectivityListener;
    private boolean wasConnectivityProblem;

    public EnterCodePresenter(Context context, EnterCodeLayoutBinding binding, String countryCode, String phoneNumber) {
        this.context = context;
        this.binder = binding;
        this.phoneNumber = new BallabaPhoneNumber(phoneNumber, countryCode);
        editTexts = new EditText[]{binder.enterCodeFirstLeftEditText, binder.enterCodeSecondLeftEditText
                , binder.enterCodeThirdLeftEditText, binder.enterCodeFourthLeftEditText};

        initEditTexts(editTexts);
        show1MinuteClock();
    }

    private void listenToNetworkChanges(){
        connectivityListener = new BallabaConnectivityListener() {
            @Override
            public void onConnectivityChanged(boolean is) {
                if (!is){
                    ((BaseActivity)context).showNetworkError(binder.getRoot());
                    wasConnectivityProblem = true;
                }else if(wasConnectivityProblem){
                    ((BaseActivity)context).hideNetworkError();
                    onCodeCompleted();
                }

            }
        };
        BallabaConnectivityAnnouncer.getInstance(context).register(connectivityListener);
    }

    public EnterCodePresenter getInstance() {
        if(instance == null){
            instance = new EnterCodePresenter(context, binder, phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber());
        }
        return instance;
    }


    private void initEditTexts(EditText[] editTexts) {
        for (EditText et : editTexts) {
            et.addTextChangedListener(this);//for numeric key press
            et.setOnKeyListener(this);//for backspace
            et.setOnTouchListener(this);//to prevent touchable mode
        }

        editTexts[0].requestFocus();

        //UiUtils.instance(true, context).showSoftKeyboard();
    }

    public void cancelButtonClicked() {
        ((EnterCodeActivity) context).onBackPressed();
    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        sbCode.append(c);
        int codeLength = sbCode.length();
        if (codeLength < 4 && codeLength > 0) {
            editTexts[codeLength - 1].clearFocus();
            editTexts[codeLength].requestFocus();
            editTexts[codeLength].setCursorVisible(true);
        } else if (codeLength >= 4){
            onCodeCompleted();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            int codeLength = sbCode.length() - 1;
            if (codeLength >= 0 && codeLength < 3) {
                sbCode.setLength(codeLength);//delete last char
                editTexts[codeLength + 1].clearFocus();
                editTexts[codeLength].requestFocus();
                editTexts[codeLength].setCursorVisible(true);
            }
        }

        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        UiUtils.instance(true, context).showSoftKeyboard();
        return true;
    }

    private void onCodeCompleted() {
        if(BallabaConnectivityAnnouncer.getInstance(context).isConnected()) {
            ConnectionsManager.getInstance(context).enterCode(phoneNumber.getFullPhoneNumber(),sbCode.toString() , new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Log.d(TAG, "enterCode success: "+entity);
                    if (entity instanceof BallabaUser) {
                        wasConnectivityProblem = false;

                        BallabaUser user = (BallabaUser)entity;
                        BallabaUserManager.getInstance().setUser(user);
                        SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.GLOBAL_TOKEN, user.getGlobal_token());
                        UiUtils.instance(true, context).hideSoftKeyboard(binder.getRoot());
                        binder.enterCodeProgress.setVisibility(View.VISIBLE);
                        ConnectionsManager.getInstance(context).getAttachmentsAddonsConfig(new BallabaResponseListener() {
                            @Override
                            public void resolve(BallabaBaseEntity entity) {
                                PropertyAttachmentsAddonsHolder.getInstance().parseAttachmentsAddonsResponse(((BallabaOkResponse)entity).body);
                                getProperties();
                            }

                            @Override
                            public void reject(BallabaBaseEntity entity) {
                                Log.e(TAG, entity.toString());
                            }
                        });
                    }
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    if (entity instanceof BallabaErrorResponse) {
                        Log.d(TAG, "enterCode rejected " + ((BallabaErrorResponse) entity).statusCode);
                        clearCode();

                        onFlowChanged(((BallabaErrorResponse) entity).statusCode);
                    }
                }
            });
        }else {
            ((BaseActivity)context).showNetworkError(binder.getRoot());
            listenToNetworkChanges();
        }
    }

    private void getProperties(){
        BallabaSearchPropertiesManager.getInstance(context).getRandomProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ArrayList<BallabaPropertyResult> properties = BallabaSearchPropertiesManager
                        .getInstance(context).parsePropertyResults(
                                ((BallabaOkResponse)entity).body);

                Log.d(TAG, "properties: " + properties+"");
                BallabaSearchPropertiesManager.getInstance(context).appendProperties(
                        properties, false);
                binder.enterCodeProgress.setVisibility(View.GONE);
                onFlowChanged(EnterPhoneNumberPresenter.Flows.OK);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                ((BaseActivity)context).getDefaultSnackBar(binder.getRoot(), context.getResources().getString(R.string.error_network_internal), true);
            }
        });
    }

    private void onFlowChanged(int statusCode) {
        switch (statusCode) {
            case Flows.OK:
                Intent intentToMainActivity = new Intent(context, MainActivity.class);
                //intentToMainActivity.putExtra(SOMETHING TO MOVE);
                intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentToMainActivity);
                ((EnterCodeActivity)context).finish();
                break;

            case Flows.INTERNAL_ERROR:
                Toast.makeText(context, context.getString(R.string.error_network_internal), Toast.LENGTH_LONG).show();
                break;

            case Flows.NOT_A_VALID_PHONE_NUMBER:
                Toast.makeText(context, context.getString(R.string.error_network_not_valid_phone_number), Toast.LENGTH_LONG).show();
                break;

            case Flows.CODE_EXPIRED:
                binder.enterCodeErrorTextView.setVisibility(View.VISIBLE);
                Toast.makeText(context, context.getString(R.string.error_network_code_has_expired), Toast.LENGTH_LONG).show();
                break;

            case Flows.USER_IS_BLOCKED:
                Toast.makeText(context, context.getString(R.string.error_network_user_blocked), Toast.LENGTH_LONG).show();
                break;

            default:
                Toast.makeText(context, context.getString(R.string.error_network_default), Toast.LENGTH_LONG).show();
        }
    }

    //This method fill in code editTexts automatically by reading code from received sms.
    //We need to delay fill in by 3 seconds, to let our app get code from server, so it could be compared to each other.
    public void autoFillCode(final String code){
        Log.d(TAG, "code: "+code);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binder.enterCodeFirstLeftEditText.setText(code.charAt(0)+"");
                binder.enterCodeSecondLeftEditText.setText(code.charAt(1)+"");
                binder.enterCodeThirdLeftEditText.setText(code.charAt(2)+"");
                binder.enterCodeFourthLeftEditText.setText(code.charAt(3)+"");
            }
        }, 3000);
    }

    private void clearCode(){
        for (EditText et : editTexts){
            et.setText("");
        }
        sbCode.setLength(0);
        editTexts[0].requestFocus();
    }

    //TODO this method exists in EnterPhoneNumberPresenter exactly(exclude onFlowChanged()). So, consider make it generic once
    public void onSendAgainButtonClick(){
        UiUtils.instance(true, context).buttonChanger(binder.enterCodeSendAgainButton, false);
        show1MinuteClock();

        //TODO move next line method to basePresenter
        UiUtils.instance(true, context).hideSoftKeyboard(((Activity)context).getWindow().getDecorView());

        ((BaseActivity)context).getDefaultSnackBar(binder.getRoot(), context.getString(R.string.enter_code_send_again_snack_bar_text), true).show();

        ConnectionsManager.getInstance(context).loginWithPhoneNumber(phoneNumber.getFullPhoneNumber(), new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    Log.d(TAG, "loginWithPhoneNumber");
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                if(entity instanceof BallabaErrorResponse){
                    //TODO replace next line with snackbar
                    //binder.enterPhoneNumberTextErrorAnswer.setVisibility(View.VISIBLE);
                    Log.d(TAG, "loginWithPhoneNumber rejected "+((BallabaErrorResponse)entity).statusCode);
                }
            }
        });
    }

    private void show1MinuteClock(){
        new CountDownTimer(SEND_AGAIN_DELAY * 1000 , 1000) { //60000 milli seconds is total time, 1000 milli seconds is time interval
            public void onTick(long millisUntilFinished) {
                binder.enterCodeSendAgainButton.setText(String.format("%2d:00", millisUntilFinished/1000));

            }
            public void onFinish() {
                UiUtils.instance(true, context).buttonChanger(binder.enterCodeSendAgainButton, true);
                binder.enterCodeSendAgainButton.setText(context.getString(R.string.enter_code_send_again_button_text));
            }
        }.start();
    }

}
