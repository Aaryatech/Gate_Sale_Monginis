package com.ats.gate_sale_monginis.bean;

/**
 * Created by MAXADMIN on 14/2/2018.
 */

public class Info {

    private String message;
    private Boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Info{" +
                "message='" + message + '\'' +
                ", error=" + error +
                '}';
    }
}
