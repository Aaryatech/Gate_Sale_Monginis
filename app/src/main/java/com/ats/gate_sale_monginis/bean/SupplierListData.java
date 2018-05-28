package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MAXADMIN on 17/1/2018.
 */

public class SupplierListData {

    private List<OtherSupplierList> otherSupplierList;
    private ErrorMessage errorMessage;

    public List<OtherSupplierList> getOtherSupplierList() {
        return otherSupplierList;
    }

    public void setOtherSupplierList(List<OtherSupplierList> otherSupplierList) {
        this.otherSupplierList = otherSupplierList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SupplierListData{" +
                "otherSupplierList=" + otherSupplierList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
