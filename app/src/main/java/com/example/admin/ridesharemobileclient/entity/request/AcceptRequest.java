package com.example.admin.ridesharemobileclient.entity.request;

/**
 * Created by admin on 5/7/2019.
 */

public class AcceptRequest {
    private String registerTripId;

    private String status;

    public AcceptRequest(String registerTripId, String status) {
        this.registerTripId = registerTripId;
        this.status = status;
    }

    public String getRegisterTripId() {
        return registerTripId;
    }

    public void setRegisterTripId(String registerTripId) {
        this.registerTripId = registerTripId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
