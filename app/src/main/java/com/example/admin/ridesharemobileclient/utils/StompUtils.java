package com.example.admin.ridesharemobileclient.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import ua.naiksoftware.stomp.StompClient;

public class StompUtils {
    private static final String TAG = "StompUtils";

    @SuppressLint("CheckResult")
    public static void lifecycle(StompClient stompClient) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i(TAG, "Connect: stomp connection opened!");
                    break;
                case ERROR:
                    Log.e(TAG, "Connect: Error occured!", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.i(TAG, "Connect: stomp connection closed!");
                    break;
            }
        });
    }
}
