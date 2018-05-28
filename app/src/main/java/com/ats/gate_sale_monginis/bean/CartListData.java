package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 11/01/18.
 */

public class CartListData {

    private int id;
    private String itemId;
    private String itemName;
    private String itemImage;
    private double itemRate;
    private int quantity;
    private int birthday;

    public CartListData(int id, String itemId, String itemName, String itemImage, double itemRate, int quantity, int birthday) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemRate = itemRate;
        this.quantity = quantity;
        this.birthday = birthday;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public double getItemRate() {
        return itemRate;
    }

    public void setItemRate(double itemRate) {
        this.itemRate = itemRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartListData{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemRate=" + itemRate +
                ", quantity=" + quantity +
                ", birthday=" + birthday +
                '}';
    }
}
