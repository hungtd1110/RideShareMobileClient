package com.example.admin.ridesharemobileclient.config;

import static com.example.admin.ridesharemobileclient.config.Const.IP;

/**
 * Created by admin on 5/5/2019.
 */

public class Chat {
    public static final String placeholder = "placeholder";

    public static final String address = "ws://" + IP + ":8080/carpool/websocket?authorization=" + App.sToken;

    public static final String send_message = "/user/" + placeholder + "/";

    public static final String recive_message = "/socket/message/" + placeholder;

    public static final String notification = "/socket/notification/" + placeholder;
}
