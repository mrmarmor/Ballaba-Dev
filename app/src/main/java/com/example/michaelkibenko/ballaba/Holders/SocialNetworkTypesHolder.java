package com.example.michaelkibenko.ballaba.Holders;

import android.support.annotation.IntDef;

import static com.example.michaelkibenko.ballaba.Holders.SocialNetworkTypesHolder.FACEBOOK;
import static com.example.michaelkibenko.ballaba.Holders.SocialNetworkTypesHolder.INSTAGRAM;
import static com.example.michaelkibenko.ballaba.Holders.SocialNetworkTypesHolder.LINKED_IN;
import static com.example.michaelkibenko.ballaba.Holders.SocialNetworkTypesHolder.TWITTER;

@IntDef({FACEBOOK, LINKED_IN, INSTAGRAM, TWITTER})
public @interface SocialNetworkTypesHolder {
    int FACEBOOK = 1;
    int LINKED_IN = 2;
    int INSTAGRAM = 3;
    int TWITTER = 4;
}
