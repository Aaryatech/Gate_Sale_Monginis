package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 11/01/18.
 */

public class ProductQty {

    int id, qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ProductQty{" +
                "id=" + id +
                ", qty=" + qty +
                '}';
    }
}
