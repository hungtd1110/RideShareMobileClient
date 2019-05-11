package com.example.admin.ridesharemobileclient.entity;

/**
 * Created by admin on 4/21/2019.
 */

public class UserDriver {
    private String id;
    private User user;
    private Driver driver;
    private String isSubmit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(String isSubmit) {
        this.isSubmit = isSubmit;
    }
}
