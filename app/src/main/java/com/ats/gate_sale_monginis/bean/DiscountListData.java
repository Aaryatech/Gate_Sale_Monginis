package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class DiscountListData {

    private List<GateSaleDiscountList> gateSaleDiscountList;
    private ErrorMessage errorMessage;

    public List<GateSaleDiscountList> getGateSaleDiscountList() {
        return gateSaleDiscountList;
    }

    public void setGateSaleDiscountList(List<GateSaleDiscountList> gateSaleDiscountList) {
        this.gateSaleDiscountList = gateSaleDiscountList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "DiscountListData{" +
                "gateSaleDiscountList=" + gateSaleDiscountList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
