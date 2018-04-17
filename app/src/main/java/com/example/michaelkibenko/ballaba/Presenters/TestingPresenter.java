package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SplashActivity;
import com.example.michaelkibenko.ballaba.Activities.Underwriting.UnderwritingWelcomeActivity;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class TestingPresenter extends BasePresenter {

    private Context context;
    private Intent enterPhoneNumberIntent, enterCodeIntent;


    public TestingPresenter(Context context){
        this.context = context;
        enterPhoneNumberIntent = new Intent(this.context, EnterPhoneNumberActivity.class);
        enterCodeIntent = new Intent(this.context, EnterCodeActivity.class);
    }

    public void gotoEnterCodeIntent(){
        context.startActivity(enterCodeIntent);
    }

    public void gotoEnterPhoneNumberIntent(){
        context.startActivity(enterPhoneNumberIntent);
    }

    public void onSearchClicked(){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public void onFlowStartedClicked(){
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    public void onUnderwritingClicked(){
        context.startActivity(new Intent(context, UnderwritingWelcomeActivity.class));
    }
}
