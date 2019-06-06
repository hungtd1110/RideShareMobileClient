package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/12/2019.
 */

public class CheckRegisterHitchhiker {
    private String id;
    private String userId;
    private String hitchhikerId;
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

    public String getHitchhikerId() {
        return hitchhikerId;
    }

    public void setHitchhikerId(String hitchhikerId) {
        this.hitchhikerId = hitchhikerId;
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
