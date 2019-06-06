package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/10/2019.
 */

public class DriverUserNotAccept {
    private DriverUserRespone user;

    public DriverUserNotAccept(DriverUserRespone user) {
        this.user = user;
    }

    public DriverUserRespone getUser() {
        return user;
    }

    public void setUser(DriverUserRespone user) {
        this.user = user;
    }
}
