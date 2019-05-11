package com.example.admin.ridesharemobileclient.entity.respone;

import com.example.admin.ridesharemobileclient.entity.User;

/**
 * Created by admin on 5/3/2019.
 */

public class LoginRespone {
    private String token;
    private User userInformation;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(User userInformation) {
        this.userInformation = userInformation;
    }
}
