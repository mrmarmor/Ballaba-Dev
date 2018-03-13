package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;
import com.example.michaelkibenko.ballaba.Activities.SearchActivity;
import com.example.michaelkibenko.ballaba.Activities.SplashActivity;

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
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    public void onFlowStartedClicked(){
        context.startActivity(new Intent(context, SplashActivity.class));
    }
}
