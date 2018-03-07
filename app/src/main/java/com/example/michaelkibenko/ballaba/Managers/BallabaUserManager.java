package com.example.michaelkibenko.ballaba.Managers;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;

/**
 * Created by User on 07/03/2018.
 */

public class BallabaUserManager {
    private static BallabaUserManager instance = null;

    private BallabaUserManager() {
    }

    public static BallabaUserManager getInstance() {
        if(instance == null) {
            instance = new BallabaUserManager();
        }
        return instance;
    }

    public BallabaUser getUser(){ return new BallabaUser(); }
}
