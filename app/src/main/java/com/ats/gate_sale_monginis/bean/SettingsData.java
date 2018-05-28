package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MAXADMIN on 14/2/2018.
 */

public class SettingsData {

    private List<FrItemStockConfigure> frItemStockConfigure;
    private Info info;

    public List<FrItemStockConfigure> getFrItemStockConfigure() {
        return frItemStockConfigure;
    }

    public void setFrItemStockConfigure(List<FrItemStockConfigure> frItemStockConfigure) {
        this.frItemStockConfigure = frItemStockConfigure;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "frItemStockConfigure=" + frItemStockConfigure +
                ", info=" + info +
                '}';
    }
}
