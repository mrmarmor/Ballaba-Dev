package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;

import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class PreAuthPresenter extends BasePresenter {

    private Context context;
    private Intent enterPhoneNumberIntent;


    public PreAuthPresenter(Context context){
        this.context = context;
        enterPhoneNumberIntent = new Intent(this.context, EnterPhoneNumberActivity.class);
    }

    public void hasAccountPressed(){
        context.startActivity(enterPhoneNumberIntent);
    }

    public void firstTimePressed(){
        context.startActivity(enterPhoneNumberIntent);
    }
}
