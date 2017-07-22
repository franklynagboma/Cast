package com.androidtecknowlogy.tym.cast.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by AGBOMA franklyn on 7/20/17.
 */

public class MessagingService extends FirebaseMessagingService {

    private final String LOG_TAG = MessagingService.class.getSimpleName();

    public MessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        String notificationBody = null;
        if(remoteMessage.getNotification() != null) {
            logger("From: " + remoteMessage.getFrom());
            notificationBody = remoteMessage.getNotification().getBody();
            logger("Notification Message Body: " + notificationBody);
        }
        //Generate Notification
        createEventsNotification(notificationBody);
    }

    private void logger(String mess){
        Log.d(LOG_TAG, mess);
    }

    private void createEventsNotification(String message) {
        //Perform message broadcast
        Intent intent = new Intent(this, CastActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Notification tone
        Uri notificationSounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Notification builder
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Tym Frontiers,new event.")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(notificationSounduri)
                .setContentIntent(pendingIntent);
        //bind notification manager to system service
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //pop notification on device
        notificationManager.notify(0, notification.build());
    }
}
