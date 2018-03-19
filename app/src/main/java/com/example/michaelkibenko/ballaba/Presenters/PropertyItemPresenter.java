package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;

import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;
import com.example.michaelkibenko.ballaba.Activities.SearchActivity;
import com.example.michaelkibenko.ballaba.Activities.SplashActivity;

/**
 * Created by User on 19/03/2018.
 */

public class PropertyItemPresenter extends BasePresenter{
    private Context context;

    public PropertyItemPresenter(Context context){
        this.context = context;
    }

    public void onPropertyClick(){
        //context.startActivity(new Intent(context, PropertyDetailsActivity.class));
    }
}
