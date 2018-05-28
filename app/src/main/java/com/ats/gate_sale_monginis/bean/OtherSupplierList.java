package com.ats.gate_sale_monginis.bean;

/**
 * Created by MAXADMIN on 17/1/2018.
 */

public class OtherSupplierList {

    private String message;
    private Integer suppId;
    private String suppName;
    private String suppAddr;
    private String suppMob;
    private Integer delStatus;
    private Boolean error;

    public OtherSupplierList() {
    }

    public OtherSupplierList(Integer suppId, String suppName, String suppAddr, String suppMob, Integer delStatus) {
        this.suppId = suppId;
        this.suppName = suppName;
        this.suppAddr = suppAddr;
        this.suppMob = suppMob;
        this.delStatus = delStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuppId() {
        return suppId;
    }

    public void setSuppId(Integer suppId) {
        this.suppId = suppId;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public String getSuppAddr() {
        return suppAddr;
    }

    public void setSuppAddr(String suppAddr) {
        this.suppAddr = suppAddr;
    }

    public String getSuppMob() {
        return suppMob;
    }

    public void setSuppMob(String suppMob) {
        this.suppMob = suppMob;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OtherSupplierList{" +
                "message='" + message + '\'' +
                ", suppId=" + suppId +
                ", suppName='" + suppName + '\'' +
                ", suppAddr='" + suppAddr + '\'' +
                ", suppMob='" + suppMob + '\'' +
                ", delStatus=" + delStatus +
                ", error=" + error +
                '}';
    }
}
