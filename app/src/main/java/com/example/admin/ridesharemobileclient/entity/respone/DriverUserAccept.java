package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/10/2019.
 */

public class DriverUserAccept {
    private DriverUserRespone user;

    public DriverUserAccept(DriverUserRespone user) {
        this.user = user;
    }

    public DriverUserRespone getUser() {
        return user;
    }

    public void setUser(DriverUserRespone user) {
        this.user = user;
    }
}
