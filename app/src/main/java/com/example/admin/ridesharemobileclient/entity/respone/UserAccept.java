package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/10/2019.
 */

public class UserAccept {
    private UserRespone user;

    public UserAccept(UserRespone user) {
        this.user = user;
    }

    public UserRespone getUser() {
        return user;
    }

    public void setUser(UserRespone user) {
        this.user = user;
    }
}
