package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/10/2019.
 */

public class HitchhikerUserNotAccept {
    private HitchhikerUserRespone user;

    public HitchhikerUserNotAccept(HitchhikerUserRespone user) {
        this.user = user;
    }

    public HitchhikerUserRespone getUser() {
        return user;
    }

    public void setUser(HitchhikerUserRespone user) {
        this.user = user;
    }
}
