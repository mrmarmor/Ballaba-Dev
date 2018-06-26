package com.example.michaelkibenko.ballaba.Entities;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;

/**
 * Created by User on 04/03/2018.
 */

public class BallabaErrorResponse extends BallabaBaseEntity {
    public int statusCode;
    public String message;

    public BallabaErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    //TODO here we can build a parser for all messages given from server, so that we will display a hebrew message to user
}
