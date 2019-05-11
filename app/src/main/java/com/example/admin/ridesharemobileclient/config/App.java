package com.example.admin.ridesharemobileclient.config;

import android.annotation.SuppressLint;
import android.app.Application;

import com.example.admin.ridesharemobileclient.entity.User;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application mInstance;

    public static String sToken = "";
    public static User sUser = null;

    public static Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
