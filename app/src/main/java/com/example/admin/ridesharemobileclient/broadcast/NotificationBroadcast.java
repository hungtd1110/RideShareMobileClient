package com.example.admin.ridesharemobileclient.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.config.Chat;
import com.example.admin.ridesharemobileclient.utils.StompUtils;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

/**
 * Created by admin on 6/6/2019.
 */

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Chat.address);
        stompClient.connect();
        StompUtils.lifecycle(stompClient);

        stompClient.topic(Chat.notification.replace(Chat.placeholder, App.sUser.getUserId())).subscribe(stompMessage -> {
            try {
                JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
                showNotification(context, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showNotification(Context context, JSONObject jsonObject) {
        try {
            String content = jsonObject.getString("content");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.logo)
                    .setContentTitle("RideShare")
                    .setContentText(content)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
