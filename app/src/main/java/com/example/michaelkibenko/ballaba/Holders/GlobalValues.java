package com.example.michaelkibenko.ballaba.Holders;

import com.example.michaelkibenko.ballaba.BallabaApplication;

/**
 * Created by User on 05/03/2018.
 */

public @interface GlobalValues {
    String appName = BallabaApplication.getAppContext().getApplicationInfo().name;
}
