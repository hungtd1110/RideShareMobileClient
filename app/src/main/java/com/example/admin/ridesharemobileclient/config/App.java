package com.example.admin.ridesharemobileclient.config;

import android.annotation.SuppressLint;
import android.app.Application;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application mInstance;

    public static String sToken = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTQ2MTMxNTksInVzZXJuYW1lIjoidXNlciJ9.1NcBgnH8my1VSAKR2REAIKMenPuXDPhGXK4VtCT6h_A";
//    public static String sToken = "";

    public static Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
