package com.example.admin.ridesharemobileclient.entity.respone;

/**
 * Created by admin on 4/6/2019.
 */

public class BaseRespone {
    private String code;
    private String message;
    private Object metadata;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
