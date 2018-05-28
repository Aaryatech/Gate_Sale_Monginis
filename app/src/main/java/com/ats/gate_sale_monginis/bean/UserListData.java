package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class UserListData {

    private List<GateSaleUserList> gateSaleUserList;
    private ErrorMessage errorMessage;

    public List<GateSaleUserList> getGateSaleUserList() {
        return gateSaleUserList;
    }

    public void setGateSaleUserList(List<GateSaleUserList> gateSaleUserList) {
        this.gateSaleUserList = gateSaleUserList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "UserListData{" +
                "gateSaleUserList=" + gateSaleUserList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
