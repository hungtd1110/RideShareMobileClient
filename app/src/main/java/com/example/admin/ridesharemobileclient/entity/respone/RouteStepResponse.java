package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 6/1/2019.
 */

public class RouteStepResponse {
    private String id;
    private String userDriverId;
    private String steps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserDriverId() {
        return userDriverId;
    }

    public void setUserDriverId(String userDriverId) {
        this.userDriverId = userDriverId;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
