package com.example.admin.ridesharemobileclient.entity;

import java.io.Serializable;

/**
 * Created by admin on 6/1/2019.
 */

public class RouteStep implements Serializable {
    private String longitude;
    private String latitude;

    public RouteStep() {
    }

    public RouteStep(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
