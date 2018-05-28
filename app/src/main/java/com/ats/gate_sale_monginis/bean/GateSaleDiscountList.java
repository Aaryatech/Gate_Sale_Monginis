package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class GateSaleDiscountList {

    private String message;
    private Integer discountId;
    private String discountHead;
    private float discountPer;
    private Integer catId;
    private Integer userType;
    private Integer delStatus;
    private Boolean error;

    public GateSaleDiscountList(Integer discountId, String discountHead, float discountPer, Integer catId, Integer userType, Integer delStatus) {
        this.discountId = discountId;
        this.discountHead = discountHead;
        this.discountPer = discountPer;
        this.catId = catId;
        this.userType = userType;
        this.delStatus = delStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDiscountHead() {
        return discountHead;
    }

    public void setDiscountHead(String discountHead) {
        this.discountHead = discountHead;
    }

    public float getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(float discountPer) {
        this.discountPer = discountPer;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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
        return "GateSaleDiscountList{" +
                "message='" + message + '\'' +
                ", discountId=" + discountId +
                ", discountHead='" + discountHead + '\'' +
                ", discountPer=" + discountPer +
                ", catId=" + catId +
                ", userType=" + userType +
                ", delStatus=" + delStatus +
                ", error=" + error +
                '}';
    }
}
