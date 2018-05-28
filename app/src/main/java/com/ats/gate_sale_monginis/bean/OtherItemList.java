package com.ats.gate_sale_monginis.bean;

/**
 * Created by MAXADMIN on 17/1/2018.
 */

public class OtherItemList {

    private Integer itemId;
    private Integer suppId;
    private String suppName;
    private String itemName;
    private String itemQty;
    private float itemRate;
    private Integer delStatus;

    public OtherItemList() {
    }

    public OtherItemList(Integer itemId, Integer suppId, String itemName, String itemQty, float itemRate, Integer delStatus) {
        this.itemId = itemId;
        this.suppId = suppId;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemRate = itemRate;
        this.delStatus = delStatus;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public float getItemRate() {
        return itemRate;
    }

    public void setItemRate(float itemRate) {
        this.itemRate = itemRate;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "OtherItemList{" +
                "itemId=" + itemId +
                ", suppId=" + suppId +
                ", suppName='" + suppName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", itemRate=" + itemRate +
                ", delStatus=" + delStatus +
                '}';
    }
}
