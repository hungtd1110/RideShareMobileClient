package com.example.admin.ridesharemobileclient.entity;

/**
 * Created by admin on 5/5/2019.
 */

public class Message {
    private long userIdSend;
    private long userIdReceive;
    private String content;
    private String createdAt;
    private String image;

    public long getUserIdSend() {
        return userIdSend;
    }

    public void setUserIdSend(long userIdSend) {
        this.userIdSend = userIdSend;
    }

    public long getUserIdReceive() {
        return userIdReceive;
    }

    public void setUserIdReceive(long userIdReceive) {
        this.userIdReceive = userIdReceive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
