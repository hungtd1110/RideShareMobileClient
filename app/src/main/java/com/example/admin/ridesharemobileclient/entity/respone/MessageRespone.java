package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 5/4/2019.
 */

public class MessageRespone {
    private long userId;
    private String username;
    private String image;
    private String email;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
