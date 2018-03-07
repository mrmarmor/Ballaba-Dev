package com.example.michaelkibenko.ballaba.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 06/03/2018.
 */

public class GeneralUtils {
    public static Map<String, String> getParams(final String[] KEYS, final String[] VALUES) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < KEYS.length; i++)
            params.put(KEYS[i], VALUES[i]);

        return params;
    }
}
