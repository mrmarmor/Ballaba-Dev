package com.example.michaelkibenko.ballaba.Common;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by User on 14/06/2018.
 */

public class GuaranteeFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private final String TAG = GuaranteeFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //TODO here we need to send the message to server with the token like that:
        //sendRegistrationToServer(refreshedToken);

        new GuaranteeFcmService().showNotification(null, "try");
    }
}
