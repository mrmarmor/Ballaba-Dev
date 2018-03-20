package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;

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
