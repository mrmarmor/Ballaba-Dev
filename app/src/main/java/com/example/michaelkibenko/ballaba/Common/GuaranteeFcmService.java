package com.example.michaelkibenko.ballaba.Common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.michaelkibenko.ballaba.Activities.CreditCardActivity;
import com.example.michaelkibenko.ballaba.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by User on 14/06/2018.
 */

public class GuaranteeFcmService extends FirebaseMessagingService {
    private final String TAG = GuaranteeFcmService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Instance ID: " + FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        showNotification(null/*fixIt!!*/, remoteMessage.getData().get("message"));//or "title"
/*fixIt!!*/
    }

    public void showNotification(Context context, String message) {
        Intent intent = new Intent(Intent.CATEGORY_INFO, null, context, CreditCardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String title = context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_title);
            /*message = getString(R.string.invitationMessage, party.getName(), party.getLocation()
                    , party.getDate(), party.getTime());*/
        //dataJson.put("body","Hi, "+party.getName()+" invites you to his birthday party in "

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.check_blue_24)//TODO change icon
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
