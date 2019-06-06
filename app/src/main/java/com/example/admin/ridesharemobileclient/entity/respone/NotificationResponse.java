package com.example.admin.ridesharemobileclient.entity.respone;

import java.util.List;

/**
 * Created by admin on 6/6/2019.
 */

public class NotificationResponse {
    private String total;
    private List<Notification> notifications;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
