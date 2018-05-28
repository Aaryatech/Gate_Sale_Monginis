package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class ErrorMessage {

    private Boolean error;
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
