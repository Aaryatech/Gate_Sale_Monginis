package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MAXADMIN on 17/1/2018.
 */

public class OtherItemListData {

    private List<OtherItemList> otherItemList;
    private ErrorMessage errorMessage;

    public List<OtherItemList> getOtherItemList() {
        return otherItemList;
    }

    public void setOtherItemList(List<OtherItemList> otherItemList) {
        this.otherItemList = otherItemList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "OtherItemListData{" +
                "otherItemList=" + otherItemList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
