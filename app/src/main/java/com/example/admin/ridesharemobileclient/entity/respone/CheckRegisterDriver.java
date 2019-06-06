package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/12/2019.
 */

public class CheckRegisterDriver {
    private String id;
    private String userId;
    private String driverId;
    private Boolean isSubmitter;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Boolean getSubmitter() {
        return isSubmitter;
    }

    public void setSubmitter(Boolean submitter) {
        isSubmitter = submitter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
