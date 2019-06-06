package com.example.admin.ridesharemobileclient.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

public class NotificationService extends Service {
    private BroadcastReceiver broadcast;
//    private static NotificationCompat.Builder builder;
//    private static NotificationManagerCompat notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        builder = new NotificationCompat.Builder(this);
//        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Chat.address);
//        stompClient.connect();
//        StompUtils.lifecycle(stompClient);
//
//        stompClient.topic(Chat.notification.replace(Chat.placeholder, App.sUser.getUserId())).subscribe(stompMessage -> {
//            try {
//                JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
//                showNotification(jsonObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Chat.address);
                stompClient.connect();
                StompUtils.lifecycle(stompClient);

                stompClient.topic(Chat.notification.replace(Chat.placeholder, App.sUser.getUserId())).subscribe(stompMessage -> {
                    try {
                        JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
                        showNotification(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

//        broadcast = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Chat.address);
//                stompClient.connect();
//                StompUtils.lifecycle(stompClient);
//
//                stompClient.topic(Chat.notification.replace(Chat.placeholder, App.sUser.getUserId())).subscribe(stompMessage -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
//                        showNotification(jsonObject);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        };
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("notification");
//        registerReceiver(broadcast, filter);
////
//        Intent intentBroadcast = new Intent();
//        intentBroadcast.setAction("notification");
//        sendBroadcast(intentBroadcast);

        return START_STICKY;
    }

    private void showNotification(JSONObject jsonObject) {
        try {
            String content = jsonObject.getString("content");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.logo)
                    .setContentTitle("RideShare")
                    .setContentText(content)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
//        unregisterReceiver(broadcast);
    }
}
