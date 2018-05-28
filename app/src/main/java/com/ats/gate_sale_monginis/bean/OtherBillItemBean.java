package com.ats.gate_sale_monginis.bean;

/**
 * Created by MAXADMIN on 16/1/2018.
 */

public class OtherBillItemBean {

    private int vendorId;
    private String vendorName;
    private int itemId;
    private String itemName;
    private String itemQty;
    private int qty;
    private float perItemRate;
    private float rate;
    private int discount;

    public OtherBillItemBean(int vendorId, String vendorName, int itemId, String itemName, String itemQty, int qty, float rate) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.qty = qty;
        this.rate = rate;
    }

    public OtherBillItemBean(int vendorId, String vendorName, int itemId, String itemName, String itemQty, int qty, float rate, int discount) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.qty = qty;
        this.rate = rate;
        this.discount = discount;
    }

    public OtherBillItemBean(int vendorId, String vendorName, int itemId, String itemName, String itemQty, int qty, float perItemRate, float rate, int discount) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.qty = qty;
        this.perItemRate = perItemRate;
        this.rate = rate;
        this.discount = discount;
    }

    public float getPerItemRate() {
        return perItemRate;
    }

    public void setPerItemRate(float perItemRate) {
        this.perItemRate = perItemRate;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OtherBillItemBean{" +
                "vendorId=" + vendorId +
                ", vendorName='" + vendorName + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", qty=" + qty +
                ", perItemRate=" + perItemRate +
                ", rate=" + rate +
                ", discount=" + discount +
                '}';
    }
}
