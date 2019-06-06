package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 6/6/2019.
 */

public class Notification {
    private String id;
    private String userId;
    private String tripId;
    private String typeTrip;
    private String content;
    private String status;
    private String createdAt;

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

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTypeTrip() {
        return typeTrip;
    }

    public void setTypeTrip(String typeTrip) {
        this.typeTrip = typeTrip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
