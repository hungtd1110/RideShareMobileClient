package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/10/2019.
 */

public class UserNotAccept {
    private UserRespone user;

    public UserNotAccept(UserRespone user) {
        this.user = user;
    }

    public UserRespone getUser() {
        return user;
    }

    public void setUser(UserRespone user) {
        this.user = user;
    }
}
