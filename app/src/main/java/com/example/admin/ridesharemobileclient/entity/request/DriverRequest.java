package com.example.admin.ridesharemobileclient.entity.request;

/**
 * Created by admin on 5/7/2019.
 */

public class DriverRequest {
    private String driverId;
    private String time;
    private String numberSeat;
    private String price;
    private String note;
    private String routeStep;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRouteStep() {
        return routeStep;
    }

    public void setRouteStep(String routeStep) {
        this.routeStep = routeStep;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumberSeat() {
        return numberSeat;
    }

    public void setNumberSeat(String numberSeat) {
        this.numberSeat = numberSeat;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
