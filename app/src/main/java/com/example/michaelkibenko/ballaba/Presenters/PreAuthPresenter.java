package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class PreAuthPresenter extends BasePresenter {

    private Context context;

    public PreAuthPresenter(Context context){
        this.context = context;
    }

    public void hasAccountPressed(){
        Toast.makeText(this.context, "hasAccountPressed", Toast.LENGTH_LONG).show();
    }

    public void firstTimePressed(){
        Toast.makeText(this.context, "firstTimePressed", Toast.LENGTH_LONG).show();
    }
}
