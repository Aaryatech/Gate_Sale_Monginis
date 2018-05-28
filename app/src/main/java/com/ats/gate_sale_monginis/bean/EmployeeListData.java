package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MIRACLEINFOTAINMENT on 11/01/18.
 */

public class EmployeeListData {

    private List<GetGateSaleEmpList> getGateSaleEmpList;
    private ErrorMessage errorMessage;

    public List<GetGateSaleEmpList> getGetGateSaleEmpList() {
        return getGateSaleEmpList;
    }

    public void setGetGateSaleEmpList(List<GetGateSaleEmpList> getGateSaleEmpList) {
        this.getGateSaleEmpList = getGateSaleEmpList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "EmployeeListData{" +
                "getGateSaleEmpList=" + getGateSaleEmpList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
