package com.example.michaelkibenko.ballaba.Managers;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;

import org.json.JSONObject;

/**
 * Created by michaelkibenko on 04/03/2018.
 */

public interface BallabaResponseListener {
    void resolve(BallabaBaseEntity entity);

    void reject(BallabaBaseEntity entity);
}
