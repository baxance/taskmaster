package com.baxance.taskmaster;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        Log.i("messageReceipt", remoteMessage.toString());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationCompat.CATEGORY_REMINDER)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getFrom())
                .setContentText(remoteMessage.getSentTime() + "")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }

}
